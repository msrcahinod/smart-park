package com.example.smartpark.service;

import com.example.smartpark.dto.ParkingLotDTO;
import com.example.smartpark.entity.ParkingLot;
import com.example.smartpark.entity.Vehicle;
import com.example.smartpark.dto.VehicleDTO;
import com.example.smartpark.exception.SmartParkException;
import com.example.smartpark.repository.ParkingLotRepository;
import com.example.smartpark.repository.VehicleRepository;
import com.example.smartpark.utils.SmartParkConstants;
import com.example.smartpark.utils.MockData;
import com.example.smartpark.utils.SmartParkValidation;
import com.example.smartpark.utils.VehicleType;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
* SMARTPARK SERVICE IMPLEMENTATION
* implements SmartParkService, SmartParkValidation
* */
@Service
public class SmartParkServiceImpl implements SmartParkService, SmartParkValidation {

    //Parking lot repository
    private final ParkingLotRepository parkingLotRepository;

    //Vehicle repository
    private final VehicleRepository vehicleRepository;


    public SmartParkServiceImpl(ParkingLotRepository parkingLotRepository,
                                VehicleRepository vehicleRepository,
                                MockData mockData) {

        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;

        //Fill Database with Dummy Data
        mockData.fillParkingLot();

    }

    @Override
    public ParkingLotDTO registerParkingLot(ParkingLotDTO parkingLotDetails) {

        //DTO To Entity
        ParkingLot registerParkingLot = new ParkingLot();
        registerParkingLot.setLotId(parkingLotDetails.getLotId());
        registerParkingLot.setLocation(parkingLotDetails.getLocation());
        registerParkingLot.setCapacity(parkingLotDetails.getCapacity());
        registerParkingLot.setOccupiedSpaces(SmartParkConstants.ZERO);

        //Check if Parking Lot id is valid
        if (isValidParkingLotId(registerParkingLot.getLotId())){

            //Check if Parking lot already registered
            if(!isParkingLotRegistered(registerParkingLot.getLotId())){

                //Insert Parking lot details
                parkingLotRepository.save(registerParkingLot);
            } else {
                //Error : Parking Lot ID already registered.
                throw new SmartParkException(SmartParkConstants.ERR_MSG_ALREADY_REGISTERED_PARKING_LOT, HttpStatus.BAD_REQUEST);
            }
        } else {
            //Error : Parking Lot ID exceeded the maximum allowed characters (50).
            throw new SmartParkException(SmartParkConstants.ERR_MSG_ID_EXCEEDED_MAX_CHARACTERS, HttpStatus.BAD_REQUEST);
        }

        //Search the inserted parking lot
        ParkingLot insertedParkingLot = parkingLotRepository.findById(registerParkingLot.getLotId()).get();

        //return Entity to DTO for response
        return new ParkingLotDTO(
                insertedParkingLot.getLotId(),
                insertedParkingLot.getLocation(),
                insertedParkingLot.getCapacity(),
                insertedParkingLot.getOccupiedSpaces());
    }

    @Transactional
    @Override
    public VehicleDTO registerVehicle(VehicleDTO vehicleDetails) {

        //DTO To Entity
        Vehicle registerVehicle = new Vehicle();
        registerVehicle.setLicensePlate(vehicleDetails.getLicensePlate());
        registerVehicle.setType(vehicleDetails.getType());
        registerVehicle.setOwnerName(vehicleDetails.getOwnerName());
        registerVehicle.setParkingLot(parkingLotRepository.findById(SmartParkConstants.UNASSIGNED_VEHICLE).get());

        //Check if Vehicle already registered
        if (!isVehicleRegistered(vehicleDetails.getLicensePlate())){

            //Check if input value format is correct
            if (isValidString(vehicleDetails.getLicensePlate(), SmartParkConstants.LICENSE_REGEX) &&
                isValidVehicleType(vehicleDetails.getType()) &&
                isValidString(vehicleDetails.getOwnerName(), SmartParkConstants.OWNER_NAME_REGEX)){

                //Insert Vehicle details
                vehicleRepository.save(registerVehicle);

                //Assign default value to vehicle lot ID;
                vehicleRepository.updateVehicleParkingLot(SmartParkConstants.UNASSIGNED_VEHICLE,
                        registerVehicle.getLicensePlate());

                int unAssignedParkingCount = vehicleRepository.countAllByParkingLot_LotId(SmartParkConstants.UNASSIGNED_VEHICLE);

                parkingLotRepository.updateParkingLotOccupiedSpaces(SmartParkConstants.UNASSIGNED_VEHICLE, unAssignedParkingCount);

            } else
                //Error : Input format Incorrect.
                throw new SmartParkException(SmartParkConstants.ERR_MSG_INCORRECT_FORMAT, HttpStatus.BAD_REQUEST);

        } else
            //Error : Vehicle plate already registered.
            throw new SmartParkException(SmartParkConstants.ERR_MSG_ALREADY_REGISTERED_VEHICLE, HttpStatus.BAD_REQUEST);

        //Search the inserted Vehicle
        Vehicle insertedVehicle = vehicleRepository.findById(registerVehicle.getLicensePlate()).get();

        //return Entity to DTO for response
        return new VehicleDTO(
                insertedVehicle.getLicensePlate(),
                insertedVehicle.getType(),
                insertedVehicle.getOwnerName(),
                insertedVehicle.getParkingLot().getLotId());
    }

