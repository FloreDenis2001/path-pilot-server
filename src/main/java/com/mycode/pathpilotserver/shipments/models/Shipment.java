package com.mycode.pathpilotserver.shipments.models;

import com.mycode.pathpilotserver.address.Address;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.shipmentDetails.models.ShipmentDetail;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "Shipments")
@Table(name = "shipments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Shipment {

    @Id
    @SequenceGenerator(name = "shipments_sequence", sequenceName = "shipments_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipments_sequence")
    @Column(name = "id", updatable = false)
    private Long id;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "origin_country")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city")),
            @AttributeOverride(name = "street", column = @Column(name = "origin_street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "origin_number")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "origin_postalCode"))
    })
    private Address originAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "destination_country")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city")),
            @AttributeOverride(name = "street", column = @Column(name = "destination_street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "destination_number")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "destination_postalCode"))
    })
    private Address destinationAddress;



    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "estimated_delivery_date", nullable = false)
    private LocalDateTime estimatedDeliveryDate;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShipmentDetail> shipmentDetails;

    public Shipment(Address destinationAddress, Address originAddress, String status, LocalDateTime localDateTime) {
        this.destinationAddress=destinationAddress;
        this.originAddress=originAddress;
        this.status=status;
        this.estimatedDeliveryDate=localDateTime;
    }


    @Override
    public String toString() {
        String text = " Status :"+status+" Estimated Delivery Date :"+estimatedDeliveryDate;
        return text;
    }


}
