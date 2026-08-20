package com.example.smartpark.utils;

import com.example.smartpark.entity.ParkingLot;
import com.example.smartpark.entity.Vehicle;
import com.example.smartpark.repository.ParkingLotRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 *   MOCK DATA
 */
@Component
public class MockData {

    private final ParkingLotRepository parkingLotRepository;

    public MockData(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public void fillParkingLot(){

        ParkingLot unassignedParkingLot = createLot(SmartParkConstants.UNASSIGNED_VEHICLE, "NONE", 1000, 0);
        ParkingLot lot1 = createLot("LOT1", "Downtown Garage", 50, 3);
        ParkingLot lot2 = createLot("LOT2", "Airport North Lot", 100, 2);
        ParkingLot lot3 = createLot("LOT3", "Mall South Lot", 2, 2);
        ParkingLot lot4 = createLot("LOT4", "Central Station Plaza", 40, 3);
        ParkingLot lot5 = createLot("LOT5", "Harbor West Terminal", 25, 0);

        addVehicle(lot1, "ABC-1234", "Car", "Alice Smith");
        addVehicle(lot1, "XYZ-9876", "Motorcycle", "Bob Jones");
        addVehicle(lot1, "LMN-4567", "Truck", "Charlie Brown");

        addVehicle(lot2, "DEF-5678", "Car", "Diana Prince");
        addVehicle(lot2, "GHI-9012", "Car", "Ethan Hunt");

        addVehicle(lot3, "JKL-3456", "Car", "Fiona Gallagher");
        addVehicle(lot3, "NOP-7890", "Truck", "George Clark");

        addVehicle(lot4, "QRS-1122", "Truck", "Hannah Abbott");
        addVehicle(lot4, "TUV-3344", "Motorcycle", "Ian Malcolm");
        addVehicle(lot4, "WXY-5566", "Motorcycle", "Julia Roberts");

        parkingLotRepository.saveAll(List.of(unassignedParkingLot, lot1, lot2, lot3, lot4, lot5));
    }

    private ParkingLot createLot(String id, String location, int capacity, int occupiedSpaces) {
        ParkingLot lot = new ParkingLot();
        lot.setLotId(id);
        lot.setLocation(location);
        lot.setCapacity(capacity);
        lot.setOccupiedSpaces(occupiedSpaces);
        return lot;
    }

    private void addVehicle(ParkingLot lot, String licensePlate, String type, String ownerName) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        vehicle.setType(type);
        vehicle.setOwnerName(ownerName);
        vehicle.setParkingLot(lot);

        lot.getVehicles().add(vehicle);
    }


}
