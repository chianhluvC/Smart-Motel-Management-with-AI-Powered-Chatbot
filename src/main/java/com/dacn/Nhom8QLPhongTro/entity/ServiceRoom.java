package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;



@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ServiceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 50, message = "Service name should be between 1 and 50 characters")
    private String name;

    @Min(value = 0, message = "Minimum price is 0")
    private Double price;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    List<ElectricWater> electricWaters;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    List<OtherService> otherServices;


}
