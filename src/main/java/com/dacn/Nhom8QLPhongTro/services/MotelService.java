package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.Motel;
import com.dacn.Nhom8QLPhongTro.repository.IMotelRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MotelService {

    private final IMotelRepository motelRepository;


    @PostConstruct
    public void init() {
        if (motelRepository.count() == 0) {
            Motel motel = new Motel();
            motel.setId(1L);
            motel.setName("HuyCoNy");
            motel.setAddress("22/2 Đường số 7 Phường Linh Trung 71308 Ho Chi Minh City");

            Motel motel2 = new Motel();
            motel2.setId(2L);
            motel2.setName("Hoa Cỏ May");
            motel2.setAddress("Xa lộ Hà Nội, Quận 9, kéo dài đến vành đai 2, phường Tân Phú, Quận 9, TP.HCM.");

            Motel motel3 = new Motel();
            motel3.setId(3L);
            motel3.setName("Tân Hương");
            motel3.setAddress("162 Tân Hương, P.Tân Quý, Q. Tân Phú");

            List<Motel> motels = new ArrayList<>();
            motels.add(motel);
            motels.add(motel2);
            motels.add(motel3);
            motelRepository.saveAll(motels);

        }
    }


    public Motel getMotelByName(String name) {
        return motelRepository.findByName(name);
    }


    public List<Motel> getAllMotels(){
        return motelRepository.findAll();
    }

    public Motel getMotel(Long id){
        return motelRepository.findById(id).orElseThrow(()->
                new RuntimeException("Motel not found on ::"+id));
    }


    public Motel addMotel(Motel motel){
        return motelRepository.save(motel);
    }

    public Motel updateMotel(@NotNull Motel motel){
        Motel existingMotel = motelRepository.findById(motel.getId())
                .orElseThrow(()->new IllegalStateException("Motel with ID "
                        + motel.getId() +" does not exist."));
        existingMotel.setName(motel.getName());
        existingMotel.setAddress(motel.getAddress());
        motelRepository.save(existingMotel);
        return existingMotel;
    }

    public void deleteMotelById(Long id){
        if(!motelRepository.existsById(id)){
            throw new IllegalStateException("Motel with ID "+id+" does not exist.");
        }
        motelRepository.deleteById(id);
    }

    public int getQuantityMotel(){
        List<Motel> motels = motelRepository.findAll();
        return motels.size();
    }

}
