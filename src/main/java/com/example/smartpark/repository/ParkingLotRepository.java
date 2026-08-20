package com.example.smartpark.repository;

import com.example.smartpark.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 *   PARKING LOT REPOSITORY
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ParkingLot p SET p.occupiedSpaces = :occupancyCount " +
            "WHERE p.lotId = :lotId")
    void updateParkingLotOccupiedSpaces(@Param("lotId") String lotId, @Param("occupancyCount") int occupancyCount);


}
