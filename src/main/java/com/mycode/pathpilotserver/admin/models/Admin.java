package com.mycode.pathpilotserver.admin.models;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
@Entity(name = "Admin")
@Table(name = "admins")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Admin extends User {


    @Id
    @SequenceGenerator(name = "admins_sequence", sequenceName = "admins_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admins_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "number")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code"))
    })
    private Address address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    public Admin(String name, Address address, String phone) {
        this.name = name;
        this.address=address;
        this.phone = phone;
    }

    @Override
    public String toString() {
        String text = "Name :"+name+" Phone :"+phone+"\n"+address.toString();
        return text;
    }
}
