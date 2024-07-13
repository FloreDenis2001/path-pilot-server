package com.mycode.pathpilotserver.routes.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "route")
@Entity(name = "Route")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Route {

    @Id
    @SequenceGenerator(name = "route_sequence", sequenceName = "route_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_sequence")
    @Column(name = "id", updatable = false)
    @EqualsAndHashCode.Include
    private Long id;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    @JsonManagedReference
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @JsonManagedReference
    private Driver driver;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Company company;

    @Column(name = "departure_date", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime departureDate;

    @Column(name = "arrival_time", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime arrivalTime;


    @Column(name = "total_distance", nullable = false)
    private double totalDistance;

    @Column(name="start_point", nullable = false)
    private String startPoint;

    @Column(name="end_point", nullable = false)
    private String endPoint;


    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @OrderBy("deliverySequence ASC")
    private Set<Order> orders = new HashSet<>();
    public void addOrder(Order order) {
        orders.add(order);
        order.setRoute(this);
    }



    @Override
    public String toString() {
        String sb = "Route{" + "id=" + id +
                ", orders=" + orders +
                ", vehicle=" + vehicle +
                ", driver=" + driver +
                ", departureDate=" + departureDate +
                ", arrivalTime=" + arrivalTime +
                '}';
        return sb;
    }


}
