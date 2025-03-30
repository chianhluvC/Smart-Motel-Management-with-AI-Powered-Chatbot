package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.ElectricWaterDto;
import com.dacn.Nhom8QLPhongTro.entity.ElectricWater;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.entity.ServiceRoom;
import com.dacn.Nhom8QLPhongTro.services.ElectricWaterService;
import com.dacn.Nhom8QLPhongTro.services.RentRoomService;
import com.dacn.Nhom8QLPhongTro.services.ServiceRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/electric-waters")
public class ElectricWaterApiController {


    private final ElectricWaterService electricWaterService;
    private final ServiceRoomService serviceRoomService;
    private final RentRoomService rentRoomService;

    private ElectricWaterDto convertToElectricWaterDto(ElectricWater electricWater) {
        ElectricWaterDto electricWaterDto = new ElectricWaterDto();
        electricWaterDto.setId(electricWater.getId());
        electricWaterDto.setOldIndex(electricWater.getOldIndex());
        electricWaterDto.setNewIndex(electricWater.getNewIndex());
        electricWaterDto.setStatus(electricWater.getStatus());
        electricWaterDto.setServiceName(electricWater.getService().getName());
        electricWaterDto.setRentRoomId(electricWater.getRentRoom().getId());
        return electricWaterDto;
    }


    private ElectricWater convertToElectricWater(ElectricWaterDto electricWaterDto) {
        ElectricWater electricWater = new ElectricWater();
        electricWater.setId(electricWaterDto.getId());
        electricWater.setOldIndex(electricWaterDto.getOldIndex());
        electricWater.setNewIndex(electricWaterDto.getNewIndex());
        electricWater.setStatus(electricWaterDto.getStatus());
        electricWater.setService(serviceRoomService.getServiceRoomByName(electricWaterDto.getServiceName()));
        electricWater.setRentRoom(rentRoomService.getRentRoomById(electricWaterDto.getRentRoomId()).orElse(null));
        return electricWater;
    }


    @GetMapping("/rent-room/{id}")
    public List<ElectricWaterDto> getAllElectricWatersByRentRoomId(@PathVariable Long id) {
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        return electricWaterService.getAllElectricWatersByRentRoomWithAllStatus(rentRoom).stream().map(this::convertToElectricWaterDto).toList();
    }

    @PostMapping
    public ResponseEntity<ElectricWaterDto> createElectricWater(@RequestBody ElectricWaterDto electricWaterDto) {
        RentRoom rentRoom = rentRoomService.getRentRoomById(electricWaterDto.getRentRoomId()).orElse(null);
        if(rentRoom != null) {
            ElectricWater electricWater = electricWaterService.createElectricWater(convertToElectricWater(electricWaterDto));
            return ResponseEntity.ok(convertToElectricWaterDto(electricWater));
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ElectricWaterDto> updateElectricWater(@PathVariable Long id, @RequestBody ElectricWaterDto electricWaterDto) {
        ElectricWater electricWater = electricWaterService.getElectricWaterById(id);
        if(electricWater != null) {
            electricWaterDto.setId(electricWater.getId());
            return ResponseEntity.ok(convertToElectricWaterDto(electricWaterService.updateElectricWater(convertToElectricWater(electricWaterDto))));
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteElectricWater(@PathVariable Long id) {
        ElectricWater electricWater = electricWaterService.getElectricWaterById(id);
        if(electricWater == null) {
            return ResponseEntity.notFound().build();
        }
        electricWaterService.deleteElectricWater(electricWater);
        return ResponseEntity.ok("Xóa thành công");
    }


    @GetMapping("/check")
    public ResponseEntity<String> checkWaterElectricExisting(@RequestParam Long idRent, @RequestParam Long id){
        ServiceRoom serviceRoom = serviceRoomService.getServiceRoom(id);
        RentRoom rentRoom = rentRoomService.getRentRoomById(idRent).orElse(null);
        if(serviceRoom == null || rentRoom==null){
            return ResponseEntity.notFound().build();
        }
        if(!electricWaterService.checkExisting(rentRoom, serviceRoom)) {
            return ResponseEntity.ok().body("Có thể tạo.");
        }
        else {
            return ResponseEntity.status(201).body("Dịch vụ đã tồn tại.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectricWaterDto> getById(@PathVariable Long id){
        return ResponseEntity.ok().body(convertToElectricWaterDto(electricWaterService.getElectricWaterById(id)));
    }


}
