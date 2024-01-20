package com.mycode.pathpilotserver.shipments.models;

import jakarta.persistence.*;

@Entity(name = "Shipments")
@Table(name = "shipments")
public class Shipments {

    @Id
    @SequenceGenerator(name = "shipments_sequence", sequenceName = "shipments_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipments_sequence")
    private Long id;


}
