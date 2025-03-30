package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OtherService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Minimum quantity is 0")
    private int quantity;

    private Boolean status;

    @ManyToOne
    @JoinColumn
    private ServiceRoom service;

    @ManyToOne
    @JoinColumn
    private RentRoom rentRoom;

}
