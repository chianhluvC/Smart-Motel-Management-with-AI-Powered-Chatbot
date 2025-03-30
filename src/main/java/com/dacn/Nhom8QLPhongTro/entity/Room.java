package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.util.List;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 200, message = "Room name should be between 1 and 200 characters")
    private String name;

    @ColumnDefault("false")
    private Boolean isRent = false;

    @ManyToOne
    @JoinColumn
    private Category category;

    @ManyToOne
    @JoinColumn
    private MotelFloorDetail motelFloorDetail;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<RentRoom> rentRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<ImageRoom> imageRooms;

}
