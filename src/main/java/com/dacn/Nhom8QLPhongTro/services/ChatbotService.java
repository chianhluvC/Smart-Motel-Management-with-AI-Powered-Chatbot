package com.dacn.Nhom8QLPhongTro.services;

import com.dacn.Nhom8QLPhongTro.entity.RentRoom;
import com.dacn.Nhom8QLPhongTro.entity.Room;
import com.dacn.Nhom8QLPhongTro.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ChatbotService {


    private final RoomService roomService;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();  // Dùng để xử lý JSON
    private final UserService userService;
    private final RentRoomService rentRoomService;

    public String handleUserRequest(String message, String isLoggedIn) {
        message = message.toLowerCase();

        if (message.contains("phòng trống")||message.equals("1")) {
            return getAvailableRooms();
        } else if (message.equals("2")) {
            return "Bạn muốn tìm phòng à? Hãy cung cấp thông tin phòng cần tìm nhé!";
        } else if (message.contains("tìm phòng")) {
            return searchSmartRooms(message);
        } else if (message.contains("đặt phòng")||message.equals("3")) {
            return autoBooking(message, isLoggedIn);
        } else if(message.contains("đã thuê")||message.equals("4")){
            return handleRoomRented(isLoggedIn);
        } else if (message.contains("đặt cọc")||message.equals("5")) {
            return handleRoomRent(isLoggedIn);
        } else {
            return callOllamaAI(message);
        }
    }

    private String getAvailableRooms() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            return "Hiện tại không có phòng trống. Bạn có thể quay lại sau hoặc liên hệ với chủ trọ để biết thêm thông tin.";
        }
        String roomList = rooms.stream()
                .map(Room::getName)
                .collect(Collectors.joining(", "));
        return "Hiện có " + rooms.size() + " phòng trống: " + roomList + ". Bạn có muốn đặt phòng không?";
    }

    private String handleRoomRented(String isLoggedIn) {
        if (Objects.equals("", isLoggedIn)) {
            return "Bạn cần đăng nhập để xem thông tin. Vui lòng đăng nhập và thử lại!";
        }
        User user = userService.getUserByUsername(isLoggedIn);
        List<RentRoom> rentRooms = rentRoomService.getAllRentedRoomByUser(user);
        String roomList = rentRooms.stream()
                .map(RentRoom::getRoom).map(Room::getName)
                .collect(Collectors.joining(", "));
        if(roomList.isEmpty()){
            return "Danh sách đã thuê trống.";
        }
        return "Danh sách phòng bạn đã thuê là: " + roomList;
    }

    private String handleRoomRent(String isLoggedIn) {
        if (Objects.equals("", isLoggedIn)) {
            return "Bạn cần đăng nhập để xem thông tin. Vui lòng đăng nhập và thử lại!";
        }
        User user = userService.getUserByUsername(isLoggedIn);
        List<RentRoom> rentRooms = rentRoomService.getAllRentRoomByUser(user);
        String roomList = rentRooms.stream()
                .map(RentRoom::getRoom).map(Room::getName)
                .collect(Collectors.joining(", "));
        if(roomList.isEmpty()){
            return "Bạn chưa thuê phòng trọ nào.";
        }
        return "Danh sách phòng đặt cọc là: " + roomList;
    }


    private String searchSmartRooms(String message) {
        List<Room> rooms = roomService.getAllRooms();


        Pattern pricePattern = Pattern.compile("(dưới|trên) (\\d{6,7})");
        Matcher priceMatcher = pricePattern.matcher(message);
        if (priceMatcher.find()) {
            String condition = priceMatcher.group(1);
            Double price = Double.parseDouble(priceMatcher.group(2));
            rooms = rooms.stream()
                    .filter(r -> r.getCategory() != null &&
                            (condition.equals("dưới") ? r.getCategory().getPrice() < price : r.getCategory().getPrice() > price))
                    .collect(Collectors.toList());
        }


        Pattern areaPattern = Pattern.compile("vị trí (\\w+)");
        Matcher areaMatcher = areaPattern.matcher(message);
        if (areaMatcher.find()) {
            String area = areaMatcher.group(1);
            rooms = rooms.stream()
                    .filter(r -> r.getMotelFloorDetail().getMotel().getAddress().toLowerCase().contains(area))
                    .collect(Collectors.toList());
        }


        Pattern namePattern = Pattern.compile("phòng (\\w+)");
        Matcher nameMatcher = namePattern.matcher(message);
        if (nameMatcher.find()) {
            String name = nameMatcher.group(1);
            rooms = rooms.stream()
                    .filter(r -> r.getName().toLowerCase().contains(name))
                    .collect(Collectors.toList());
        }


        Pattern floorPattern = Pattern.compile("tầng (\\d+)");
        Matcher floorMatcher = floorPattern.matcher(message);
        if (floorMatcher.find()) {
            int floor = Integer.parseInt(floorMatcher.group(1));
            String floorString = "Tầng " + floor;
            rooms = rooms.stream()
                    .filter(r -> Objects.equals(r.getMotelFloorDetail().getFloor().getName(), floorString))
                    .collect(Collectors.toList());
        }


        Pattern areaSizePattern = Pattern.compile("(dưới|trên) (\\d{1,3})m2");
        Matcher areaSizeMatcher = areaSizePattern.matcher(message);
        if (areaSizeMatcher.find()) {
            String condition = areaSizeMatcher.group(1);
            int size = Integer.parseInt(areaSizeMatcher.group(2));
            rooms = rooms.stream()
                    .filter(r -> condition.equals("dưới") ? Integer.parseInt(r.getCategory().getSize())  < size : Integer.parseInt(r.getCategory().getSize()) > size)
                    .collect(Collectors.toList());
        }


        if (rooms.isEmpty()) {
            return "Không tìm thấy phòng phù hợp với tiêu chí của bạn.";
        }


        String roomsInfo = rooms.stream()
                .map(r -> "Phòng: " + r.getName() + ", Vị trí: " + r.getMotelFloorDetail().getMotel().getAddress() +
                        ", " + r.getMotelFloorDetail().getFloor().getName() +
                        ", Diện tích: " + r.getCategory().getSize() + "m2, Giá: " + r.getCategory().getPrice() + " VNĐ")
                .collect(Collectors.joining("\n"));

        String aiPrompt = "Dưới đây là danh sách các phòng trọ mà người dùng quan tâm:\n" + roomsInfo +
                "\n\nDựa vào giá, diện tích, vị trí và tầng, hãy chọn phòng tối ưu nhất và giải thích lý do.";

        return callOllamaAI(aiPrompt);
    }



    private String autoBooking(String message, String username) {
        if (username == null || username.isEmpty()) {
            return "Bạn cần đăng nhập để đặt phòng.";
        }


        Pattern roomPattern = Pattern.compile("phòng (\\w+)");
        Matcher matcher = roomPattern.matcher(message);
        if (matcher.find()) {
            String roomName = matcher.group(1);
            Optional<Room> roomOpt = roomService.searchRoom(roomName).stream().findFirst();
            if (roomOpt.isEmpty()) {
                return "Không tìm thấy phòng " + roomName + ". Vui lòng kiểm tra lại!";
            }

            Room room = roomOpt.get();
            if (room.getIsRent()) {
                return "Phòng " + roomName + " đã có người đặt.";
            }

            Optional<User> userOpt = Optional.ofNullable(userService.getUserByUsername(username));
            if (userOpt.isEmpty()) {
                return "Tài khoản không hợp lệ. Vui lòng đăng nhập lại.";
            }


            RentRoom rentRoom = new RentRoom();
            rentRoom.setUser(userOpt.get());
            rentRoom.setRoom(room);
            rentRoom.setCheckInDate(LocalDate.now());
            rentRoomService.createRentRoom(rentRoom);

            return "Bạn đã đặt thành công phòng " + roomName + ". Vui lòng chờ chủ trọ xác nhận hoặc chủ động kiểm tra thông tin phòng đang thuê!";
        }

        return "Vui lòng ghi rõ tên phòng bạn muốn đặt (VD: Tôi muốn đặt phòng 101).";
    }

    @SneakyThrows
    public String callOllamaAI(String prompt) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("model", "llama3");
        jsonNode.put("prompt", prompt + " Hãy trả lời bằng tiếng Việt.");
        jsonNode.put("stream", false);
        String requestJson = objectMapper.writeValueAsString(jsonNode);

        try {

            String OLLAMA_API_URL = "http://localhost:11500/api/generate";
            String response = webClient.post()
                    .uri(OLLAMA_API_URL)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response != null) {
                JsonNode jsonResponse = objectMapper.readTree(response);
                return jsonResponse.path("response").asText("Xin lỗi, tôi không hiểu câu hỏi của bạn.");
            }
        } catch (Exception e) {
            return "Xin lỗi, hiện tại tôi không thể kết nối đến hệ thống AI. Hãy thử lại sau!";
        }
        return "Xin lỗi, tôi không thể xử lý yêu cầu của bạn vào lúc này.";
    }



}
