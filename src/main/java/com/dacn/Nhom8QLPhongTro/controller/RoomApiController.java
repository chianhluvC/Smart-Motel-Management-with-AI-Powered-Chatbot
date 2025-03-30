package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.PageNumberDto;
import com.dacn.Nhom8QLPhongTro.dto.RoomDto;
import com.dacn.Nhom8QLPhongTro.entity.ImageRoom;
import com.dacn.Nhom8QLPhongTro.entity.MotelFloorDetail;
import com.dacn.Nhom8QLPhongTro.entity.Room;
import com.dacn.Nhom8QLPhongTro.message.ResponseMessage;
import com.dacn.Nhom8QLPhongTro.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomApiController {


    private final RoomService roomService;

    private final CategoryService categoryService;

    private final FloorService floorService;

    private final FilesStorageService filesStorageService;

    private final ImageRoomService imageRoomService;

    private final MotelFloorDetailService motelFloorDetailService;

    private final MotelService motelService;


    @SneakyThrows
    private RoomDto convertToRoomDto(Room room)  {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setName(room.getName());
        roomDto.setIsRent(room.getIsRent());
        roomDto.setPrice(room.getCategory().getPrice());
        roomDto.setCategoryName(categoryService.getCategory(room.getCategory().getId()).getName());
        roomDto.setFloorName(room.getMotelFloorDetail().getFloor().getName());
        roomDto.setMotelName(room.getMotelFloorDetail().getMotel().getName());
        roomDto.setSize(categoryService.getCategory(room.getCategory().getId()).getSize());
        roomDto.setDescription(categoryService.getCategory(room.getCategory().getId()).getDescription());
        roomDto.setQuantityBathRoom(categoryService.getCategory(room.getCategory().getId()).getQuantityBathRoom());
        roomDto.setQuantityBedRoom(categoryService.getCategory(room.getCategory().getId()).getQuantityBedRoom());
        roomDto.setAddress(room.getMotelFloorDetail().getMotel().getAddress());
        List<byte[]> images = new ArrayList<>();
        imageRoomService.getImageRoomByRoom(room).forEach(
               p->{
                   try {
                       images.add(filesStorageService.imageToByteArray(p.getImage()));
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
        );
        roomDto.setImage(images);
        return roomDto;
    }


    private Room convertToRoom(RoomDto roomDto){
        Room room = new Room();
        room.setId(roomDto.getId());
        room.setName(roomDto.getName());
        room.setIsRent(roomDto.getIsRent());
        room.setCategory(categoryService.getCategoryByName(roomDto.getCategoryName()));
        room.setMotelFloorDetail(motelFloorDetailService.findByFloorNameAndMotelName(roomDto.getFloorName(),roomDto.getMotelName()));
        return room;
    }


    @GetMapping
    @ResponseBody
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        if(rooms.isEmpty()){
            return null;
        }
        return rooms.stream().map(this::convertToRoomDto).toList();
    }


    @PostMapping
    public ResponseEntity<ResponseMessage> createRoom(@ModelAttribute RoomDto roomDto, @RequestParam("files") MultipartFile[] files ) {
        String message;
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            if(motelFloorDetailService.findByFloorNameAndMotelName(roomDto.getFloorName(),roomDto.getMotelName()) == null){
                MotelFloorDetail motelFloorDetail = new MotelFloorDetail();
                motelFloorDetail.setFloor(floorService.getFloorByName(roomDto.getFloorName()));
                motelFloorDetail.setMotel(motelService.getMotelByName(roomDto.getMotelName()));
                motelFloorDetailService.save(motelFloorDetail);
            }
            Room room = roomService.addRoom(convertToRoom(roomDto));
            Arrays.stream(files).forEach(p->{
                String path = filesStorageService.save(p);
                ImageRoom imageRoom = new ImageRoom();
                imageRoom.setRoom(room);
                imageRoom.setImage(path);
                imageRoomService.addImageRoom(imageRoom);
            });
            message = "Create room successfully";
            responseMessage.setMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        } catch (Exception e) {

            message = "Không thể tải ảnh lên: " + e.getMessage();
            responseMessage.setMessage(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }

    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RoomDto>  getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        if(room == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(convertToRoomDto(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage>  updateRoom(@PathVariable Long id, @ModelAttribute RoomDto roomDto, @RequestParam("files") MultipartFile[] files) {
        Room room = roomService.getRoomById(id);
        String message;
        ResponseMessage responseMessage = new ResponseMessage();
        if(room == null){
            return ResponseEntity.notFound().build();
        }
        List<ImageRoom> imageRooms = imageRoomService.getImageRoomByRoom(room);
        if(!imageRooms.isEmpty()) {
            imageRooms.forEach(p-> filesStorageService.delete(p.getImage()));
            imageRoomService.deleteImageRoomByRoom(room);
        }
        try {
            if(motelFloorDetailService.findByFloorNameAndMotelName(roomDto.getFloorName(),roomDto.getMotelName()) == null){
                MotelFloorDetail motelFloorDetail = new MotelFloorDetail();
                motelFloorDetail.setFloor(floorService.getFloorByName(roomDto.getFloorName()));
                motelFloorDetail.setMotel(motelService.getMotelByName(roomDto.getMotelName()));
                motelFloorDetailService.save(motelFloorDetail);
            }
            Arrays.stream(files).forEach(p->{
                String path = filesStorageService.save(p);
                ImageRoom imageRoom = new ImageRoom();
                imageRoom.setRoom(room);
                imageRoom.setImage(path);
                imageRoomService.addImageRoom(imageRoom);
            });
            roomDto.setId(id);
            roomService.updateRoom(convertToRoom(roomDto));
            responseMessage.setMessage("Update room successfully");
            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
        } catch (Exception e) {
            message = "Không thể cập nhật phòng: " + e.getMessage();
            responseMessage.setMessage(message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
        }

    }



    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ResponseMessage> deleteRoom(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        if(room == null) {
            return ResponseEntity.notFound().build();
        }
        List<ImageRoom> imageRooms = imageRoomService.getImageRoomByRoom(room);
        if(!imageRooms.isEmpty()) {
            imageRooms.forEach(p-> filesStorageService.delete(p.getImage()));
            imageRoomService.deleteImageRoomByRoom(room);
        }
        roomService.deleteRoom(room.getId());
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Delete room successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }


    @GetMapping("/pagination")
    @ResponseBody
    public List<RoomDto> getAllRoomWithPagination(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "6") int size) {
        Page<Room> listRoom = roomService.getAllPagination(page, size);
        if(listRoom.isEmpty()){
            return null;
        }
        return listRoom.stream().map(this::convertToRoomDto).toList();
    }

    @GetMapping("/pageNumber")
    @ResponseBody
    public ResponseEntity<PageNumberDto> getPageNumber(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "6") int size){
        PageNumberDto pageNumberDto = new PageNumberDto();
        pageNumberDto.setPage(page);
        pageNumberDto.setSize(size);
        int quantityRoom = roomService.getAllRooms().size();
        if(quantityRoom%pageNumberDto.getSize()>0){
            pageNumberDto.setQuantityPage(quantityRoom/pageNumberDto.getSize()+1);
        }
        else
            pageNumberDto.setQuantityPage(quantityRoom/pageNumberDto.getSize());
        return ResponseEntity.status(HttpStatus.OK).body(pageNumberDto);
    }



    @GetMapping("/search")
    @ResponseBody
    public List<RoomDto> getRoomBySearch(@RequestParam(name = "keyword") String keyword) {
        List<Room> listSearch = roomService.searchRoom(keyword);
        if(listSearch.isEmpty()){
            return null;
        }
        return listSearch.stream().map(this::convertToRoomDto).toList();
    }






}





