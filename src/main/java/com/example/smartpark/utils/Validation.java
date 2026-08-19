package com.example.smartpark.utils;

import com.example.smartpark.entity.Vehicle;

public interface Validation {

    default boolean isValidString(String licensePlate, String format) {
        return licensePlate.matches(format);
    }

    boolean isParkingLotExisting(String parkingLotId);

    boolean isVehicleExisting(String licensePlate);

    boolean isValidVehicleType(String vehicleType);

    default boolean isValidParkingLotId(String parkingLotId){
        return parkingLotId.length() <= Constants.MAXIMUM_PARKING_LOT_ID_CHARACTERS;
    }

    boolean isParkingAvailable(String parkingLotId);

}
