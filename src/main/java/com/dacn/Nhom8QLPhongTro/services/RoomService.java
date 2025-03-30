package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.Room;
import com.dacn.Nhom8QLPhongTro.repository.IRoomRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;




@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final IRoomRepository roomRepository;


    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream().filter(p-> !p.getIsRent()).toList();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(()->
                new RuntimeException("Room not found on ::"+id));
    }

    public Room getRoomByName(String name) {
        return roomRepository.findByName(name).orElseThrow(()->
                new RuntimeException("Room not found on ::"+name));
    }

    public Room addRoom(Room room) {
        room.setIsRent(false);
        return roomRepository.save(room);
    }



    public List<Room> searchRoom(String keyword) {
        return roomRepository.search(keyword);
    }

    public Page<Room> getAllPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return roomRepository.findAll(pageable);
    }



    public void updateRoom(@NotNull  Room room) {
        Room existingRoom = roomRepository.findById(room.getId()).orElseThrow(()
                -> new IllegalArgumentException("Room with id: "+room.getId()+"does not exist"));
        existingRoom.setName(room.getName());
        existingRoom.setCategory(room.getCategory());
        existingRoom.setMotelFloorDetail(room.getMotelFloorDetail());
        existingRoom.setIsRent(room.getIsRent());
         roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }


    public int getQuantityRoom(){
        List<Room> rooms = roomRepository.findAll().stream().toList();
        return rooms.size();
    }


}
