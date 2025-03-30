package com.dacn.Nhom8QLPhongTro.controller;



import com.dacn.Nhom8QLPhongTro.dto.ServiceRoomDto;
import com.dacn.Nhom8QLPhongTro.entity.ServiceRoom;
import com.dacn.Nhom8QLPhongTro.services.ServiceRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("api/serviceRooms")
public class ServiceRoomApiController {

    private final ServiceRoomService serviceRoomService;


    private ServiceRoomDto convertToServiceRoomDto(ServiceRoom serviceRoom){
        ServiceRoomDto serviceRoomDto = new ServiceRoomDto();
        serviceRoomDto.setId(serviceRoom.getId());
        serviceRoomDto.setName(serviceRoom.getName());
        serviceRoomDto.setPrice(serviceRoom.getPrice());
        return serviceRoomDto;
    }

    private ServiceRoom convertToServiceRoom(ServiceRoomDto serviceRoomDto){
        ServiceRoom serviceRoom = new ServiceRoom();
        serviceRoom.setId(serviceRoomDto.getId());
        serviceRoom.setName(serviceRoomDto.getName());
        serviceRoom.setPrice(serviceRoomDto.getPrice());
        return serviceRoom;
    }


    @GetMapping
    @ResponseBody
    public List<ServiceRoomDto> getAllServiceRooms() {
        return serviceRoomService.getAllServices().stream().map(this::convertToServiceRoomDto).toList();
    }


    @GetMapping("/withOut-electricWater")
    @ResponseBody
    public List<ServiceRoomDto> getAllServiceRoomWithOutElectricWater(){
        return serviceRoomService.getListWithoutElectricWater().stream().map(this::convertToServiceRoomDto).toList();
    }

    @GetMapping("/electricWaters")
    @ResponseBody
    public List<ServiceRoomDto> getAllServiceRoomOnlyElectricWater(){
        return serviceRoomService.getListElectricWater().stream().map(this::convertToServiceRoomDto).toList();
    }


    @PostMapping
    public ServiceRoomDto createServiceRoom(@RequestBody ServiceRoomDto serviceRoomDto) {
        ServiceRoom serviceRoom = serviceRoomService.addServiceRoom(convertToServiceRoom(serviceRoomDto));
        return convertToServiceRoomDto(serviceRoom);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ServiceRoomDto getServiceRoomById(@PathVariable Long id) {
        return convertToServiceRoomDto(serviceRoomService.getServiceRoom(id));
    }

    @PutMapping("/{id}")
    public ServiceRoomDto updateServiceRoom(@PathVariable Long id, @RequestBody ServiceRoomDto serviceRoomDto) {
        serviceRoomDto.setId(id);
        ServiceRoom updateServiceRoom = serviceRoomService.updateServiceRoom(convertToServiceRoom(serviceRoomDto));
        return convertToServiceRoomDto(updateServiceRoom);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public void deleteServiceRoom(@PathVariable Long id) {
        if(serviceRoomService.getServiceRoom(id) != null) {
            serviceRoomService.deleteServiceRoomById(id);
        }
    }



}
