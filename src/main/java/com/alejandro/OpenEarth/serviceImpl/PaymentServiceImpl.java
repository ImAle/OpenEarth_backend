package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.Payment;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.PaymentRepository;
import com.alejandro.OpenEarth.service.PaymentService;
import com.alejandro.OpenEarth.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    @Qualifier("paymentRepository")
    private PaymentRepository paymentRepository;

//    @Override
//    public Payment createPaymentForRent(PaymentCreationDto paymentDto) {
//
//        Payment payment = new Payment();
//        payment.setPaymentId(paymentDto.getPaymentId());
//        payment.setPayerId(paymentDto.getPayerId());
//        payment.setMethod(paymentDto.getMethod());
//        payment.setStatus(paymentDto.getStatus());
//        payment.setCurrency(paymentDto.getCurrency());
//        payment.setDescription(paymentDto.getDescription());
//        payment.setAmount(paymentDto.getAmount());
//        payment.setCreatedAt(LocalDateTime.now());
//        payment.setUpdatedAt(LocalDateTime.now());
//
//        return paymentRepository.save(payment);
//    }

    @Override
    public Payment saveFromCapture(Map<String,Object> capResp, User user) {
        System.out.println(capResp.toString());

        String orderId = (String) capResp.get("id");
        String status  = (String) capResp.get("status");
        String payerId = (String)((Map)capResp.get("payer")).get("payer_id");
        var pu = ((List<Map<String,Object>>)capResp.get("purchase_units")).get(0);
        var cap = ((List<Map<String,Object>>)((Map)pu.get("payments")).get("captures")).get(0);

        Map amount = (Map)cap.get("amount");
        System.out.println("map amount");
        System.out.println(amount.toString());

        Payment p = new Payment();
        p.setPaymentId(orderId);
        p.setPayerId(payerId);
        p.setCurrency((String)amount.get("currency_code"));
        p.setAmount(Double.parseDouble((String)amount.get("value")));
        p.setMethod("Paypal");
        p.setDescription("Purchase Order: " + orderId + " at " + LocalDateTime.now() + " for " + amount.get("value") + amount.get("currency_code"));
        p.setStatus(status);
        p.setUserId(user.getId());
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(p);
    }

//    @Override
//    public Double getTotalEarningsByOwner(Long ownerId) {
//        List<Payment> payments = paymentRepository.findByRent_House_User_Id(ownerId);
//        return payments.stream()
//                .filter(pay -> "approved".equalsIgnoreCase(pay.getStatus()))
//                .mapToDouble(Payment::getAmount).sum();
//    }

//    @Override
//    public Double getEarningsByRent(Long rentId) {
//        List<Payment> payments = paymentRepository.findByPaymentIdAndStatusIgnoreCase(rentId, "approved");
//        return payments.stream().mapToDouble(Payment::getAmount).sum();
//    }
}
