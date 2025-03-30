package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.MotelDto;
import com.dacn.Nhom8QLPhongTro.entity.Motel;
import com.dacn.Nhom8QLPhongTro.services.MotelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@RestController
@RequestMapping("/api/motels")
public class MotelApiController {

    private final MotelService motelService ;

    private MotelDto convMotelDto(Motel motel){
        MotelDto motelDto = new MotelDto();
        motelDto.setId(motel.getId());
        motelDto.setName(motel.getName());
        motelDto.setAddress(motel.getAddress());

        return motelDto;
    }
    private Motel conMotel(MotelDto motelDto){
        Motel motel = new Motel();
        motel.setId(motelDto.getId());
        motel.setName(motelDto.getName());
        motel.setAddress(motelDto.getAddress());

        return motel;
    }


    @GetMapping
    public List<MotelDto> getAllMotel(){
        return motelService.getAllMotels().stream().map(this::convMotelDto).toList();
    }

    @PostMapping
    public ResponseEntity<MotelDto>  addMotel( @RequestBody MotelDto motelDto){
        Motel motel = motelService.addMotel(conMotel(motelDto));
        return ResponseEntity.ok(convMotelDto(motel));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MotelDto getMotel(@PathVariable long id){ return convMotelDto(motelService.getMotel(id));}

    @ResponseBody
    @PutMapping("/{id}")
    public MotelDto updateMotel(@PathVariable long id, @RequestBody MotelDto motelDto){
        motelDto.setId(id);
        Motel updateMotel = motelService.updateMotel(conMotel(motelDto));
        return convMotelDto(updateMotel);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteMotel(@PathVariable Long id) {
        if(motelService.getMotel(id) != null) {
            motelService.deleteMotelById(id);
        }
    }



}

