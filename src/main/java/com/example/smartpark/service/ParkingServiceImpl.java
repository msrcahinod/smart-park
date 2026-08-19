package com.example.smartpark.service;

import com.example.smartpark.dto.ParkingLotDTO;
import com.example.smartpark.entity.ParkingLot;
import com.example.smartpark.entity.Vehicle;
import com.example.smartpark.dto.VehicleDTO;
import com.example.smartpark.exception.SmartParkException;
import com.example.smartpark.repository.ParkingLotRepository;
import com.example.smartpark.repository.VehicleRepository;
import com.example.smartpark.utils.Constants;
import com.example.smartpark.utils.MockData;
import com.example.smartpark.utils.Validation;
import com.example.smartpark.utils.VehicleType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService, Validation {

    private final ParkingLotRepository parkingLotRepository;

    private final VehicleRepository vehicleRepository;


    public ParkingServiceImpl(ParkingLotRepository parkingLotRepository, VehicleRepository vehicleRepository, MockData mockData) {
        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;

        //Fill Database with Dummy Data
        mockData.fillParkingLot();

    }

    @Override
    public void registerParkingLot(ParkingLot parkingLotDetails) {

        //Check if Parking Lot Id is valid
        if (isValidParkingLotId(parkingLotDetails.getLotId())){
            //Check if Parking lot already exists
            if(!isParkingLotExisting(parkingLotDetails.getLotId())){

                //Insert Parking lot details
                parkingLotRepository.save(parkingLotDetails);
            } else {
                throw new SmartParkException("Parking Lot ID already exists",
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new SmartParkException("Parking Lot ID exceeded the maximum allowed characters (50)",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void registerVehicle(Vehicle vehicleDetails) {

        //Check if Vehicle already exists
        if (!isVehicleExisting(vehicleDetails.getLicensePlate())){

            //Check if input value format is correct
            if (isValidString(vehicleDetails.getLicensePlate(), Constants.LICENSE_REGEX) &&
                isValidVehicleType(vehicleDetails.getType()) &&
                isValidString(vehicleDetails.getOwnerName(), Constants.OWNER_NAME_REGEX)){

                //Insert Vehicle details
                vehicleRepository.save(vehicleDetails);
                System.out.println("success");
            } else {
                throw new SmartParkException("Input format Incorrect. Please check and try again.",
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new SmartParkException("License plate already exists",
                    HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public List<VehicleDTO> checkInVehicle(String licensePlate, String lotID) {

        if (isParkingAvailable(lotID)){
            if (isVehicleExisting(licensePlate) && isParkingLotExisting(lotID)){

                //Update vehicle - Main method function START
                vehicleRepository.updateVehicleParkingLot(lotID, licensePlate);

                //Check vehicle count
                int parkingLotOccupancyCount = vehicleRepository.findByParkingLotLotId(lotID).size();

                //Update parking Lot Occupancy Count - Main method function END
                parkingLotRepository.updateParkingLotOccupiedSpaces(lotID, parkingLotOccupancyCount);
            } else {
                throw new SmartParkException("Parking Lot and/or Vehicle does not exist",
                        HttpStatus.BAD_REQUEST);
            }
        } else throw new SmartParkException("Parking Capacity Full",
                HttpStatus.BAD_REQUEST);

        return mapToVehicleDto(vehicleRepository.findById(lotID).stream().toList());
    }

    @Override
    public List<VehicleDTO> checkOutVehicle(String licensePlate, String lotID) {

        if (isVehicleExisting(licensePlate) && isParkingLotExisting(lotID)){

            //Update vehicle - Main method function START
            vehicleRepository.updateVehicleParkingLot(Constants.UNASSIGNED_VEHICLE, licensePlate);

            //Check vehicle count
            int parkingLotOccupancyCount = vehicleRepository.findByParkingLotLotId(lotID).size();

            //Update parking Lot Occupancy Count - Main method function END
            parkingLotRepository.updateParkingLotOccupiedSpaces(lotID, parkingLotOccupancyCount);
        } else {
            throw new SmartParkException("Parking Lot and/or Vehicle does not exist",
                    HttpStatus.BAD_REQUEST);
        }

        return mapToVehicleDto(vehicleRepository.findById(lotID).stream().toList());
    }

    @Override
    public List<ParkingLotDTO>  viewParkingStatus(String parkingLotId) {
        List<ParkingLotDTO> parkingLotDTO = new ArrayList<>();

        if (isParkingLotExisting(parkingLotId)){
            parkingLotDTO = mapToParkingDto(parkingLotRepository.findById(parkingLotId)
                    .stream()
                    .toList());
        } else {
            throw new SmartParkException("Parking Lot does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return parkingLotDTO;
    }

    //Functional
    @Override
    public List<VehicleDTO> viewAllVehiclePerLot(String parkingLotId) {
        List<VehicleDTO> parkedVehicles = new ArrayList<>();

        if (isParkingLotExisting(parkingLotId)){
            parkedVehicles = mapToVehicleDto(vehicleRepository.findByParkingLotLotId(parkingLotId));

        } else {
            throw new SmartParkException("Vehicle does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return parkedVehicles;
    }

    @Override
    public boolean isParkingLotExisting(String parkingLotId) {

        //Check if Parking lot is existing
        return parkingLotRepository.findById(parkingLotId).isPresent();
    }

    @Override
    public boolean isVehicleExisting(String licensePlate) {

        //Check if Vehicle is existing
        return vehicleRepository.findById(licensePlate).isPresent();
    }

    @Override
    public boolean isValidVehicleType(String inputVehicleType) {

        //Check if Vehicle type is Car, Motorcyle or Truck
        return Arrays.stream(VehicleType.values())
                .anyMatch(enumType ->
                        enumType.name().equalsIgnoreCase(inputVehicleType));
    }

    @Override
    public boolean isParkingAvailable(String parkingLotId) {

        //Get parking capacity
        int targetLotCapacity =  parkingLotRepository.findById(parkingLotId).get().getCapacity();

        //Get parking occupied spaces
        int targetParkingLotOccupancy = vehicleRepository.countAllByParkingLot_LotId(parkingLotId);

        //Check availability and return result
        return targetLotCapacity > targetParkingLotOccupancy;
    }

    public List<VehicleDTO> mapToVehicleDto(List<Vehicle> vehicleList){

        return vehicleList.stream()
                .map(vehicle -> new VehicleDTO(
                        vehicle.getLicensePlate(),
                        vehicle.getType(),
                        vehicle.getOwnerName(),
                        vehicle.getParkingLot().getLotId()))
                .collect(Collectors.toList());
    }

    public List<ParkingLotDTO> mapToParkingDto(List<ParkingLot> parkingLotList){

        return parkingLotList.stream()
                .map(parkingLot -> new ParkingLotDTO(
                        parkingLot.getLotId(),
                        parkingLot.getLocation(),
                        parkingLot.getCapacity(),
                        parkingLot.getOccupiedSpaces()))
                .collect(Collectors.toList());
    }

}
