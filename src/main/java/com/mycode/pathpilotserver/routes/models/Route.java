package com.mycode.pathpilotserver.routes.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Driver driver;

    @Column(name = "departure_date", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime departureDate;

    @Column(name = "arrival_time", nullable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime arrivalTime;


    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonBackReference
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order) {
        orders.add(order);
        order.setRoute(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Route{");
        sb.append("id=").append(id);
        sb.append(", orders=").append(orders);
        sb.append(", vehicle=").append(vehicle);
        sb.append(", driver=").append(driver);
        sb.append(", departureDate=").append(departureDate);
        sb.append(", arrivalTime=").append(arrivalTime);
        sb.append('}');
        return sb.toString();
    }


}
