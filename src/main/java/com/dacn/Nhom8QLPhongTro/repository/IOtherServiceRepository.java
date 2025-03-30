package com.dacn.Nhom8QLPhongTro.repository;


import com.dacn.Nhom8QLPhongTro.entity.OtherService;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOtherServiceRepository extends JpaRepository<OtherService, Long> {
    List<OtherService> findOtherServiceByRentRoom(RentRoom rentRoom);

}
