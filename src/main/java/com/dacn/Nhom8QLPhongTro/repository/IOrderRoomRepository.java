package com.dacn.Nhom8QLPhongTro.repository;

import com.dacn.Nhom8QLPhongTro.entity.OrderRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface IOrderRoomRepository extends JpaRepository<OrderRoom, Long> {

}
