package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.entity.EmailDetails;
import com.dacn.Nhom8QLPhongTro.entity.OrderRoom;
import com.dacn.Nhom8QLPhongTro.services.EmailService;
import com.dacn.Nhom8QLPhongTro.services.OrderRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sendMail")
public class EmailApiController {

    private final EmailService emailService;
    private final OrderRoomService orderRoomService;

    // Sending a simple Email
    @PostMapping("/")
    public String
    sendMail(@RequestBody EmailDetails details)
    {
        return emailService.sendSimpleMail(details);
    }

    // Sending email with attachment
    @PostMapping("/WithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {
        return emailService.sendMailWithAttachment(details);
    }

    // Sending orderRoom email
    @PostMapping("/orderRoom/{id}")
    public String sendMailOrderRoom(@PathVariable Long id){
        OrderRoom orderRoom = orderRoomService.findOrderRoomById(id).orElse(null);
        EmailDetails details = new EmailDetails();
        if(orderRoom != null){
            details.setRecipient(orderRoom.getRentRoom().getUser().getEmail());
            details.setMsgBody(orderRoomService.createOrderRoomForMail(orderRoom));
            details.setSubject("NHÀ TRỌ HUYCONY GỬI HÓA ĐƠN TIỀN TRỌ");
            return emailService.sendSimpleMail(details);
        }
        return "Không tìm thấy hóa đơn cần gửi";
    }



}
