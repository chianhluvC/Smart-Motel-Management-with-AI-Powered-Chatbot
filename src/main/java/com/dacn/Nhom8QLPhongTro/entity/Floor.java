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
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 20, message = "Floor name should have a length between 3 and 20 characters.")
    private String name;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL)
    private List<MotelFloorDetail> motelFloorDetails;



}
