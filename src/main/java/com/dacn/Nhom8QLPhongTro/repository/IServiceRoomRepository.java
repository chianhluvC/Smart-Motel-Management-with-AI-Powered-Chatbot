package com.dacn.Nhom8QLPhongTro.repository;

import com.dacn.Nhom8QLPhongTro.entity.ServiceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IServiceRoomRepository extends JpaRepository<ServiceRoom, Long> {

    Optional<ServiceRoom> findByName(String name);
}
