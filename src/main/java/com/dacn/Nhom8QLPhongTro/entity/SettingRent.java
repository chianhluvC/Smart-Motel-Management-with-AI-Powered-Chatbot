package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class SettingRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Size(min = 1, max = 20, message = "Setting rent name should be between 1 and 20 characters")
    private String name;

    @ColumnDefault("false")
    private Boolean isActive = false;

}
