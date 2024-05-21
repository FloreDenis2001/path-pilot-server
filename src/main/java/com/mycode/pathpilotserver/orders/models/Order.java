package com.mycode.pathpilotserver.orders.models;

import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.system.enums.OrderType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity(name = "Order")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Data
@SuperBuilder
public class Order  {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @Column(name = "id", updatable = false)
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
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "id", nullable = false)
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id")
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
