package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.SettingRent;
import com.dacn.Nhom8QLPhongTro.repository.ISettingRentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class SettingRentService {

    private final ISettingRentRepository settingRentRepository;

    @PostConstruct
    public void createDefaultSettingRent() {

        if(settingRentRepository.count() == 0) {
            SettingRent settingRent = new SettingRent();
            settingRent.setId(Long.parseLong("1"));
            settingRent.setName("AutoConfirm");
            settingRentRepository.save(settingRent);
        }

    }


    public Boolean isAutoConfirm() {
        SettingRent settingRent = settingRentRepository.findById(Long.parseLong("1")).orElse(null);
        if (settingRent == null) {
            return false;
        }
        else
            return settingRent.getIsActive();
    }


    public SettingRent getSettingRentByName(String name) {
        return settingRentRepository.findByName(name).orElse(null);
    }

    public void updateStatus(SettingRent settingRent){
        SettingRent existingSettingRent = settingRentRepository.findById(settingRent.getId()).orElse(null);
        if (existingSettingRent != null) {
            existingSettingRent.setIsActive(settingRent.getIsActive());
            settingRentRepository.save(existingSettingRent);
        }
    }


}
