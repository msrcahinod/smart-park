package com.example.smartpark.repository;

import com.example.smartpark.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *   VEHICLE REPOSITORY
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    //Find all vehicles by Lot ID
    List<Vehicle> findByParkingLotLotId(String parkingLotId);

    //Update target vehicle lot ID
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vehicle v SET v.parkingLot.lotId = :lotId " +
            "WHERE v.licensePlate = :licensePlate")
    void updateVehicleParkingLot(@Param("lotId") String lotId, @Param("licensePlate") String licensePlate);

    //Count all vehicles in a parking lot
    int countAllByParkingLot_LotId(String parkingLotLotId);

    //checking if vehicle is existing and is not parked
    boolean existsByLicensePlateAndParkingLotLotIdIsContaining(String licensePlate, String unassignedVehicle);
}