    @Transactional
    @Override
    public List<VehicleDTO> checkInVehicle(String licensePlate, String parkingLotId) {

        //Checf if Vehicle and Parking lot are both registered
        if (isVehicleRegistered(licensePlate) && isParkingLotRegistered(parkingLotId)) {

            //Check if parking lot has space
            if (isParkingAvailable(parkingLotId)) {

                //Check if vehicle is not parked
                if (isVehicleNotParked(licensePlate)) {

                    //Update vehicle lot ID - Main method function START
                    vehicleRepository.updateVehicleParkingLot(parkingLotId, licensePlate);

                    //Get updated vehicle count
                    int parkingLotOccupancyCount = vehicleRepository.countAllByParkingLot_LotId(parkingLotId);

                    //Update parking Lot Occupancy Count
                    parkingLotRepository.updateParkingLotOccupiedSpaces(parkingLotId, parkingLotOccupancyCount);

                    //Get updated unassigned vehicle count
                    int unAssignedParkingCount = vehicleRepository.countAllByParkingLot_LotId(SmartParkConstants.UNASSIGNED_VEHICLE);

                    //Update unassigned Lot Occupancy Count - Main method function END
                    parkingLotRepository.updateParkingLotOccupiedSpaces(SmartParkConstants.UNASSIGNED_VEHICLE, unAssignedParkingCount);
                } else
                    //Error : Vehicle is already parked
                    throw new SmartParkException(SmartParkConstants.ERR_MSG_ALREADY_PARKED_VEHICLE, HttpStatus.BAD_REQUEST);
            } else
                //Error : Parking Capacity Full
                throw new SmartParkException(SmartParkConstants.ERR_MSG_FULL_PARKING, HttpStatus.BAD_REQUEST);
        } else
            //Error : Parking lot and/or Vehicle not registered.
            throw new SmartParkException(SmartParkConstants.PARKING_LOT_OR_VEHICLE_NOT_REGISTERED, HttpStatus.NOT_FOUND);

        //Return Vehicle DTO response
        return mapToVehicleDto(vehicleRepository.findById(licensePlate).stream().toList());
    }

    @Transactional
    @Override
    public List<VehicleDTO> checkOutVehicle(String licensePlate) {

        //Initialize parkingLotID
        String parkingLotId = SmartParkConstants.EMPTY_STRING;

        //Check if Vehicle is registered
        if (isVehicleRegistered(licensePlate)){

            //Check if vehicle is parked
            if (!isVehicleNotParked(licensePlate)) {

                //Get parking lot id based on provided license plate
                parkingLotId = vehicleRepository.findById(licensePlate).get().getParkingLot().getLotId();

                //Update vehicle lot id to unassigned - Main method function START
                vehicleRepository.updateVehicleParkingLot(SmartParkConstants.UNASSIGNED_VEHICLE, licensePlate);

                //Get updated parking lot occupancy count;
                int updatedParkingLotOccupancyCount = vehicleRepository.countAllByParkingLot_LotId(parkingLotId);

                //Update parking Lot Occupancy Count
                parkingLotRepository.updateParkingLotOccupiedSpaces(parkingLotId, updatedParkingLotOccupancyCount);

                //Get updated unassigned vehicle count
                int unAssignedParkingCount = vehicleRepository.countAllByParkingLot_LotId(SmartParkConstants.UNASSIGNED_VEHICLE);

                //Update unassigned Lot Occupancy Count - Main method function END
                parkingLotRepository.updateParkingLotOccupiedSpaces(SmartParkConstants.UNASSIGNED_VEHICLE, unAssignedParkingCount);
            } else
                //Error : Vehicle is not parked yet
                throw new SmartParkException(SmartParkConstants.ERR_MSG_VEHICLE_NOT_PARKED, HttpStatus.BAD_REQUEST);
        } else
            //Error : Vehicle not registered.
            throw new SmartParkException(SmartParkConstants.ERR_MSG_UNREGISTERED_VEHICLE, HttpStatus.NOT_FOUND);

        //Return Vehicle DTO response
        return mapToVehicleDto(vehicleRepository.findById(licensePlate).stream().toList());
    }

