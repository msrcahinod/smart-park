package com.example.smartpark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VehicleDTO {

    private String licensePlate;

    private String type;

    private String ownerName;

    private String parkingLot;

}
