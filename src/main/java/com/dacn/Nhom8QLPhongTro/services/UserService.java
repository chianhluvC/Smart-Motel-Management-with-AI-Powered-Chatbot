package com.dacn.Nhom8QLPhongTro.services;



import com.dacn.Nhom8QLPhongTro.entity.User;
import com.dacn.Nhom8QLPhongTro.repository.IRoleRepository;
import com.dacn.Nhom8QLPhongTro.repository.IuserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;



@RequiredArgsConstructor
@Service
public class UserService {


    private final IuserRepository userRepository;

    private final IRoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtService jwtService;


    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(encoder.encode("1"));
            user.setEmail("admin@admin.com");
            user.setName("admin");
            user.setPhoneNumber("123456789");
            userRepository.save(user);
            Long userId = userRepository.getUserIdByUsername(user.getUsername());
            Long roleId = roleRepository.getRoleIdByName("ADMIN");
            userRepository.addRoleToUser(userId, roleId);
        }
    }


    public String save(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if(roleId!=0&&userId!=0)
            userRepository.addRoleToUser(userId,roleId);
        return "User added successfully";
    }
    

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }


    public User getUserLogin(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String username;
        String token;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        else {
            return null;
        }
        return userRepository.findByUsername(username);
    }


    public String getRoleByUser(User user){
        String role = null;
        if(Arrays.stream(userRepository.getRoleOfUser(user.getId())).findFirst().isPresent()){
           role  = Arrays.stream(userRepository.getRoleOfUser(user.getId())).findFirst().get();
        }
        return role;
    }



    public User updateUser(User user){
        return userRepository.save(user);
    }



}
