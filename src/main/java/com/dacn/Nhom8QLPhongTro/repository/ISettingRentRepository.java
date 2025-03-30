package com.dacn.Nhom8QLPhongTro.repository;

import com.dacn.Nhom8QLPhongTro.entity.SettingRent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISettingRentRepository extends JpaRepository<SettingRent, Long> {

    Optional<SettingRent>  findByName(String name);
}
