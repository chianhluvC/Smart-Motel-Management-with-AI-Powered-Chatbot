package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.StatisticalDto;
import com.dacn.Nhom8QLPhongTro.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistical")
public class StatisticalApiController {


    private final OrderRoomService orderRoomService;
    private final RoomService roomService;
    private final FloorService floorService;
    private final MotelService motelService;
    private final RentRoomService rentRoomService;



    @GetMapping
    public ResponseEntity<StatisticalDto> getStatistical(){
        StatisticalDto statisticalDto = new StatisticalDto();
        statisticalDto.setCollectMoney(orderRoomService.getAllTotalPayment());
        statisticalDto.setQuantityRoom(roomService.getQuantityRoom());
        statisticalDto.setQuantityFloor(floorService.getQuantityFloor());
        statisticalDto.setQuantityMotel(motelService.getQuantityMotel());
        statisticalDto.setQuantityRented(rentRoomService.getQuantityRented());
        statisticalDto.setQuantityRent(rentRoomService.getQuantityRent());
        return ResponseEntity.ok(statisticalDto);
    }



}
