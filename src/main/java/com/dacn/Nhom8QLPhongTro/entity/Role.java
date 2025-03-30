package com.dacn.Nhom8QLPhongTro.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;



@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "Name must be less than 50 characters")
    @NotBlank(message = "Name is require")
    @Column( length = 50, nullable = false)
    private String name;

    @Size(max = 250,message = "Description must be less than 250 characters")
    @Column(length = 250)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users =  new HashSet<>();

}
