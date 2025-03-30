package com.dacn.Nhom8QLPhongTro.dto;


import lombok.Data;
import java.io.Serializable;
import java.util.List;


@Data
public class RoomDto implements Serializable {

    private Long id;

    private String name;

    private Boolean isRent;

    private String categoryName;

    private String floorName;

    private String motelName;

    private Double price;

    private String size;

    private String description;

    private String address;

    private int quantityBathRoom;

    private int quantityBedRoom;

    private List<byte[]> image;

}
