package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.ServiceRoom;
import com.dacn.Nhom8QLPhongTro.repository.IServiceRoomRepository;
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
public class ServiceRoomService {

    private final IServiceRoomRepository serviceRepository;

    @PostConstruct
    public void createDefaultServiceRoom() {
        if(serviceRepository.count() == 0) {
            List<ServiceRoom> serviceRooms = new ArrayList<>();
            ServiceRoom electric = new ServiceRoom();
            electric.setId(Long.parseLong("1"));
            electric.setName("Điện");
            electric.setPrice(2356.00);
            ServiceRoom water = new ServiceRoom();
            water.setId(Long.parseLong("2"));
            water.setName("Nước");
            water.setPrice(9900.00);
            ServiceRoom guiXe = new ServiceRoom();
            guiXe.setId(Long.parseLong("3"));
            guiXe.setName("Gửi xe");
            guiXe.setPrice(100000.00);
            ServiceRoom wifi = new ServiceRoom();
            wifi.setId(Long.parseLong("4"));
            wifi.setName("Mạng Wifi");
            wifi.setPrice(300000.00);
            serviceRooms.add(guiXe);
            serviceRooms.add(wifi);
            serviceRooms.add(electric);
            serviceRooms.add(water);
            serviceRepository.saveAll(serviceRooms);
        }
    }


    public List<ServiceRoom> getAllServices() {
        return serviceRepository.findAll();
    }

    public ServiceRoom getServiceRoom(Long id){
        return serviceRepository.findById(id).orElseThrow(()->
                new RuntimeException("Service Room not found on ::"+id));
    }

    public ServiceRoom getServiceRoomByName(String name){
        return serviceRepository.findByName(name).orElseThrow(()->
                new RuntimeException("Service Room not found on ::"+name));
    }


    public ServiceRoom addServiceRoom(ServiceRoom serviceRoom){
       return   serviceRepository.save(serviceRoom);
    }

    public ServiceRoom updateServiceRoom(@NotNull ServiceRoom serviceRoom){
        ServiceRoom existingServiceRoom = serviceRepository.findById(serviceRoom.getId())
                .orElseThrow(()->new IllegalStateException("Service Room with ID "
                        + serviceRoom.getId() +" does not exist."));
        existingServiceRoom.setName(serviceRoom.getName());
        return serviceRepository.save(existingServiceRoom);
    }

    public void deleteServiceRoomById(Long id){
        if(!serviceRepository.existsById(id)){
            throw new IllegalStateException("Service Room with ID "+id+" does not exist.");
        }
        serviceRepository.deleteById(id);
    }

    public ServiceRoom getElectric(){
        return serviceRepository.findByName("Điện").orElseThrow(()->
                new RuntimeException("Service Room not found on :: Điện"));
    }

    public ServiceRoom getWater(){
        return serviceRepository.findByName("Nước").orElseThrow(()->
                new RuntimeException("Service Room not found on :: Nước"));
    }

    public List<ServiceRoom> getListElectricWater(){
        return serviceRepository.findAll().stream().filter(p->p.getName().equals("Điện")
                || p.getName().equals("Nước")).toList();
    }

    public List<ServiceRoom> getListWithoutElectricWater(){
        return serviceRepository.findAll().stream().filter(p-> !p.getName().equals("Điện")
                && !p.getName().equals("Nước")).toList();
    }


}
