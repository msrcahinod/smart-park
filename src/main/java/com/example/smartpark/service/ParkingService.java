package com.example.smartpark.service;

import com.example.smartpark.dto.ParkingLotDTO;
import com.example.smartpark.entity.ParkingLot;
import com.example.smartpark.entity.Vehicle;
import com.example.smartpark.dto.VehicleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ParkingService {

    void registerParkingLot(ParkingLot parkingLot);

    void registerVehicle(Vehicle vehicle);

    List<VehicleDTO> checkInVehicle(String licensePlate, String lotID);

    List<VehicleDTO> checkOutVehicle(String licensePlate, String lotID);

    List<ParkingLotDTO> viewParkingStatus(String parkingLotId);

    List<VehicleDTO> viewAllVehiclePerLot(String parkingLotId);

}
