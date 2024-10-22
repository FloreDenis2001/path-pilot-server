package com.mycode.pathpilotserver.customers.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "Customers")
@Table(name = "customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends User {

    @Id
    @SequenceGenerator(name = "customers_sequence", sequenceName = "customers_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "subscriptionType", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private Set<Order> orders;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private Set<Package> packages;

}
