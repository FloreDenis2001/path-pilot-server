package com.mycode.pathpilotserver.customers.models;

import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.system.security.UserRole;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "Customers")
@Table(name = "customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Customer extends User {

    @Id
    @SequenceGenerator(name = "customers_sequence", sequenceName = "customers_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "subscriptionType", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    public Customer(Long id, String firstName, String lastName, String username, String password, String email, String phone, UserRole role, Address address, Company company, SubscriptionType subscriptionType) {
        super(id, firstName, lastName, username, password, email, phone, role, address, company);
        this.subscriptionType = subscriptionType;
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;


    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}
