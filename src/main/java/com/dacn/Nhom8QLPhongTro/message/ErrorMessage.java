package com.dacn.Nhom8QLPhongTro.message;

import lombok.*;

import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private int statusCode;

    private Date timestamp;

    private String message;

    private String description;

}
