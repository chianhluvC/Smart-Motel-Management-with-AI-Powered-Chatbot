package com.dacn.Nhom8QLPhongTro.repository;


import com.dacn.Nhom8QLPhongTro.entity.Room;
import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@NonNullApi
@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {

    @Query("select p from Room  p where p.isRent = false and concat(p.name, p.category.name, p.motelFloorDetail.floor.name, " +
            "p.motelFloorDetail.motel.name, p.motelFloorDetail.motel.address) like %?1%")
    List<Room> search(String name);

    @NonNull
    @Query("select p from Room p where p.isRent = false ")
    Page<Room> findAll(Pageable pageable);

    Optional<Room> findByName(String name);

}
