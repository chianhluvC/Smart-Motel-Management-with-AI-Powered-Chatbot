package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.*;
import com.dacn.Nhom8QLPhongTro.repository.IRentRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class RentRoomService {

    private final IRentRoomRepository rentRoomRepository;
    private final SettingRentService settingRentService;
    private final RoomService roomService;
    private final OtherServiceService otherServiceService;
    private final ElectricWaterService electricWaterService;


    public RentRoom createRentRoom(RentRoom rentRoom) {
        Room room = roomService.getRoomById(rentRoom.getRoom().getId());
        if(room!=null&&!room.getIsRent()){
            rentRoom.setDepositPrice(room.getCategory().getPrice());
            rentRoom.setQuantityPeople(rentRoom.getQuantityPeople());
            rentRoom.setCreationDate(LocalDate.now());
            rentRoom.setIsCheckOut(false);
            if(settingRentService.isAutoConfirm()){
                rentRoom.setStatus("Đã duyệt cần người dùng đặt cọc");
            }
            else {
                rentRoom.setStatus("Chờ phê duyệt");
            }
            room.setIsRent(true);
            roomService.updateRoom(room);
            return rentRoomRepository.save(rentRoom);
        }
        return null;
    }



    public void updateRentStatusByIsConfirm(RentRoom rentRoom, Boolean isConfirm) {
        if(isConfirm){
            rentRoom.setStatus("Đã duyệt cần người dùng đặt cọc");
        }
        else {
            rentRoom.setStatus("Không được phê duyệt!");
            Room room = roomService.getRoomById(rentRoom.getRoom().getId());
            room.setIsRent(false);
            roomService.updateRoom(room);
        }
        rentRoomRepository.save(rentRoom);
    }

    public void unRent(RentRoom rentRoom) {
        Room room = roomService.getRoomById(rentRoom.getRoom().getId());
        room.setIsRent(false);
        roomService.updateRoom(room);
        rentRoom.setStatus("Hết thời hạn thanh toán");
        rentRoomRepository.save(rentRoom);
    }


    public Optional<RentRoom>  getRentRoomById(Long rentRoomId) {
        return rentRoomRepository.findById(rentRoomId);
    }

    public void updateStatus(RentRoom rentRoom, String status) {
        Room room = roomService.getRoomById(rentRoom.getRoom().getId());
        room.setIsRent(true);
        roomService.updateRoom(room);
        rentRoom.setStatus(status);
        rentRoomRepository.save(rentRoom);
    }

    public List<RentRoom> getAllRentRoomByUser(User user){
        return rentRoomRepository.findAllByUser(user);
    }

    public List<RentRoom> getAllRentedRoomByUser(User user){
        return rentRoomRepository.findAllByUser(user).stream().filter(p->p.getStatus().equals("Đã thanh toán tiền đặt cọc bằng VNPay.")).toList();
    }


    public List<RentRoom> getAllRentRoom(){
        return rentRoomRepository.findAll();
    }


    public void checkOutRoom(RentRoom rentRoom){
        rentRoom.setIsCheckOut(true);
        rentRoom.setCheckOutDate(LocalDate.now());
        rentRoom.setStatus("Đã trả phòng.");
        Room room = roomService.getRoomById(rentRoom.getRoom().getId());
        room.setIsRent(false);
        List<OtherService> otherServiceList = otherServiceService.getOtherServiceByRentRoom(rentRoom);
        if(!otherServiceList.isEmpty())
            otherServiceService.updateAllOtherServiceStatusByList(otherServiceList, false);
        List<ElectricWater> electricWaterList = electricWaterService.getAllElectricWatersByRentRoom(rentRoom);
        if(!electricWaterList.isEmpty())
            electricWaterService.updateAllElectricWaterStatusByList(electricWaterList, false);
        roomService.updateRoom(room);
        rentRoomRepository.save(rentRoom);
    }


    public int getQuantityRented(){
        return  rentRoomRepository.findAll().stream().filter(p->p.getStatus().equals("Đã trả phòng.")).toList().size();
    }


    public int getQuantityRent(){
        return rentRoomRepository.findAll().size();
    }



}
