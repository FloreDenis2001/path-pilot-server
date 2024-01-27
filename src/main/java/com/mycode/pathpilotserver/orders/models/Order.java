package com.mycode.pathpilotserver.orders.models;

import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity(name = "Order")
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Data
@SuperBuilder
public class Order {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName ="id",nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shipment_id",referencedColumnName ="id",nullable = false)
    private Shipment shipment;

    @Override
    public String toString() {
        String text = "Order Date :"+orderDate+" Total Amount :"+totalAmount+" Customer :"+customer+" Shipment :"+shipment;
        return text;
    }

}
