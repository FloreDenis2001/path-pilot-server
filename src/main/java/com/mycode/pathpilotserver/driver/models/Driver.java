package com.mycode.pathpilotserver.driver.models;

import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import com.mycode.pathpilotserver.user.models.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "Driver")
@Table(name = "drivers")
@NoArgsConstructor
@Getter
@Setter
@Data
@SuperBuilder
public class Driver extends User{

    @Id
    @SequenceGenerator(name = "driver_sequence", sequenceName = "driver_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "phone", nullable = false)
    private String phone;


    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShipmentDetail> shipmentDetails;

    public Driver(String driverName, String number, String license123) {
        this.name = driverName;
        this.phone = number;
        this.licenseNumber = license123;
    }


    @Override
    public String toString() {
        String text = "Name :"+name+" License Number :"+licenseNumber+" Phone :"+phone;
        return text;
    }
}
