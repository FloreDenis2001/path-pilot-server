package com.mycode.pathpilotserver.image.repository;

import com.mycode.pathpilotserver.PathPilotServerApplication;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.company.repository.CompanyRepo;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = PathPilotServerApplication.class)
class ImageRepoTest {
    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Test
    void testSaveImage() {
        Image image = Image.builder()
                .name("Test Image")
                .fileType("image/png")
                .data(new byte[]{1, 2, 3})
                .build();

        Image savedImage = imageRepo.save(image);

        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getId()).isNotNull();
        assertThat(savedImage.getName()).isEqualTo("Test Image");
    }

    @Test
    void testFindImageByUserEmail() {
        Image image = Image.builder()
                .name("Test Image")
                .fileType("image/png")
                .data(new byte[]{1, 2, 3})
                .build();
        Image savedImage = imageRepo.save(image);

        Company company = Company.builder()
                .name("Test Company")
                .registrationNumber("TEST123")
                .industry("Transport")
                .income(1000000.0)
                .phone("0123456789")
                .email("contact@testcompany.com")
                .website("http://testcompany.com")
                .address(null)
                .build();
        companyRepo.save(company);

        User customer = Customer.builder()
                .username("customer_user")
                .password("password")
                .email("customer@example.com")
                .role(UserRole.CUSTOMER)
                .firstName("Customer")
                .lastName("User")
                .phone("1234567890")
                .address(null)
                .company(company)
                .subscriptionType(SubscriptionType.BASIC)
                .image(savedImage)
                .build();
        userRepo.save(customer);

        Optional<Image> retrievedImage = imageRepo.findImageByUserEmail("customer@example.com");

        assertThat(retrievedImage).isPresent();
        assertThat(retrievedImage.get().getName()).isEqualTo("Test Image");
    }
}