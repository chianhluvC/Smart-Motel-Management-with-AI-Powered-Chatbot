package com.dacn.Nhom8QLPhongTro.repository;



import com.dacn.Nhom8QLPhongTro.entity.Motel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMotelRepository extends JpaRepository<Motel, Long> {
    Motel findByName(String name);
}
