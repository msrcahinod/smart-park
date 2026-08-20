package com.example.smartpark.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
*   VEHICLE ENTITY
*/
@Entity
@Getter
@Setter
public class Vehicle {

    @Id
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name="type", nullable = false)
    private String type;

    @Column(name="owner_name", nullable = false)
    private String ownerName;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    @JsonBackReference
    private ParkingLot parkingLot;
}
