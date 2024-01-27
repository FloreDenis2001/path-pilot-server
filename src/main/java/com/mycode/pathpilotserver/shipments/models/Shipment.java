package com.mycode.pathpilotserver.shipments.models;

import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "Shipments")
@Table(name = "shipments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Data
public class Shipment {

    @Id
    @SequenceGenerator(name = "shipments_sequence", sequenceName = "shipments_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipments_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "estimated_delivery_date", nullable = false)
    private LocalDateTime estimatedDeliveryDate;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShipmentDetail> shipmentDetails;

    @Override
    public String toString() {
        String text = "Origin :"+origin+" Destination :"+destination+" Status :"+status+" Estimated Delivery Date :"+estimatedDeliveryDate;
        return text;
    }


}
