package com.mycode.pathpilotserver.vehicles.models;

import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "capacity", nullable = false)
    private int capacity;


    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShipmentDetail> shipmentDetails;

    public Vehicle(String vehicleType, String registrationNumber, int capacity) {
        this.type = vehicleType;
        this.registrationNumber = registrationNumber;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        String text = "Registration Number :"+registrationNumber+" Type :"+type+" Capacity :"+capacity;
        return text;
    }
}
