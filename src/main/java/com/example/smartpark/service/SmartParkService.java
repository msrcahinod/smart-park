package com.example.smartpark.service;

import com.example.smartpark.dto.ParkingLotDTO;
import com.example.smartpark.dto.VehicleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 *   SMARTPARK SERVICE
 */
@Service
public interface SmartParkService {

    ParkingLotDTO registerParkingLot(ParkingLotDTO parkingLot);

    VehicleDTO registerVehicle(VehicleDTO vehicle);

    List<VehicleDTO> checkInVehicle(String licensePlate, String lotID);

    List<VehicleDTO> checkOutVehicle(String licensePlate);

    List<ParkingLotDTO> viewParkingStatus(String parkingLotId);

    List<VehicleDTO> viewAllVehiclePerLot(String parkingLotId);

}
