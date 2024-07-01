package com.mycode.pathpilotserver.vehicles.models;

import com.fasterxml.jackson.annotation.*;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.routes.models.Route;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity(name = "Vehicle")
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehicle {

    @Id
    @SequenceGenerator(name = "vehicle_sequence", sequenceName = "vehicle_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "fuel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;


    @Column(name = "fuel_capacity", nullable = false)
    private double fuelCapacity;

    @Column(name = "fuel_consumption", nullable = false)
    private double fuelConsumption;


    @Column(name = "last_service", nullable = false)
    private LocalDate lastService;

    @Column(name = "next_service", nullable = false)
    private LocalDate nextService;

    @Column(name = "kilometres", nullable = false)
    private double km;


    @Column(name = "registration_number", nullable = false)
    @Unique
    private String registrationNumber;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "length", nullable = false)
    private double length;

    @Column(name = "weight", nullable = false)
    private double weight;


    @Column(name = "isActive", nullable = false)
    private boolean isActive;


    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Route> routes;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @JsonBackReference
    private Company company;


    public double getVolume() {
        return this.height * this.width * this.length;
    }

    public void increaseKilometers(double kilometers) {
        this.km += kilometers;
    }

    @Override
    public String toString() {
        String sb = "Vehicle{" + "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", fuelType=" + fuelType +
                ", fuelCapacity=" + fuelCapacity +
                ", fuelConsumption=" + fuelConsumption +
                ", lastService=" + lastService +
                ", nextService=" + nextService +
                ", km=" + km +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", capacity=" + capacity +
                ", width=" + width +
                ", height=" + height +
                ", length=" + length +
                ", weight=" + weight +
                ", isActive=" + isActive +
                '}';
        return sb;
    }
}
