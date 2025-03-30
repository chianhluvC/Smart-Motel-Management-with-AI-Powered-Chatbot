package com.dacn.Nhom8QLPhongTro.services;



import com.dacn.Nhom8QLPhongTro.entity.OtherService;
import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.repository.IOtherServiceRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OtherServiceService {

    private final IOtherServiceRepository otherServiceRepository;

    public OtherService createOtherService(OtherService otherService){
        otherService.setStatus(true);
        return otherServiceRepository.save(otherService);
    }

    public Optional<OtherService> getOtherServiceById(Long id){
        return otherServiceRepository.findById(id);
    }

    public List<OtherService> getAllOtherServices(){
        return otherServiceRepository.findAll();
    }

    public OtherService updateOtherService(@NotNull OtherService otherService){
        OtherService existingOtherService = otherServiceRepository.findById(otherService.getId()).orElse(null);
        if(existingOtherService == null){
            return null;
        }
        existingOtherService.setQuantity(otherService.getQuantity());
        existingOtherService.setRentRoom(otherService.getRentRoom());
        existingOtherService.setService(otherService.getService());
        existingOtherService.setStatus(otherService.getStatus());
        return otherServiceRepository.save(existingOtherService);
    }

    public void deleteOtherService(Long id){
        otherServiceRepository.findById(id).ifPresent(otherServiceRepository::delete);
    }

    public List<OtherService> getOtherServiceByRentRoom(RentRoom rentRoom){
        return otherServiceRepository.findOtherServiceByRentRoom(rentRoom).stream().filter(OtherService::getStatus).toList();
    }

    public List<OtherService> getOtherServiceByRentRoomWithAllStatus(RentRoom rentRoom){
        return otherServiceRepository.findOtherServiceByRentRoom(rentRoom);
    }


    public void updateAllOtherServiceStatusByList(List<OtherService> otherServices, Boolean status){
        otherServices.forEach(otherService -> {
            otherService.setStatus(status);
            otherServiceRepository.save(otherService);
        });
    }


    public Boolean checkExisting(RentRoom rentRoom, Long idService){
        List<OtherService> otherServices = otherServiceRepository.findAll().stream().filter(p->p.getRentRoom().equals(rentRoom)&&p.getService().getId().equals(idService)).toList();
        return !otherServices.isEmpty();
    }


}
