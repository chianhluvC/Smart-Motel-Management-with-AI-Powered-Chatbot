package com.dacn.Nhom8QLPhongTro.entity;



import com.dacn.Nhom8QLPhongTro.Validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( length = 50, nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must be less than or equals to 50 characters")
    @ValidUsername
    private String username;

    @Column( length = 250, nullable = false)
    @NotBlank(message = "Password is required")
    private String password;
    @Column( length = 50)
    @Size(max = 50, message = "Email must be less than or equals to 50 characters")
    @Email(message = "Email should be valid!")
    private String email;

    @Column( length = 50, nullable = false)
    @Size(max = 50, message = "Your name must be less than or equals to 50 characters")
    @NotBlank(message = "your name is required")
    private String name;

    @Column( length = 12, nullable = false)
    @Size(max = 12, message = "Your phone number must less than or equals to 12 characters")
    @NotBlank(message = "your phone number is required")
    private String phoneNumber;

    @ManyToMany
    @JoinTable
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RentRoom> rentRooms;



}
