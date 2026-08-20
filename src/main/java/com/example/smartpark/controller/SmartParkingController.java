package com.example.smartpark.controller;

import com.example.smartpark.dto.ParkingLotDTO;
import com.example.smartpark.dto.VehicleDTO;
import com.example.smartpark.service.SmartParkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *   SMARTPARK CONTROLLER
 */
@RequestMapping("/smartparking")
@RestController
public class SmartParkingController {

    SmartParkService smartParkService;

    public SmartParkingController(SmartParkService smartParkService) {
        this.smartParkService = smartParkService;
    }

    /*
     *   REGISTER PARKING LOT
     *   Method : POST
     *
     *   Accepts @RequestBody ParkingLotDTO
     *
     *   Returns ResponseEntity<ParkingLotDTO>
     * */
    @PostMapping("/register/parkinglot")
    public ResponseEntity<ParkingLotDTO> registerParkingLot(@RequestBody ParkingLotDTO parkingLotDetails){
        return new ResponseEntity<>(smartParkService.registerParkingLot(parkingLotDetails), HttpStatus.CREATED);
    }

    /*
     *   REGISTER VEHICLE
     *   Method : POST
     *
     *   Accepts @RequestBody VehicleDTO
     *
     *   Returns ResponseEntity<VehicleDTO>
     * */
    @PostMapping("/register/vehicle")
    public ResponseEntity<VehicleDTO> registerVehicle(@RequestBody VehicleDTO vehicleDetails){
        return new ResponseEntity<>(smartParkService.registerVehicle(vehicleDetails), HttpStatus.OK);
    }

    /*
     *   CHECK IN VEHICLE
     *   Method : PUT
     *
     *   Accepts @RequestParam license-plate, @RequestParam parking-lot-id
     *
     *   Returns ResponseEntity<List<VehicleDTO>>
     * */
    @PutMapping("/checkin/vehicle")
    public ResponseEntity<List<VehicleDTO>> checkInVehicle(@RequestParam("license-plate") String licensePlate,
                                                  @RequestParam("parking-lot-id") String parkingLotId){
        return new ResponseEntity<>(smartParkService.checkInVehicle(licensePlate,
                parkingLotId), HttpStatus.OK);
    }

    /*
     *   CHECK OUT VEHICLE
     *   Method : PUT
     *
     *   Accepts @RequestParam license-plate
     *
     *   Returns ResponseEntity<List<VehicleDTO>>
     * */
    @PutMapping("checkout/vehicle")
    public ResponseEntity<List<VehicleDTO>> checkoutVehicle(@RequestParam("license-plate") String licensePlate){
        return new ResponseEntity<>(smartParkService.checkOutVehicle(licensePlate), HttpStatus.OK);
    }

    /*
     *   VIEW PARKING LOT STATUS
     *   Method : GET
     *
     *   Accepts @RequestParam parking-lot-id
     *
     *   Returns ResponseEntity<List<ParkingLotDTO>>
     * */
    @GetMapping("/view/parkinglot")
    public ResponseEntity<List<ParkingLotDTO>> viewParkingLotStatus(@RequestParam("parking-lot-id") String parkingLotId){
        return new ResponseEntity<>(smartParkService.viewParkingStatus(parkingLotId), HttpStatus.OK);
    }

    /*
     *   VIEW VEHICLES IN A PARKING LOT
     *   Method : GET
     *
     *   Accepts @RequestParam parking-lot-id
     *
     *   Returns ResponseEntity<List<VehicleDTO>>
     * */
    @GetMapping("/view/vehicles")
    public ResponseEntity<List<VehicleDTO>> viewAllVehiclesPerLot(@RequestParam("parking-lot-id") String parkingLotId){
        return new ResponseEntity<>(smartParkService.viewAllVehiclePerLot(parkingLotId), HttpStatus.OK);
    }
}
