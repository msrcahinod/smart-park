package com.example.smartpark.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
 *   VEHICLE DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class VehicleDTO {

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotBlank(message = "Car Type is required")
    private String type;

    @NotBlank(message = "Owner Name is required")
    private String ownerName;

    private String parkingLot;

}
