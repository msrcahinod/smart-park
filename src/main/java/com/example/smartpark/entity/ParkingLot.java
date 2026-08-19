package com.example.smartpark.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ParkingLot {

    @Id
    @Column(name="lot_id", nullable = false)
    private String lotId;

    @Column(name="location", nullable = false)
    private String location;

    @Column(name="capacity", nullable = false)
    private int capacity;

    @Column(name="occupied_spaces", nullable = false)
    private int occupiedSpaces;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Vehicle> vehicles = new ArrayList<>();
}
