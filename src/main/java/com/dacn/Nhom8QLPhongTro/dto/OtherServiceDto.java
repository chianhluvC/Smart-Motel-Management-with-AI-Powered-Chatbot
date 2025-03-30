package com.dacn.Nhom8QLPhongTro.dto;


import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class OtherServiceDto {

    private Long id;

    @Min(value = 0, message = "Minimum quantity is 0")
    private int quantity;

    private Boolean status;

    private String serviceName;

    private Long rentRoomId;


}
