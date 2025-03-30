package com.dacn.Nhom8QLPhongTro.repository;

import com.dacn.Nhom8QLPhongTro.entity.ImageRoom;
import com.dacn.Nhom8QLPhongTro.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IImageRoomRepository extends JpaRepository<ImageRoom, Long> {

    List<ImageRoom> findImageRoomsByRoom(Room room);

}
