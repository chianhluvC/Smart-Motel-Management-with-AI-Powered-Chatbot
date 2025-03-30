package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.FloorDto;
import com.dacn.Nhom8QLPhongTro.entity.Floor;
import com.dacn.Nhom8QLPhongTro.services.FloorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/floors")
public class FloorApiController {

    private final FloorService floorService;

    private FloorDto convertToFloorDto(Floor floor) {
        FloorDto floorDto = new FloorDto();
        floorDto.setId(floor.getId());
        floorDto.setName(floor.getName());
        return floorDto;

    }



    @GetMapping
    @ResponseBody
    public List<FloorDto> getAllFloors() {
        return floorService.getAllFloors().stream().map(this::convertToFloorDto).toList();
    }



}
