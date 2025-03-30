package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.RentRoomDto;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.entity.Room;
import com.dacn.Nhom8QLPhongTro.entity.User;
import com.dacn.Nhom8QLPhongTro.services.RentRoomService;
import com.dacn.Nhom8QLPhongTro.services.RoomService;
import com.dacn.Nhom8QLPhongTro.services.UserService;
import com.dacn.Nhom8QLPhongTro.services.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/rent-rooms"})
public class RentRoomApiController {

    private final RentRoomService rentRoomService;
    private final RoomService roomService;
    private final UserService userService;
    private final VNPAYService vnpayService;


    private RentRoomDto convertToRentRoomDto(RentRoom rentRoom) {
        RentRoomDto rentRoomDto = new RentRoomDto();
        rentRoomDto.setId(rentRoom.getId());
        rentRoomDto.setCheckInDate(rentRoom.getCheckInDate());
        rentRoomDto.setCheckOutDate(rentRoom.getCheckOutDate());
        rentRoomDto.setStatus(rentRoom.getStatus());
        rentRoomDto.setDepositPrice(rentRoom.getDepositPrice());
        rentRoomDto.setQuantityPeople(rentRoom.getQuantityPeople());
        rentRoomDto.setCreationDate(rentRoom.getCreationDate());
        rentRoomDto.setRoomName(rentRoom.getRoom().getName());
        rentRoomDto.setUserName(rentRoom.getUser().getName());
        return rentRoomDto;
    }


    private RentRoom convertToRentRoom(RentRoomDto rentRoomDto) {
        RentRoom rentRoom = new RentRoom();
        rentRoom.setId(rentRoomDto.getId());
        rentRoom.setCheckInDate(rentRoomDto.getCheckInDate());
        rentRoom.setCheckOutDate(rentRoomDto.getCheckOutDate());
        rentRoom.setStatus(rentRoomDto.getStatus());
        rentRoom.setDepositPrice(rentRoomDto.getDepositPrice());
        rentRoom.setQuantityPeople(rentRoomDto.getQuantityPeople());
        rentRoom.setCreationDate(rentRoomDto.getCreationDate());
        rentRoom.setRoom(roomService.getRoomByName(rentRoomDto.getRoomName()));
        rentRoom.setUser(userService.getUserByUsername(rentRoomDto.getUserName()));
        return rentRoom;
    }

    @GetMapping("/all")
    public List<RentRoomDto> getAllRentRooms() {
        return rentRoomService.getAllRentRoom().stream().map(this::convertToRentRoomDto).toList();
    }


    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public List<RentRoomDto> getAllRentRoomsByUser(HttpServletRequest request) {
        User user = userService.getUserLogin(request);
        List<RentRoom> rentRooms = rentRoomService.getAllRentRoomByUser(user);
        if(rentRooms.isEmpty()){
            return null;
        }
        return rentRooms.stream().map(this::convertToRentRoomDto).toList();
    }


    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/rented")
    public List<RentRoomDto> getAllRentedRoomsByUser(HttpServletRequest request) {
        User user = userService.getUserLogin(request);
        List<RentRoom> rentRooms = rentRoomService.getAllRentedRoomByUser(user);
        if(rentRooms.isEmpty()){
            return null;
        }
        return rentRooms.stream().map(this::convertToRentRoomDto).toList();
    }



    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/{id}")
    public ResponseEntity<RentRoomDto>  rentRoom(@PathVariable Long id , @RequestBody RentRoomDto rentRoomDto, HttpServletRequest request) {
            Room room = roomService.getRoomById(id);
            User user = userService.getUserLogin(request);
            rentRoomDto.setUserName(user.getUsername());
            rentRoomDto.setRoomName(room.getName());
            RentRoom rentRoom = rentRoomService.createRentRoom(convertToRentRoom(rentRoomDto));
            if(rentRoom==null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertToRentRoomDto(rentRoom));
    }


    @PostMapping("/create-VNPayUrl/{id}")
    public ResponseEntity<String> createVNPAYUrl(@PathVariable Long id, HttpServletRequest request) {
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        if(rentRoom==null){
            return ResponseEntity.notFound().build();
        }
        RentRoomDto rentRoomDto = convertToRentRoomDto(rentRoom);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api/rent-rooms";
        String VNPayUrl = vnpayService.createOrder(request, rentRoomDto.getDepositPrice().intValue(), String.valueOf(rentRoomDto.getId()), baseUrl);
        return ResponseEntity.ok(VNPayUrl);
    }


    @GetMapping("/vnpay-payment-return")
    public void paymentCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int paymentStatus = vnpayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        RentRoom rentRoom = rentRoomService.getRentRoomById(Long.parseLong(orderInfo)).orElseThrow(()->new IllegalArgumentException("Rent room with id: "+Long.parseLong(orderInfo)+" does not exit."));
        if(paymentStatus == 1){
            rentRoomService.updateStatus(rentRoom,"Đã thanh toán tiền đặt cọc bằng VNPay.");
            response.sendRedirect("http://localhost:3000/payment-success");
        }
        else {
            rentRoomService.updateStatus(rentRoom,"Thanh toán VNPay thất bại!");
            response.sendRedirect("http://localhost:3000/payment-fail");
        }
    }


    @PostMapping("/checkOut/{id}")
    public ResponseEntity<String> checkOutRoom(@PathVariable Long id){
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        if(rentRoom==null)
            return ResponseEntity.notFound().build();
        rentRoomService.checkOutRoom(rentRoom);
        return ResponseEntity.ok("Trả phòng thành công.");
    }


    @GetMapping("/{id}")
    public ResponseEntity<RentRoomDto> getRentRoom(@PathVariable Long id){
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        if(rentRoom==null)
            return ResponseEntity.status(404).build();
        return ResponseEntity.ok(convertToRentRoomDto(rentRoom));
    }



    @PostMapping("/isConfirm/{id}")
    public ResponseEntity<String> isConfirm(@PathVariable Long id, @RequestParam String confirm){
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        if(rentRoom==null)
            return ResponseEntity.status(404).build();
        rentRoomService.updateRentStatusByIsConfirm(rentRoom, Boolean.parseBoolean(confirm));
        return ResponseEntity.ok("Cập nhật thành công.");
    }

    @PostMapping("/unRent/{id}")
    public ResponseEntity<String> unRent(@PathVariable Long id){
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        if(rentRoom==null)
            return ResponseEntity.status(404).build();
        rentRoomService.unRent(rentRoom);
        return ResponseEntity.ok("Hủy phòng thành công.");
    }


}
