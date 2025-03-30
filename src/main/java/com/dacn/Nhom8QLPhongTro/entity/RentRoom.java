package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;



@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class RentRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Size(max = 50, message = "Status maximum is 50 characters")
    private String status;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    @Min(value = 0, message = "Minimum depositPrice is 0")
    private Double depositPrice;

    private int quantityPeople;

    private LocalDate creationDate;

    private Boolean isCheckOut;

    @ManyToOne
    @JoinColumn
    private Room room;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany(mappedBy = "rentRoom", cascade = CascadeType.ALL)
    private List<OrderRoom> orderRooms;

    @OneToMany(mappedBy = "rentRoom", cascade = CascadeType.ALL)
    private List<ElectricWater> electricWaters;

    @OneToMany(mappedBy = "rentRoom", cascade = CascadeType.ALL)
    private List<OtherService> otherServices;

}
