package com.mycode.pathpilotserver.orders.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "Order")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order  {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @Column(name = "id", updatable = false)
    @EqualsAndHashCode.Include
    private Long id;


    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "height", nullable = false)
    private double height;

    @Column(name = "width", nullable = false)
    private double width;


    @Column(name = "delivery_description", nullable = false)
    private String deliveryDescription;


    @Column(name = "order_date", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
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


    public Order(LocalDateTime orderDate, double totalAmount, Customer customer) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
    }

    @Override
    public String toString() {
        String text = "Order Date :" + orderDate + " Total Amount :" + totalAmount + " Customer :" + customer + " Shipment :" + shipment;
        return text;
    }


}
