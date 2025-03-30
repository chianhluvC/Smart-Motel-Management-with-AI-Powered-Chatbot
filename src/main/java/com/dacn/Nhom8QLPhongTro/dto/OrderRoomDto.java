package com.dacn.Nhom8QLPhongTro.dto;



import lombok.Data;
import java.time.LocalDate;



@Data
public class OrderRoomDto {

    private Long id;

    private String name;

    private Double roomPrice;

    private Double waterPrice;

    private Double electricPrice;

    private Double otherServicePrice;

    private Double totalPrice;

    private String paymentStatus;

    private LocalDate createDate;

    private Long idRentRoom;

    private String nameCustomer;

    private String phone;

    private String roomName;

}
