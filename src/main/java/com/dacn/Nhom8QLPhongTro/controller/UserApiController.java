package com.dacn.Nhom8QLPhongTro.controller;

import com.dacn.Nhom8QLPhongTro.dto.JwtResponseDto;
import com.dacn.Nhom8QLPhongTro.dto.RefreshTokenRequestDto;
import com.dacn.Nhom8QLPhongTro.dto.UserDto;
import com.dacn.Nhom8QLPhongTro.entity.AuthRequest;
import com.dacn.Nhom8QLPhongTro.entity.RefreshToken;
import com.dacn.Nhom8QLPhongTro.entity.User;
import com.dacn.Nhom8QLPhongTro.exceptions.TokenRefreshException;
import com.dacn.Nhom8QLPhongTro.services.JwtService;
import com.dacn.Nhom8QLPhongTro.services.RefreshTokenService;
import com.dacn.Nhom8QLPhongTro.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private UserDto convertToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhoneNumber());
        userDto.setName(user.getName());
        return userDto;
    }


    private User convertToUser(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhone());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        return user;
    }



    @PostMapping("/addNewUser")
    public String addNewUser(@Valid @RequestBody UserDto userDto) {
        return userService.save(convertToUser(userDto));
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }



    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<UserDto> userProfile(HttpServletRequest request) {
        User user = userService.getUserLogin(request);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToUserDto(user));
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String adminProfile() {
        return "adminProfile";
    }


    @PostMapping("/generateToken")
    public JwtResponseDto authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if(authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
            return
                    JwtResponseDto.builder().accessToken(jwtService.generateToken(authRequest.getUsername())).token(refreshToken.getToken()).build();
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }


    @PostMapping("/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String tokenRefresh = refreshTokenRequestDto.getToken();
        return refreshTokenService.findByToken(refreshTokenRequestDto.getToken()).map(refreshTokenService::verifyRefreshToken).map(RefreshToken::getUser).map(user -> {
            String accessToken = jwtService.generateToken(user.getUsername());
            return JwtResponseDto.builder().accessToken(accessToken).token(refreshTokenRequestDto.getToken()).build();
        }).orElseThrow(()->new TokenRefreshException( tokenRefresh,"Refresh token not found!"));
    }


    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        refreshTokenService.findByToken(refreshTokenRequestDto.getToken()).ifPresent(refreshTokenService::deleteRefreshToken);
    }


    @GetMapping("/getUsernameLogin")
    public ResponseEntity<String> getUserNameLogin(HttpServletRequest request) {
        User user = userService.getUserLogin(request);
        if(user == null) {
            return ResponseEntity.status(201).body("User not found!");
        }
        return ResponseEntity.status(200).body(user.getUsername());
    }
    

    @GetMapping("/getRole")
    public ResponseEntity<String> getRole(HttpServletRequest request) {
        User user = userService.getUserLogin(request);
        if(user==null)
            return ResponseEntity.status(201).body("Role not found!");
        String roleName = userService.getRoleByUser(user);
        if(roleName == null) {
            return ResponseEntity.status(201).body("Role not found!");
        }
        return ResponseEntity.status(200).body(roleName);
    }


    @PutMapping("/edit-profile")
    public ResponseEntity<UserDto> editProfile(@RequestBody UserDto userDto) {
        User user = userService.getUserByUsername(userDto.getUsername());
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setPhoneNumber(userDto.getPhone());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return ResponseEntity.ok(convertToUserDto(userService.updateUser(user)));
    }





}
