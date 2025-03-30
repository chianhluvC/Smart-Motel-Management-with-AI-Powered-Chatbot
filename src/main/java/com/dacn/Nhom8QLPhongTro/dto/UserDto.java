package com.dacn.Nhom8QLPhongTro.dto;


import com.dacn.Nhom8QLPhongTro.Validator.annotation.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    @NotBlank(message = "Tên tài khoản không được để trống")
    @Size(max = 50, message = "Tên người dùng phải nhỏ hơn hoặc bằng 50 ký tự")
    @ValidUsername(message = "Tên tài khoản đã tồn tại")
    private String username;
    @Size(max = 50, message = "Tên của bạn phải nhỏ hơn hoặc bằng 50 ký tự")
    @NotBlank(message = "Tên của bạn là bắt buộc")
    private String name;
    @NotBlank(message = "Email không được để trống")
    @Size(max = 50, message = "Email phải nhỏ hơn hoặc bằng 50 ký tự")
    @Email(message = "Email phải hợp lệ!")
    private String email;
    @Pattern(regexp="(^$|[0-9]{10})",message = "Số điện thoại không hợp lệ")
    private String phone;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;


}
