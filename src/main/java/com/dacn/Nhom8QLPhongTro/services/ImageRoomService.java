package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.ImageRoom;
import com.dacn.Nhom8QLPhongTro.entity.Room;
import com.dacn.Nhom8QLPhongTro.repository.IImageRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ImageRoomService {

    private final IImageRoomRepository imageRoomRepository;

    public List<ImageRoom>  getImageRoomByRoom(Room room){
        return imageRoomRepository.findImageRoomsByRoom(room);
    }

    public void addImageRoom(ImageRoom imageRoom){
         imageRoomRepository.save(imageRoom);
    }

    public void deleteImageRoomByRoom(Room room){
        if(imageRoomRepository.findImageRoomsByRoom(room).isEmpty()){
            throw new IllegalStateException("ImageRoom with roomID "+room.getId()+" does not exist.");
        }
        imageRoomRepository.deleteAll(imageRoomRepository.findImageRoomsByRoom(room));
    }



}
