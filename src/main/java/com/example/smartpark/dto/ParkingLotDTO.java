package com.example.smartpark.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class ParkingLotDTO {

    private String lotId;

    private String location;

    private int capacity;

    private int occupiedSpaces;

}
