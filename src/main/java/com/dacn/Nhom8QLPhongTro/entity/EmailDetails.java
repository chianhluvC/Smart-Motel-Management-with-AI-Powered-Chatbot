package com.dacn.Nhom8QLPhongTro.entity;


import lombok.*;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor

public class EmailDetails {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
