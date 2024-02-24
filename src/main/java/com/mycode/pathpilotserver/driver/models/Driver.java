package com.mycode.pathpilotserver.driver.models;

import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

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

    @Column(name="name",nullable = false)
    private String name;

    @Column(name="phone",nullable = false)
    private String phone;



    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShipmentDetail> shipmentDetails;

    public Driver(String license) {

        this.licenseNumber = license;
    }


    @Override
    public String toString() {
        String text = super.toString();
        text+= "License Number :"+licenseNumber;
        return text;
    }
}
