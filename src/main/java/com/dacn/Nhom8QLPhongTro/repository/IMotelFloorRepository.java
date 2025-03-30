package com.dacn.Nhom8QLPhongTro.repository;


import com.dacn.Nhom8QLPhongTro.entity.MotelFloorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IMotelFloorRepository extends JpaRepository<MotelFloorDetail, Long > {

    @Query("select p from MotelFloorDetail p where  p.floor.name = ?1 and  p.motel.name = ?2 ")
    MotelFloorDetail findByFloorNameAndMotelName(String floor_Name, String motel_Name);


}
