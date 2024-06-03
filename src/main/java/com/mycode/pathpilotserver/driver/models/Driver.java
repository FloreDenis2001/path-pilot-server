package com.mycode.pathpilotserver.driver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "Driver")
@Table(name = "drivers")
@NoArgsConstructor
@Getter
@Setter
@Data
@SuperBuilder
@AllArgsConstructor
public class Driver extends User{

    @Id
    @SequenceGenerator(name = "driver_sequence", sequenceName = "driver_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "salary", nullable = false)
    private double salary;


    @Column(name="is_available", nullable = false)
    private boolean isAvailable;

    @Column(name="rating", nullable = false)
    private double rating;

    @Column(name="experience", nullable = false)
    private int experience;


    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Route> routes;



    @Override
    public String toString() {
        String text = super.toString();
        text+= "License Number :"+licenseNumber;
        return text;
    }


}
