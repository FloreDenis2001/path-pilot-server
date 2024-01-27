package com.mycode.pathpilotserver.user.models;


import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.orders.models.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Data
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private Driver driver;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;

    @Override
    public String toString() {
       String text = "Email :"+email+" Username :"+username+" Role :"+role;
         return text;
    }


}
