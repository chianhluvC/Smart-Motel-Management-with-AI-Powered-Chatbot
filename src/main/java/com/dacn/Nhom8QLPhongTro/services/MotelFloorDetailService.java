package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.MotelFloorDetail;
import com.dacn.Nhom8QLPhongTro.repository.IMotelFloorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MotelFloorDetailService {

    private final IMotelFloorRepository motelFloorRepository;

    public MotelFloorDetail findByFloorNameAndMotelName(String floorName, String motelName ) {
        return motelFloorRepository.findByFloorNameAndMotelName(floorName, motelName);
    }


    public void save(MotelFloorDetail motelFloorDetail) {
        motelFloorRepository.save(motelFloorDetail);
    }



}
