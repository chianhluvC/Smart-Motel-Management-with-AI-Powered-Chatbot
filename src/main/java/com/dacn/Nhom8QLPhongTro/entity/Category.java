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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 100, message = "Category name should be between 1 and 100 characters")
    private String name;

    @Min(value = 0, message = "Minimum price is 0")
    private Double price;

    @Size(min = 2, max = 20, message = "Size should be between 2 and 20 characters")
    private String size;

    @Min(value = 0, message = "Minimum quantity bedRoom is 0")
    private int quantityBedRoom;

    @Min(value = 0, message = "Minimum quantity bathRoom is 0")
    private int quantityBathRoom;

    @Size(min = 5, max = 1000, message = "Description should be between 5 and 1000 characters")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Room> rooms;


}