    @Override
    public List<ParkingLotDTO> viewParkingStatus(String parkingLotId) {

        //Initialize Parking lot list
        List<ParkingLotDTO> parkingLotDTO;

        //Check if parking lot is registered
        if (isParkingLotRegistered(parkingLotId)){

            //Get Parking lot info
            parkingLotDTO = mapToParkingDto(parkingLotRepository.findById(parkingLotId));

        } else
            //Error : Parking lot is not registered.
            throw new SmartParkException(SmartParkConstants.ERR_MSG_UNREGISTERED_PARKING_LOT, HttpStatus.NOT_FOUND);

        //Return Parking lot DTO response
        return parkingLotDTO;
    }

    //Functional
    @Override
    public List<VehicleDTO> viewAllVehiclePerLot(String parkingLotId) {
        List<VehicleDTO> parkedVehicles;

        if (isParkingLotRegistered(parkingLotId)){
            parkedVehicles = mapToVehicleDto(vehicleRepository.findByParkingLotLotId(parkingLotId));

            if (parkedVehicles.isEmpty())
                throw new SmartParkException(SmartParkConstants.ERR_MSG_NO_PARKED_VEHICLES, HttpStatus.NOT_FOUND);
        } else
            //Error : Parking lot is not registered.
            throw new SmartParkException(SmartParkConstants.ERR_MSG_UNREGISTERED_PARKING_LOT, HttpStatus.NOT_FOUND);

        return parkedVehicles;
    }

    //String Validator implementation
    @Override
    public boolean isValidString(String licensePlate, String format) {

        //Check String format
        return licensePlate.matches(format);
    }

    //Parking lot registration validator implementation
    @Override
    public boolean isParkingLotRegistered(String parkingLotId) {

        //Check if Parking lot is registered
        return parkingLotRepository.existsById(parkingLotId);
    }

    //Vehicle registration validator implementation
    @Override
    public boolean isVehicleRegistered(String licensePlate) {

        //Check if Vehicle is registered
        return vehicleRepository.existsById(licensePlate);
    }

    //Vehicle type validator implementation
    @Override
    public boolean isValidVehicleType(String inputVehicleType) {

        //Check if Vehicle type is Car, Motorcycle or Truck
        return Arrays.stream(VehicleType.values())
                .anyMatch(enumType ->
                        enumType.name().equalsIgnoreCase(inputVehicleType));
    }

    //Parking lot ID validator implementation
    @Override
    public boolean isValidParkingLotId(String parkingLotId) {

        //Check parking capacity
        return parkingLotId.length() <= SmartParkConstants.MAXIMUM_PARKING_LOT_ID_CHARACTERS;
    }

    //parking lot availability validator implementation
    @Override
    public boolean isParkingAvailable(String parkingLotId) {

        //Get parking capacity
        int targetLotCapacity =  parkingLotRepository.findById(parkingLotId).get().getCapacity();

        //Get parking occupied spaces
        int targetParkingLotOccupancy = vehicleRepository.countAllByParkingLot_LotId(parkingLotId);

        //Check availability and return result
        return targetLotCapacity > targetParkingLotOccupancy;
    }

    //Vehicle parking validator implmentation
    @Override
    public boolean isVehicleNotParked(String licensePlate) {

        //Check vehicle parking status
        return vehicleRepository.existsByLicensePlateAndParkingLotLotIdIsContaining(licensePlate,
                SmartParkConstants.UNASSIGNED_VEHICLE);
    }

    //Vehicle Mapper method
    public List<VehicleDTO> mapToVehicleDto(@NonNull List<Vehicle> vehicleList){

        //Map Vehicle list to VehicleDTO list
        return vehicleList.stream()
                .map(vehicle -> new VehicleDTO(
                        vehicle.getLicensePlate(),
                        vehicle.getType(),
                        vehicle.getOwnerName(),
                        vehicle.getParkingLot().getLotId()))
                .collect(Collectors.toList());
    }

    //Parking lot Mapper method
    public List<ParkingLotDTO> mapToParkingDto(@NonNull Optional<ParkingLot> parkingLotList){

        //Map ParkingLot list to ParkingLotDTO list
        return parkingLotList.stream()
                .map(parkingLot -> new ParkingLotDTO(
                        parkingLot.getLotId(),
                        parkingLot.getLocation(),
                        parkingLot.getCapacity(),
                        parkingLot.getOccupiedSpaces()))
                .collect(Collectors.toList());
    }
}
