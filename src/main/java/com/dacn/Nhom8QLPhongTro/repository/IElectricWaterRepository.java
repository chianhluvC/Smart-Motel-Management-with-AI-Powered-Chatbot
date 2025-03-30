package com.dacn.Nhom8QLPhongTro.repository;


import com.dacn.Nhom8QLPhongTro.entity.ElectricWater;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IElectricWaterRepository extends JpaRepository<ElectricWater, Long> {

    List<ElectricWater> findElectricWaterByRentRoom(RentRoom rentRoom);

}
