package com.mycode.pathpilotserver.shipments.models;

import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.orders.models.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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


    @Column(name="origin_name", nullable = false)
    private String originName;

    @Column(name="destination_name", nullable = false)
    private String destinationName;

    @Column(name="origin_phone", nullable = false)
    private String originPhone;

    @Column(name="destination_phone", nullable = false)
    private String destinationPhone;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "origin_country")),
            @AttributeOverride(name = "city", column = @Column(name = "origin_city")),
            @AttributeOverride(name = "street", column = @Column(name = "origin_street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "origin_number")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "origin_postalCode")),
            @AttributeOverride(name = "lat", column = @Column(name = "origin_lat")),
            @AttributeOverride(name = "lng", column = @Column(name = "origin_lng")),
            @AttributeOverride(name = "iso2", column = @Column(name = "origin_iso2")),
            @AttributeOverride(name = "admin_name", column = @Column(name = "origin_admin_name")),
            @AttributeOverride(name = "capital", column = @Column(name = "origin_capital")),
            @AttributeOverride(name = "population", column = @Column(name = "origin_population")),
            @AttributeOverride(name = "population_proper", column = @Column(name = "origin_population_proper"))

    })
    private Address originAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "destination_country")),
            @AttributeOverride(name = "city", column = @Column(name = "destination_city")),
            @AttributeOverride(name = "street", column = @Column(name = "destination_street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "destination_number")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "destination_postalCode")),
            @AttributeOverride(name = "lat", column = @Column(name = "destination_lat")),
            @AttributeOverride(name = "lng", column = @Column(name = "destination_lng")),
            @AttributeOverride(name = "iso2", column = @Column(name = "destination_iso2")),
            @AttributeOverride(name = "admin_name", column = @Column(name = "destination_admin_name")),
            @AttributeOverride(name = "capital", column = @Column(name = "destination_capital")),
            @AttributeOverride(name = "population", column = @Column(name = "destination_population")),
            @AttributeOverride(name = "population_proper", column = @Column(name = "destination_population_proper"))
    })
    private Address destinationAddress;



    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Column(name = "estimated_delivery_date", nullable = false)
    private LocalDateTime estimatedDeliveryDate;


    @Column(name = "total_distance", nullable = false)
    private double totalDistance;




    @OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Order order;



    public Shipment(Address destinationAddress, Address originAddress, StatusType status, LocalDateTime localDateTime) {
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
