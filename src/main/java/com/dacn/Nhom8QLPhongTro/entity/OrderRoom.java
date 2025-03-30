package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OrderRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "Name max length is 50")
    private String name;

    @Min(value = 0, message = "Room price of order room should be bigger than 0")
    private Double roomPrice;

    @Min(value = 0, message = "Water price of order room should be bigger than 0")
    private Double waterPrice;

    @Min(value = 0, message = "Electric price of order room should be bigger than 0")
    private Double electricPrice;

    @Min(value = 0, message = "Other service price of order room should be bigger than 0")
    private Double otherServicePrice;

    @Min(value = 0, message = "Total price of order room should be bigger than 0")
    private Double totalPrice;

    @Size(max = 50, message = "Payment status max length is 50")
    private String paymentStatus;

    private LocalDate createDate = LocalDate.now();

    @ManyToOne
    @JoinColumn
    private RentRoom rentRoom;


}
