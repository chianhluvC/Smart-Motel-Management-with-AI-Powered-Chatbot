package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MotelFloorDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn
    private Motel motel;


    @ManyToOne
    @JoinColumn
    private Floor floor;

    @OneToMany(mappedBy = "motelFloorDetail", cascade = CascadeType.ALL)
    List<Room> rooms;



}
