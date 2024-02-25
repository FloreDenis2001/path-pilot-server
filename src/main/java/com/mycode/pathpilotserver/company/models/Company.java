package com.mycode.pathpilotserver.company.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.system.audit.AbstractAuditingEntity;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "Company")
@Table(name = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class Company extends AbstractAuditingEntity {

    @Id
    @SequenceGenerator(name = "company_sequence", sequenceName = "company_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
    private Long id;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<User> users;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "number")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code"))
    })
    private Address address;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "industry", nullable = false)
    private String industry;

    @Column(name = "capital", nullable = false)
    private double capital;


    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "website", nullable = false)
    private String website;

    @Override
    public String toString() {
        String text = "Name : "+name+" Registration Number : "+registrationNumber+" Industry : "+industry+" Capital : "+capital+" Phone : "+phone+" Email : "+email+" Website : "+website;
        return text;
    }





}
