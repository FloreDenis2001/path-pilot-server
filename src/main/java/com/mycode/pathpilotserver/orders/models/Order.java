package com.mycode.pathpilotserver.orders.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity(name = "Order")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @Column(name = "id", updatable = false)
    @EqualsAndHashCode.Include
    @JsonIgnore
    private Long id;

    @Column(name = "awb", nullable = false)
    private String awb;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "width", nullable = false)
    private double width;

    @Column(name="length", nullable = false)
    private double length;

    @Column(name = "delivery_description", nullable = false)
    private String deliveryDescription;

    @Column(name = "order_date", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    @JsonBackReference
    private Route route;

    @Override
    public String toString() {
        return "Order{" +
                "awb='" + awb + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", width=" + width +
                ", length=" + length +
                ", deliveryDescription='" + deliveryDescription + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
