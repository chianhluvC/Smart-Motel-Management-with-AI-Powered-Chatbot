package com.dacn.Nhom8QLPhongTro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;



@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Motel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 200, message = "Motel name should have a length between 3 and 200 characters.")
    private String name;

    @Size(min = 10, max = 500, message = "Motel address name should have a length between 10 and 500 characters.")
    private String address;

    @OneToMany(mappedBy = "motel", cascade = CascadeType.ALL)
    private List<MotelFloorDetail> motelFloorDetails;


}
