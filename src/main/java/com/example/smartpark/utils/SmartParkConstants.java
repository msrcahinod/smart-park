package com.example.smartpark.utils;

/*
 *   SMARTPARK CONSTANTS
 */
public class SmartParkConstants {

    /*DEFAULT VALUES*/
    public static final String UNASSIGNED_VEHICLE = "00_UNASSIGNED";

    public static final String EMPTY_STRING = "";

    public static final int ZERO = 0;

    /*VALID INPUT FORMAT*/
    public static final String LICENSE_REGEX = "^[a-zA-Z0-9-]+$";

    public static final String OWNER_NAME_REGEX = "^[a-zA-Z0 ]+$";

    public static final int MAXIMUM_PARKING_LOT_ID_CHARACTERS = 50;


    /*ERROR MESSAGES*/

    public static final String ERR_MSG_ALREADY_REGISTERED_PARKING_LOT = "Parking Lot ID already registered";

    public static final String ERR_MSG_ID_EXCEEDED_MAX_CHARACTERS = "Parking Lot ID exceeded the maximum allowed characters (50).";

    public static final String ERR_MSG_INCORRECT_FORMAT = "Input format Incorrect.";

    public static final String ERR_MSG_ALREADY_REGISTERED_VEHICLE = "Vehicle plate already registered";

    public static final String ERR_MSG_ALREADY_PARKED_VEHICLE = "Vehicle is already parked";

    public static final String ERR_MSG_VEHICLE_NOT_PARKED = "Vehicle is not parked yet";

    public static final String ERR_MSG_FULL_PARKING = "Parking Capacity Full";

    public static final String PARKING_LOT_OR_VEHICLE_NOT_REGISTERED = "Parking lot and/or Vehicle not registered.";

    public static final String ERR_MSG_UNREGISTERED_VEHICLE = "Vehicle not registered.";

    public static final String ERR_MSG_UNREGISTERED_PARKING_LOT = "Parking lot is not registered.";

    public static final String ERR_MSG_NO_PARKED_VEHICLES = "Parking lot has no parked vehicles";



}
