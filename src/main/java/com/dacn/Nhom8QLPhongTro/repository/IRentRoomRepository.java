package com.dacn.Nhom8QLPhongTro.repository;

import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRentRoomRepository extends JpaRepository<RentRoom, Long> {
    List<RentRoom> findAllByUser(User user);
}
