package com.example.smartpark.repository;

import com.example.smartpark.entity.ParkingLot;
import com.example.smartpark.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByParkingLotLotId(String parkingLotId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vehicle v SET v.parkingLot.lotId = :lotId " +
            "WHERE v.licensePlate = :licensePlate")
    void updateVehicleParkingLot(@Param("lotId") String lotId, @Param("licensePlate") String licensePlate);


    int countAllByParkingLot(ParkingLot parkingLot);

    int countAllByParkingLot_LotId(String parkingLotLotId);
}
