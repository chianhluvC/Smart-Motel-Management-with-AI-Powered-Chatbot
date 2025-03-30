package com.dacn.Nhom8QLPhongTro.dto;


import lombok.Data;
import java.time.LocalDate;


@Data
public class RentRoomDto {


    private Long  id;

    private String status;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Double depositPrice;

    private int quantityPeople;

    private LocalDate creationDate;

    private String roomName;

    private String userName;


}
