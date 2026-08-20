package com.example.smartpark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 *   PARKING LOT DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotDTO {

    @NotBlank(message = "Parking Lot ID is required")
    private String lotId;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Capacity is required")
    private int capacity;

    private int occupiedSpaces;

}
