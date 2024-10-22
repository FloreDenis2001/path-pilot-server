package com.mycode.pathpilotserver.user.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.email.services.EmailServiceCommandImpl;
import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.image.repository.ImageRepo;
import com.mycode.pathpilotserver.image.utils.ImageUtils;
import com.mycode.pathpilotserver.system.jwt.JWTTokenProvider;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.dto.DeleteUserRequest;
import com.mycode.pathpilotserver.user.dto.RegisterDTO;
import com.mycode.pathpilotserver.user.dto.ResetPasswordRequest;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import com.mycode.pathpilotserver.user.exceptions.UnauthorizedAccessException;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.mycode.pathpilotserver.city.utils.Utils.getCityByName;

@Service
@Transactional
public class UserServiceCommandImpl implements UserServiceCommand {

    private final UserRepo userRepo;

    private final ImageRepo imageRepo;

    private final CompanyRepo companyRepo;

    private final JavaMailSender mailSender;
    private final JWTTokenProvider jwtTokenProvider;


    public UserServiceCommandImpl(UserRepo userRepo, ImageRepo imageRepo, CompanyRepo companyRepo, JavaMailSender mailSender, JWTTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.userRepo = userRepo;
        this.imageRepo = imageRepo;
        this.companyRepo = companyRepo;
        this.mailSender = mailSender;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void deleteUser(DeleteUserRequest deleteUserRequest) {
        if (jwtTokenProvider.isTokenValid(deleteUserRequest.email(), deleteUserRequest.token())) {
            Optional<User> userOptional = userRepo.findByEmail(deleteUserRequest.email());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole().equals(UserRole.CUSTOMER)) {
                    companyRepo.delete(user.getCompany());
                    userRepo.delete(user);
                } else {
                    userRepo.delete(user);
                }
            } else {
                throw new UserNotFoundException("User not found for email: " + deleteUserRequest.email());
            }

        } else {
            throw new UnauthorizedAccessException("Unauthorized access: Invalid token");
        }
    }


    @Override
    public void updateUser(UpdateUserRequest request) {
        User user = findUserByEmail(request.email());
        applyNewDetailsToUser(user, request);
        userRepo.save(user);
    }

    @Override
    public String uploadImage(MultipartFile file, String email) {
        try {
            Optional<User> user = userRepo.findByEmail(email);
            if (user.isEmpty()) {
                throw new UserNotFoundException("User not found for email: " + email);
            }

            byte[] compressedData = ImageUtils.compressImage(file.getBytes());
            Image existingImage = user.get().getImage();

            if (existingImage != null) {
                existingImage.setData(compressedData);
                existingImage.setFileType(file.getContentType());
                existingImage.setName(file.getOriginalFilename());
                imageRepo.save(existingImage);
                return "File uploaded successfully: " + file.getOriginalFilename();
            } else {
                return createNewImage(file, user);
            }

        } catch (IOException e) {
            throw new UnauthorizedAccessException("This file type is not supported");
        }
    }

    private String createNewImage(MultipartFile file, Optional<User> user) {
        try {
            byte[] compressedData = ImageUtils.compressImage(file.getBytes());
            Image imageData = Image.builder().name(file.getOriginalFilename()).fileType(file.getContentType()).data(compressedData).build();

            Image savedImage = imageRepo.save(imageData);

            if (user.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            user.get().setImage(savedImage);

            userRepo.save(user.get());
            return "File created successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            throw new UnauthorizedAccessException("This file type is not supported");
        }
    }

    @Override
    public void registerUser(RegisterDTO registerDTO) {
        Optional<User> user = userRepo.findByEmail(registerDTO.user().email());

        if (user.isPresent()) {
            throw new UserNotFoundException("User with email: " + registerDTO.company().email() + " already exists");
        }

        Company company = getCompany(registerDTO);
        companyRepo.saveAndFlush(company);

        Customer customer = getCustomer(registerDTO);
        customer.setCompany(company);
        userRepo.saveAndFlush(customer);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Optional<User> user = userRepo.findByEmail(resetPasswordRequest.email());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found for email: " + resetPasswordRequest.email());
        }

        EmailServiceCommandImpl.isLinkValid(resetPasswordRequest.code());
        user.get().setPassword(resetPasswordRequest.password());


        userRepo.save(user.get());
        EmailServiceCommandImpl.removeLinkAfterCreation(resetPasswordRequest.code());
        sendPasswordChangedNotification(user.get().getEmail());
    }


    private void sendPasswordChangedNotification(String userEmail) {
        String subject = "Password Changed";
        String message = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>Password Changed</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; }"
                + ".container { text-align: center; }"
                + ".message { margin-top: 20px; background-color: #f2f2f2; padding: 20px; border-radius: 10px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h2 style=\"color: #007bff;\">Password Changed</h2>"
                + "<div class=\"message\">"
                + "<p>Your password has been successfully changed.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("pathpilot116@gmail.com");
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(message, true);
        };

        mailSender.send(preparator);
    }

    @NotNull
    private Company getCompany(RegisterDTO registerDTO) {
        Company company = new Company();
        company.setName(registerDTO.company().name());
        company.setRegistrationNumber(registerDTO.company().registrationNumber());
        company.setIndustry(registerDTO.company().industry());
        company.setPhone(registerDTO.company().phone());
        company.setEmail(registerDTO.company().email());
        company.setIncome(registerDTO.company().capital());

        City city = getCityByName(registerDTO.user().address().city());
        Address fullAddress = buildAddress(city, registerDTO.user().address());

        company.setAddress(fullAddress);
        company.setWebsite(registerDTO.company().website());
        company.setCreatedBy(registerDTO.user().username());
        company.setLastModifiedBy(registerDTO.user().username());
        company.setCreatedDate(LocalDateTime.now());
        company.setLastModifiedDate(LocalDateTime.now());
        return company;
    }


    @NotNull
    private Customer getCustomer(RegisterDTO registerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(registerDTO.user().firstName());
        customer.setLastName(registerDTO.user().lastName());
        customer.setPhone(registerDTO.user().phone());
        customer.setRole(UserRole.CUSTOMER);
        customer.setEmail(registerDTO.user().email());
        customer.setPassword(registerDTO.user().password());
        customer.setUsername(registerDTO.user().username());
        customer.setSubscriptionType(SubscriptionType.BASIC);

        City city = getCityByName(registerDTO.user().address().city());
        Address fullAddress = buildAddress(city, registerDTO.user().address());

        customer.setAddress(fullAddress);

        return customer;
    }

    private Address buildAddress(City city, AddressDTO addressDTO) {
        return Address.builder()
                .cityDetails(city)
                .street(addressDTO.street())
                .postalCode(addressDTO.postalCode())
                .streetNumber(addressDTO.streetNumber())
                .build();
    }


    private User findUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
    }


    private void applyNewDetailsToUser(User user, UpdateUserRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.getAddress().setStreet(request.street());
        user.getAddress().setStreetNumber(request.streetNumber());
        user.getAddress().setPostalCode(request.postalCode());
        user.getAddress().setCityDetails(getCityByName(request.city()));
    }

}