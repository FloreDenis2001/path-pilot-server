package com.mycode.pathpilotserver.packages.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity(name = "Package")
@Table(name = "package")
@Getter
@Setter
@NoArgsConstructor
@Data
@SuperBuilder
public class Package {

    @Id
    @SequenceGenerator(name = "package_sequence", sequenceName = "package_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_sequence")
    @Column(name = "id", updatable = false)
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

    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageStatus status;


    @Column(name = "delivery_description", nullable = false)
    private String deliveryDescription;


    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;



    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName ="id",nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id",referencedColumnName ="id",nullable = false)
    @JsonBackReference
    private Shipment shipment;


    @Override
    public boolean equals(Object obj) {
        Package pack = (Package) obj;
        return this.awb.equals(pack.getAwb());
    }

    public double getVolume() {
        return this.height * this.width * this.length;
    }

    @Override
    public String toString() {
        String sb = "Package{" + "awb='" + awb + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", width=" + width +
                ", length=" + length +
                ", status=" + status +
                ", deliveryDescription='" + deliveryDescription + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", shipment=" + shipment +
                '}';
        return sb;
    }
}
