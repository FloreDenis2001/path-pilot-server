package com.mycode.pathpilotserver.shipmentDetails.models;

import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

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

    @OneToMany(mappedBy = "shipmentDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

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




    @Override
    public String toString() {
        return "ShipmentDetail{" +
                "id=" + id +
                ", orders=" + orders +
                ", vehicle=" + vehicle +
                ", driver=" + driver +
                ", departureDate=" + departureDate +
                ", arrivalTime=" + arrivalTime +
                '}';
    }








}
