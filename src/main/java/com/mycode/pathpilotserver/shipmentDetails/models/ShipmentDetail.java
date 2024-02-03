package com.mycode.pathpilotserver.shipmentDetails.models;

import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDateTime;

@Table(name = "shipment_details")
@Entity(name = "ShipmentDetails")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Data
public class ShipmentDetail {

    @Id
    @SequenceGenerator(name = "shipment_details_sequence", sequenceName = "shipment_details_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipment_details_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id",referencedColumnName ="id",nullable = false)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",referencedColumnName ="id",nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",referencedColumnName ="id",nullable = false)
    private Driver driver;

    @Column(name="departure_date", nullable = false)
    private LocalDateTime departureDate;

    @Column(name="arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    public ShipmentDetail(Shipment shipment, Driver driver, Vehicle vehicle, LocalDateTime now, LocalDateTime localDateTime) {
        this.shipment = shipment;
        this.driver = driver;
        this.vehicle = vehicle;
        this.departureDate = now;
        this.arrivalTime = localDateTime;
    }

    @Override
    public String toString() {
        String text = "Id:"+ shipment.getId()+"Shipment Id :"+shipment.getId()+" Vehicle Id :"+vehicle.getId()+" Driver Id :"+driver.getId()+" Departure Date :"+departureDate+" Arrival Time :"+arrivalTime;
        return text;
    }

}
