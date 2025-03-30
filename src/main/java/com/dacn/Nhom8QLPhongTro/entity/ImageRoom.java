package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ImageRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 350, message = "Image length should be between 1 to 350")
    private String image;

    @ManyToOne
    @JoinColumn
    private Room room;

}
