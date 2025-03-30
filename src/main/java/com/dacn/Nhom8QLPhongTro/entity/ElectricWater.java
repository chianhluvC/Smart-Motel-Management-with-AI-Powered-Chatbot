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
public class ElectricWater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Old index should be bigger than 0")
    private Double oldIndex;

    @Min(value = 0, message = "New index should be bigger than 0")
    private Double newIndex;

    private Boolean status;

    @ManyToOne
    @JoinColumn
    private ServiceRoom service;

    @ManyToOne
    @JoinColumn
    private RentRoom rentRoom;


}
