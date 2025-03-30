package com.dacn.Nhom8QLPhongTro.dto;


import jakarta.validation.constraints.Min;
import lombok.Data;





@Data
public class ElectricWaterDto {


    private Long id;

    @Min(value = 0, message = "Old index should be bigger than 0")
    private Double oldIndex;

    @Min(value = 0, message = "New index should be bigger than 0")
    private Double newIndex;

    private Boolean status;

    private String serviceName;

    private Long rentRoomId;

}
