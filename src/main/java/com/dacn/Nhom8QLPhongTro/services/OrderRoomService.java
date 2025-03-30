package com.dacn.Nhom8QLPhongTro.services;


import com.dacn.Nhom8QLPhongTro.entity.*;
import com.dacn.Nhom8QLPhongTro.repository.IOrderRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderRoomService {

    private final IOrderRoomRepository orderRoomRepository;
    private final RentRoomService rentRoomService;
    private final ElectricWaterService electricWaterService;
    private final OtherServiceService otherServiceService;

    public OrderRoom createOrderRoom(OrderRoom orderRoom) {
        RentRoom rentRoom = rentRoomService.getRentRoomById(orderRoom.getRentRoom().getId()).orElse(null);
        if(rentRoom == null) {
            return null;
        }
        ElectricWater electric = electricWaterService.getElectricByRentRoom(rentRoom);
        ElectricWater water = electricWaterService.getWaterByRentRoom(rentRoom);
        List<OtherService> otherServices = otherServiceService.getOtherServiceByRentRoom(rentRoom);
        AtomicReference<Double> otherServicePrice = new AtomicReference<>(0.0);
        if(electric==null || !electric.getStatus())
            orderRoom.setElectricPrice(Double.parseDouble("0"));
        else {
            orderRoom.setElectricPrice(electric.getService().getPrice() * (electric.getNewIndex()-electric.getOldIndex()) );
        }
        if(water==null || !water.getStatus())
            orderRoom.setWaterPrice(Double.parseDouble("0"));
        else {
            orderRoom.setWaterPrice(water.getService().getPrice()*(water.getOldIndex())-water.getNewIndex());
        }
        if(otherServices.isEmpty())
            orderRoom.setOtherServicePrice(Double.parseDouble("0"));
        else {
            otherServices.forEach(p->
                    otherServicePrice.updateAndGet(v -> v + p.getService().getPrice()*p.getQuantity())
            );
        }
        orderRoom.setOtherServicePrice(otherServicePrice.get());
        orderRoom.setRoomPrice(rentRoom.getRoom().getCategory().getPrice());
        Double totalPrice = orderRoom.getOtherServicePrice() + orderRoom.getWaterPrice() + orderRoom.getElectricPrice() +orderRoom.getRoomPrice();
        orderRoom.setTotalPrice(totalPrice);
        orderRoom.setPaymentStatus("Chờ thanh toán");
        orderRoom.setCreateDate(LocalDate.now());
        return orderRoomRepository.save(orderRoom);
    }

    public void updatePaymentStatus(OrderRoom orderRoom, String paymentStatus) {
        orderRoom.setPaymentStatus(paymentStatus);
         orderRoomRepository.save(orderRoom);
    }

    public Optional<OrderRoom> findOrderRoomById(Long id){
        return orderRoomRepository.findById(id);
    }

    public String createOrderRoomForMail(OrderRoom orderRoom){

        Double totalPrice = orderRoom.getTotalPrice();
        String formatMoneyStr = String.format("%,.0f", totalPrice);
        String str;
        String title = "NHÀ TRỌ HUYCONY XIN CHÀO BẠN "+orderRoom.getRentRoom().getUser().getName();
        String message = "\n\nChúng tôi rất hân hạnh vì được bạn tin tưởng lựa chọn, " +
                "chúng tôi xin gửi bạn hóa đơn tiền " + orderRoom.getRentRoom().getRoom().getName();
        String styleStr1 = "\n" + "-------------------------------------------------------------------------------------";
        String orderStr = "\n" + "Nội dung thu: " + orderRoom.getName();
        String totalPriceStr = "\n" + "Tổng tiền thu (bao gồm điện nước và cả dịch vụ): " + formatMoneyStr +"đ";
        String paymentStatusStr = "\n" + "Trạng thái thanh toán: "  + orderRoom.getPaymentStatus();
        String styleStr2 = "\n" + "-------------------------------------------------------------------------------------";
        String thankStr = "\n" + "Cảm ơn bạn và chúc bạn một ngày tốt lành!";
        str = title + message + styleStr1 + orderStr + totalPriceStr + paymentStatusStr + styleStr2 + thankStr;
        return str;
    }


    public Double getAllTotalPayment(){
        Double totalPrice = 0.00;
        List<OrderRoom> orderRooms = orderRoomRepository.findAll().stream().filter(p->p.getPaymentStatus().equals("Đã thanh toán bằng VNPay")).toList();
        if(!orderRooms.isEmpty()){
            for (OrderRoom orderRoom : orderRooms){
                totalPrice+=orderRoom.getTotalPrice();
            }
        }
        return totalPrice;
    }

    public List<OrderRoom> getAllOrderRoomByUser(User user){
        return  orderRoomRepository.findAll().stream().filter(p->p.getRentRoom().getUser().getUsername().equals(user.getUsername())).toList();
    }



    public OrderRoom getOderRoomById(Long id){
        return orderRoomRepository.findById(id).orElse(null);
    }

    public List<OrderRoom> getAllOrderRoom(){
        return orderRoomRepository.findAll();
    }


}
