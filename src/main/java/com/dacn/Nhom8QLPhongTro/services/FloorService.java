package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.Floor;
import com.dacn.Nhom8QLPhongTro.repository.IFloorRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;




@Service
@RequiredArgsConstructor
@Transactional
public class FloorService {

    private final IFloorRepository floorRepository;


    @PostConstruct
    public void init() {
        if (floorRepository.count() == 0) {
            Floor floor = new Floor();
            floor.setId(1L);
            floor.setName("Tầng 1");

            Floor floor1 = new Floor();
            floor1.setId(2L);
            floor1.setName("Tầng 2");

            Floor floor2 = new Floor();
            floor2.setId(3L);
            floor2.setName("Tầng 3");

            List<Floor> floors = Arrays.asList(floor, floor1, floor2);
            floorRepository.saveAll(floors);
        }
    }



    public List<Floor> getAllFloors(){
        return floorRepository.findAll();
    }

    public Floor getFloor(Long id){
        return floorRepository.findById(id).orElseThrow(()->
                new RuntimeException("Floor not found on ::"+id));
    }

    public Floor getFloorByName(String name){
        return floorRepository.findByName(name).orElseThrow(()->
                new RuntimeException("Floor not found on ::"+name));
    }

    public void addFloor(Floor floor){
        floorRepository.save(floor);
    }

    public void updateFloor(@NotNull Floor floor){
        Floor existingFloor = floorRepository.findById(floor.getId())
                .orElseThrow(()->new IllegalStateException("Floor with ID "
                        + floor.getId() +" does not exist."));
        existingFloor.setName(floor.getName());
        floorRepository.save(existingFloor);
    }

    public void deleteFloor(Long id){
        if(!floorRepository.existsById(id)){
            throw new IllegalStateException("Floor with ID "+id+" does not exist.");
        }
        floorRepository.deleteById(id);
    }

    public int getQuantityFloor(){
        return floorRepository.findAll().size();
    }

}
