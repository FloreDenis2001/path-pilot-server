package com.mycode.pathpilotserver.routes.models;

import com.mycode.pathpilotserver.driver.models.Driver;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "route")
@Entity(name = "Route")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Data
public class Route {

    @Id
    @SequenceGenerator(name = "route_sequence", sequenceName = "route_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",referencedColumnName ="id",nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",referencedColumnName ="id",nullable = false)
    private Driver driver;

    @Column(name="departure_date", nullable = false)
    private LocalDateTime departureDate;

    @Column(name="arrival_time", nullable = false)
    private LocalDateTime arrivalTime;


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
