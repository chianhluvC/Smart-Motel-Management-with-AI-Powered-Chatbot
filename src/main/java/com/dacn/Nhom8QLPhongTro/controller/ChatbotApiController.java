package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.entity.User;
import com.dacn.Nhom8QLPhongTro.services.ChatbotService;
import com.dacn.Nhom8QLPhongTro.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatbotApiController {


    private final ChatbotService chatbotService;
    private final UserService userService;

    @GetMapping("/ask")
    public ResponseEntity<String> ask(@RequestParam String message, HttpServletRequest request) {
        String userLogin = "";
        User user = userService.getUserLogin(request);
        if(user != null) {
            userLogin = user.getUsername();
        }
        String answer = chatbotService.handleUserRequest(message, userLogin);
        return ResponseEntity.ok(answer);
    }


}
