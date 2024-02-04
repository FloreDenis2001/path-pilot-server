package com.mycode.pathpilotserver.company.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "Company")
@Table(name = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@SuperBuilder
public class Company {

    @Id
    @SequenceGenerator(name = "company_sequence", sequenceName = "company_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_sequence")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "industry", nullable = false)
    private String industry;

    @Column(name = "capital", nullable = false)
    private double capital;

    @Column(name = "number_of_employees", nullable = false)
    private int numberOfEmployees;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "website", nullable = false)
    private String website;



}
