package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.ElectricWater;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.entity.ServiceRoom;
import com.dacn.Nhom8QLPhongTro.repository.IElectricWaterRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ElectricWaterService {

    private final IElectricWaterRepository electricWaterRepository;
    private final ServiceRoomService serviceRoomService;

    public ElectricWater createElectricWater(ElectricWater electricWater){
        electricWater.setStatus(true);
        return electricWaterRepository.save(electricWater);
    }

    public void deleteElectricWater(ElectricWater electricWater){
        electricWaterRepository.delete(electricWater);
    }


    public ElectricWater getElectricWaterById(Long id){
        return electricWaterRepository.findById(id).orElse(null);
    }


    public ElectricWater updateElectricWater(@NotNull ElectricWater electricWater){
        ElectricWater existingElectricWater = electricWaterRepository.findById(electricWater.getId()).orElse(null);
        if(existingElectricWater == null){
            return null;
        }
        existingElectricWater.setNewIndex(electricWater.getNewIndex());
        existingElectricWater.setOldIndex(electricWater.getOldIndex());
        existingElectricWater.setStatus(electricWater.getStatus());
        return electricWaterRepository.save(existingElectricWater);
    }


    public List<ElectricWater> getAllElectricWatersByRentRoom(RentRoom rentRoom){
        return electricWaterRepository.findElectricWaterByRentRoom(rentRoom).stream().filter(ElectricWater::getStatus).toList();
    }

    public List<ElectricWater> getAllElectricWatersByRentRoomWithAllStatus(RentRoom rentRoom){
        return electricWaterRepository.findElectricWaterByRentRoom(rentRoom);
    }

    public ElectricWater getElectricByRentRoom(RentRoom rentRoom){
        return electricWaterRepository.findElectricWaterByRentRoom(rentRoom).stream().filter(p -> p.getService().getName().equals(serviceRoomService.getElectric().getName()) && p.getStatus()).findFirst().orElse(null);
    }

    public ElectricWater getWaterByRentRoom(RentRoom rentRoom){
        return electricWaterRepository.findElectricWaterByRentRoom(rentRoom).stream().filter(p -> p.getService().getName().equals(serviceRoomService.getWater().getName()) && p.getStatus()).findFirst().orElse(null);
    }

    public void updateAllElectricWaterStatusByList(List<ElectricWater> electricWaters, Boolean status){
        electricWaters.forEach(otherService -> {
            otherService.setStatus(status);
            electricWaterRepository.save(otherService);
        });
    }

    public Boolean checkExisting(RentRoom rentRoom, ServiceRoom serviceRoom){

        List<ElectricWater> electricWaters = electricWaterRepository.findAll().stream().filter(p->p.getService().equals(serviceRoom)&&p.getRentRoom().equals(rentRoom)).toList();
        return  !electricWaters.isEmpty();
    }



}
