package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.entity.SettingRent;
import com.dacn.Nhom8QLPhongTro.services.SettingRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settingRent")
@RequiredArgsConstructor
public class SettingRentApiController {

    private final SettingRentService settingRentService;


    @GetMapping
    @ResponseBody
    public SettingRent getSettingRent() {
        return settingRentService.getSettingRentByName("AutoConfirm");
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<String> updateSettingRent(@RequestBody SettingRent settingRent) {
        settingRentService.updateStatus(settingRent);
        return ResponseEntity.ok().body("Cập nhật trạng thái thành công");
    }


}
