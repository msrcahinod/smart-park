package com.example.smartpark.controller;

import com.example.smartpark.dto.ParkingLotDTO;
import com.example.smartpark.entity.ParkingLot;
import com.example.smartpark.entity.Vehicle;
import com.example.smartpark.dto.VehicleDTO;
import com.example.smartpark.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequestMapping("/smartparking")
@RestController
public class SmartParkingController {

    ParkingService parkingService;

    public SmartParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/register/parkinglot")
    public ResponseEntity<String> registerParkingLot(@RequestBody ParkingLot parkingLotDetails){
        parkingService.registerParkingLot(parkingLotDetails);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/register/vehicle")
    public ResponseEntity<String> registerVehicle(@RequestBody Vehicle vehicleDetails){
        parkingService.registerVehicle(vehicleDetails);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/checkin/vehicle/")
    public ResponseEntity<List<VehicleDTO>> checkInVehicleP(@RequestParam("license-plate") String licensePlate,
                                                  @RequestParam("parking-lot-id") String parkingLotId){
        return new ResponseEntity<>(parkingService.checkInVehicle(licensePlate,
                parkingLotId), HttpStatus.OK);
    }

    @PutMapping("checkout/vehicle/")
    public ResponseEntity<List<VehicleDTO>> checkoutVehicle(@RequestParam("license-plate") String licensePlate,
                                                  @RequestParam("parking-lot-id") String parkingLotId){
        return new ResponseEntity<>(parkingService.checkOutVehicle(licensePlate, parkingLotId), HttpStatus.OK);
    }

    @GetMapping("/view/parkinglot/{parkingLotId}")
    public ResponseEntity<List<ParkingLotDTO>> viewParkingLotStatus(@PathVariable String parkingLotId){

        return new ResponseEntity<>(parkingService.viewParkingStatus(parkingLotId), HttpStatus.OK);
    }

    @GetMapping("/view/vehicles/{parkingLotId}")
    public ResponseEntity<List<VehicleDTO>> viewAllVehiclesPerLot(@PathVariable String parkingLotId){
        return new ResponseEntity<>(parkingService.viewAllVehiclePerLot(parkingLotId), HttpStatus.OK);
    }
}
