package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.Role;
import com.dacn.Nhom8QLPhongTro.repository.IRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final IRoleRepository roleRepository;


    @PostConstruct
    public void createDefaultRole() {

        if(roleRepository.count() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setId(Long.parseLong("1"));
            roleAdmin.setName("ADMIN");
            Role roleUser = new Role();
            roleUser.setId(Long.parseLong("2"));
            roleUser.setName("USER");
            List<Role> roles = new ArrayList<>();
            roles.add(roleAdmin);
            roles.add(roleUser);
            roleRepository.saveAll(roles);
        }

    }



}
