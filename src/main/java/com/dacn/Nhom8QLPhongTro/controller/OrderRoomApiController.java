package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.OrderRoomDto;
import com.dacn.Nhom8QLPhongTro.entity.OrderRoom;
import com.dacn.Nhom8QLPhongTro.entity.User;
import com.dacn.Nhom8QLPhongTro.services.OrderRoomService;
import com.dacn.Nhom8QLPhongTro.services.RentRoomService;
import com.dacn.Nhom8QLPhongTro.services.UserService;
import com.dacn.Nhom8QLPhongTro.services.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;



@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/order-rooms"})
public class OrderRoomApiController {


    private final OrderRoomService orderRoomService;
    private final RentRoomService rentRoomService;
    private final VNPAYService vnpayService;
    private final UserService userService;

    private OrderRoomDto convertToOrderRoomDto(OrderRoom orderRoom) {
        OrderRoomDto orderRoomDto = new OrderRoomDto();
        orderRoomDto.setId(orderRoom.getId());
        orderRoomDto.setName(orderRoom.getName());
        orderRoomDto.setRoomPrice(orderRoom.getRoomPrice());
        orderRoomDto.setWaterPrice(orderRoom.getWaterPrice());
        orderRoomDto.setElectricPrice(orderRoom.getElectricPrice());
        orderRoomDto.setOtherServicePrice(orderRoom.getOtherServicePrice());
        orderRoomDto.setTotalPrice(orderRoom.getTotalPrice());
        orderRoomDto.setPaymentStatus(orderRoom.getPaymentStatus());
        orderRoomDto.setCreateDate(orderRoom.getCreateDate());
        orderRoomDto.setIdRentRoom(orderRoom.getRentRoom().getId());
        orderRoomDto.setNameCustomer(orderRoom.getRentRoom().getUser().getName());
        orderRoomDto.setPhone(orderRoom.getRentRoom().getUser().getPhoneNumber());
        orderRoomDto.setRoomName(orderRoom.getRentRoom().getRoom().getName());
        return orderRoomDto;
    }


    private OrderRoom convertToOrderRoom(OrderRoomDto orderRoomDto) {
        OrderRoom orderRoom = new OrderRoom();
        orderRoom.setId(orderRoomDto.getId());
        orderRoom.setName(orderRoomDto.getName());
        orderRoom.setRoomPrice(orderRoomDto.getRoomPrice());
        orderRoom.setWaterPrice(orderRoomDto.getWaterPrice());
        orderRoom.setElectricPrice(orderRoomDto.getElectricPrice());
        orderRoom.setOtherServicePrice(orderRoomDto.getOtherServicePrice());
        orderRoom.setTotalPrice(orderRoomDto.getTotalPrice());
        orderRoom.setPaymentStatus(orderRoomDto.getPaymentStatus());
        orderRoom.setCreateDate(orderRoomDto.getCreateDate());
        orderRoom.setRentRoom(rentRoomService.getRentRoomById(orderRoomDto.getIdRentRoom()).orElse(null));
        return orderRoom;
    }

    @PostMapping
    public ResponseEntity<OrderRoomDto>  createOrderRoom(@RequestBody OrderRoomDto orderRoomDto) {
        return ResponseEntity.ok(convertToOrderRoomDto(orderRoomService.createOrderRoom(convertToOrderRoom(orderRoomDto))));
    }


    @PostMapping("/create-VNPayUrl/{id}")
    public ResponseEntity<String> createVNPAYUrl(@PathVariable Long id, HttpServletRequest request) {
        OrderRoom orderRoom = orderRoomService.findOrderRoomById(id).orElse(null);
        if(orderRoom == null){
            return ResponseEntity.notFound().build();
        }
        OrderRoomDto orderRoomDto = convertToOrderRoomDto(orderRoom);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api/order-rooms";
        String VNPayUrl = vnpayService.createOrder(request, orderRoomDto.getTotalPrice().intValue(), String.valueOf(orderRoomDto.getId()), baseUrl);
        return ResponseEntity.ok(VNPayUrl);
    }


    @GetMapping("/vnpay-payment-return")
    public void paymentCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int paymentStatus = vnpayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        OrderRoom orderRoom = orderRoomService.findOrderRoomById(Long.parseLong(orderInfo)).orElseThrow(()->new IllegalArgumentException("Order room with id: "+Long.parseLong(orderInfo)+" does not exit."));
        if(paymentStatus == 1){
            orderRoomService.updatePaymentStatus(orderRoom,"Đã thanh toán bằng VNPay");
            response.sendRedirect("http://localhost:3000/payment-success");
        }
        else {
            orderRoomService.updatePaymentStatus(orderRoom,"Thanh toán VNPay thất bại!");
            response.sendRedirect("http://localhost:3000/payment-fail");
        }
    }


    @GetMapping("/user-orderRooms")
    public List<OrderRoomDto> getAllOrderRoomByUser(HttpServletRequest request) {
        User user = userService.getUserLogin(request);
        if(user==null)
            return null;
        List<OrderRoom> orderRooms = orderRoomService.getAllOrderRoomByUser(user);
        if(orderRooms.isEmpty())
            return null;
        return orderRooms.stream().map(this::convertToOrderRoomDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRoomDto> getRentRoomDto(@PathVariable Long id){
        OrderRoom orderRoom = orderRoomService.getOderRoomById(id);
        if(orderRoom==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertToOrderRoomDto(orderRoom));
    }


    @GetMapping
    public  List<OrderRoomDto> getAllOrderRoom(){
        return orderRoomService.getAllOrderRoom().stream().map(this::convertToOrderRoomDto).toList();
    }

}
