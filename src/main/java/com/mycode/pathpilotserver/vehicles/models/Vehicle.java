package com.mycode.pathpilotserver.vehicles.models;

import com.mycode.pathpilotserver.routes.models.Route;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "Vehicle")
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
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

    @Column(name = "isActive", nullable = false)
    private boolean isActive;


    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Route> routes;


    public Vehicle(String make, String model, int year, FuelType fuelType, double fuelCapacity, double fuelConsumption, LocalDate lastService, LocalDate nextService, double km, String registrationNumber, int capacity) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.fuelCapacity = fuelCapacity;
        this.fuelConsumption = fuelConsumption;
        this.lastService = lastService;
        this.nextService = nextService;
        this.km = km;
        this.registrationNumber = registrationNumber;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Vehicle{");
        sb.append(", make='").append(make).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", year=").append(year);
        sb.append(", fuelType=").append(fuelType);
        sb.append(", fuelCapacity=").append(fuelCapacity);
        sb.append(", fuelConsumption=").append(fuelConsumption);
        sb.append(", lastService=").append(lastService);
        sb.append(", nextService=").append(nextService);
        sb.append(", km=").append(km);
        sb.append(", registrationNumber='").append(registrationNumber).append('\'');
        sb.append(", capacity=").append(capacity);
        sb.append(", routes=").append(routes);
        sb.append('}');
        return sb.toString();
    }
}
