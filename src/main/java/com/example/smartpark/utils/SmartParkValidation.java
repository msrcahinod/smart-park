package com.example.smartpark.utils;

/*
 *   SMARTPARK VALIDATION
 */
public interface SmartParkValidation {

    //String Validator
    boolean isValidString(String licensePlate, String format);

    //Parking lot registration validator
    boolean isParkingLotRegistered(String parkingLotId);

    //Vehicle registration validator
    boolean isVehicleRegistered(String licensePlate);

    //Vehicle type validator
    boolean isValidVehicleType(String vehicleType);

    //Parking lot ID validator
    boolean isValidParkingLotId(String parkingLotId);

    //parking lot availability validator
    boolean isParkingAvailable(String parkingLotId);

    //Vehicle parking validator
    boolean isVehicleNotParked(String licensePlate);
}
