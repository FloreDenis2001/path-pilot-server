package com.mycode.pathpilotserver.customers.models;

import com.mycode.pathpilotserver.orders.models.Order;
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
public class Customer {

    @Id

    @SequenceGenerator(name = "customers_sequence", sequenceName = "customers_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    public Customer(String name, String address, String phone, User user) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.user = user;
    }

    @Override
    public String toString() {
        String text = "Name :"+name+" Phone :"+phone+" Address :"+address;
        return text;
    }
}
