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
public class Driver extends User {

    @Id
    @SequenceGenerator(name = "driver_sequence", sequenceName = "driver_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "salary", nullable = false)
    private double salary;


    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "experience", nullable = false)
    private int experience;


    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Route> routes;

    public void increaseSalaryByKilometers(double kilometers) {
        double increaseAmount = kilometers * 0.02;
        this.salary += increaseAmount;
        this.salary = Math.round(this.salary * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.setLength(sb.length() - 1);
        sb.append("licenseNumber='").append(licenseNumber).append('\'');
        sb.append(", salary=").append(salary);
        sb.append(", isAvailable=").append(isAvailable);
        sb.append(", rating=").append(rating);
        sb.append(", experience=").append(experience);
        sb.append('}');
        return sb.toString();
    }
}
