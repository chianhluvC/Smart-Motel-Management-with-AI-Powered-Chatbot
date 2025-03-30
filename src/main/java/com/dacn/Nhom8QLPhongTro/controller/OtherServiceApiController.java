package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.OtherServiceDto;
import com.dacn.Nhom8QLPhongTro.entity.OtherService;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.services.OtherServiceService;
import com.dacn.Nhom8QLPhongTro.services.RentRoomService;
import com.dacn.Nhom8QLPhongTro.services.ServiceRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/other-services")
public class OtherServiceApiController {


    private final OtherServiceService otherServiceService;

    private final ServiceRoomService serviceRoomService;

    private final RentRoomService rentRoomService;


    private OtherServiceDto convertToOtherServiceDto(OtherService otherService) {
        OtherServiceDto otherServiceDto = new OtherServiceDto();
        otherServiceDto.setId(otherService.getId());
        otherServiceDto.setQuantity(otherService.getQuantity());
        otherServiceDto.setStatus(otherService.getStatus());
        otherServiceDto.setServiceName(otherService.getService().getName());
        otherServiceDto.setRentRoomId(otherService.getRentRoom().getId());
        return otherServiceDto;
    }


    private OtherService convertToOtherService(OtherServiceDto otherServiceDto) {
        OtherService otherService = new OtherService();
        otherService.setId(otherServiceDto.getId());
        otherService.setQuantity(otherServiceDto.getQuantity());
        otherService.setStatus(otherServiceDto.getStatus());
        otherService.setService(serviceRoomService.getServiceRoomByName(otherServiceDto.getServiceName()));
        otherService.setRentRoom(rentRoomService.getRentRoomById(otherServiceDto.getRentRoomId()).orElse(null));
        return otherService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<OtherServiceDto> getOtherServiceById(@PathVariable Long id){
        OtherService otherService = otherServiceService.getOtherServiceById(id).orElse(null);
        if(otherService==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(convertToOtherServiceDto(otherService));
    }



    @GetMapping("/rent-room/{id}")
    public List<OtherServiceDto> getAllOtherServiceByRentRoomId(@PathVariable Long id) {
        RentRoom rentRoom = rentRoomService.getRentRoomById(id).orElse(null);
        if (rentRoom == null) {
            return null;
        }
        return otherServiceService.getOtherServiceByRentRoomWithAllStatus(rentRoom).stream().map(this::convertToOtherServiceDto).toList();
    }


    @PostMapping
    public ResponseEntity<OtherServiceDto> createOtherService(@RequestBody OtherServiceDto otherServiceDto) {

        RentRoom rentRoom = rentRoomService.getRentRoomById(otherServiceDto.getRentRoomId()).orElse(null);
        if(rentRoom == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToOtherServiceDto(otherServiceService.createOtherService(convertToOtherService(otherServiceDto))));
    }


    @PutMapping("/{id}")
    public ResponseEntity<OtherServiceDto> updateOtherService(@PathVariable Long id, @RequestBody OtherServiceDto otherServiceDto) {
        OtherService otherService = otherServiceService.getOtherServiceById(id).orElse(null);
        if(otherService == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToOtherServiceDto(otherServiceService.updateOtherService(convertToOtherService(otherServiceDto))));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOtherService(@PathVariable Long id) {
        OtherService otherService = otherServiceService.getOtherServiceById(id).orElse(null);
        if(otherService == null) {
            return ResponseEntity.notFound().build();
        }
        otherServiceService.deleteOtherService(otherService.getId());
        return ResponseEntity.ok("Xóa dịch vụ thành công!");
    }


    @GetMapping("/check")
    public ResponseEntity<String> checkOtherServiceExisting(@RequestParam Long idRent, @RequestParam Long idService){

        RentRoom rentRoom = rentRoomService.getRentRoomById(idRent).orElse(null);
        if(rentRoom==null)
            return ResponseEntity.notFound().build();
        if (!otherServiceService.checkExisting(rentRoom, idService))
            return ResponseEntity.ok("Có thể tạo.");
        else
            return ResponseEntity.status(201).body("Dịch vụ đã tồn tại.");
    }


}
