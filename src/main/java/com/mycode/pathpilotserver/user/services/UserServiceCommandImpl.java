package com.mycode.pathpilotserver.user.services;

import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.dto.LoginUserRequest;
import com.mycode.pathpilotserver.user.dto.RegisterDTO;
import com.mycode.pathpilotserver.user.dto.UpdateUserRequest;
import com.mycode.pathpilotserver.user.exceptions.UserNotFoundException;
import com.mycode.pathpilotserver.user.exceptions.WrongPasswordException;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserServiceCommandImpl implements UserServiceCommand {

    private final UserRepo userRepo;

    private final CompanyRepo companyRepo;

    public UserServiceCommandImpl(UserRepo userRepo, CompanyRepo companyRepo) {
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
    }


    @Override
    public void deleteUser(LoginUserRequest loginUserRequest) {
        Optional<User> user = userRepo.findByEmail(loginUserRequest.email());
        validatePassword(user.get(), loginUserRequest.password());
        user.ifPresentOrElse(userRepo::delete, () -> {
            throw new UserNotFoundException("User not found for email: " + loginUserRequest.email());
        });
    }

    @Override
    public void updateUser(UpdateUserRequest request) {
        User user = findUserByEmail(request.email());
        validatePassword(user, request.password());
        applyNewDetailsToUser(user, request.newUser());
        userRepo.save(user);
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

    @NotNull
    private static Company getCompany(RegisterDTO registerDTO) {
        Company company = new Company();
        company.setName(registerDTO.company().name());
        company.setRegistrationNumber(registerDTO.company().registrationNumber());
        company.setIndustry(registerDTO.company().industry());
        company.setPhone(registerDTO.company().phone());
        company.setEmail(registerDTO.company().email());
        company.setCapital(registerDTO.company().capital());
        company.setAddress(registerDTO.company().address());
        company.setWebsite(registerDTO.company().website());
        company.setCreatedBy(registerDTO.user().username());
        company.setLastModifiedBy(registerDTO.user().username());
        company.setCreatedDate(LocalDateTime.now());
        company.setLastModifiedDate(LocalDateTime.now());
        return company;
    }


    @NotNull
    private static Customer getCustomer(RegisterDTO registerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(registerDTO.user().firstName());
        customer.setLastName(registerDTO.user().lastName());
        customer.setPhone(registerDTO.user().phone());
        customer.setRole(UserRole.CUSTOMER);
        customer.setEmail(registerDTO.user().email());
        customer.setPassword(registerDTO.user().password());
        customer.setUsername(registerDTO.user().username());
        customer.setSubscriptionType(SubscriptionType.BASIC);
        customer.setAddress(registerDTO.user().address());

        return customer;
    }

    private User findUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
    }

    private void validatePassword(User user, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException("Invalid password for user: " + user.getEmail());
        }
    }

    private void applyNewDetailsToUser(User user, User newUserDetails) {
        user.setEmail(newUserDetails.getEmail());
        user.setPassword(newUserDetails.getPassword());
        user.setUsername(newUserDetails.getUsername());
    }

}
