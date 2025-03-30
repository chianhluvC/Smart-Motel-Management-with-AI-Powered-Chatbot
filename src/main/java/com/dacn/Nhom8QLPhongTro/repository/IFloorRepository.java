package com.dacn.Nhom8QLPhongTro.repository;


import com.dacn.Nhom8QLPhongTro.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFloorRepository extends JpaRepository<Floor, Long> {

    Optional<Floor> findByName(String name);
}
