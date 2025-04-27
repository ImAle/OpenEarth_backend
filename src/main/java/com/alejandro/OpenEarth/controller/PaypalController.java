package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.PaymentCreationDto;
import com.alejandro.OpenEarth.dto.RentCreationDto;
import com.alejandro.OpenEarth.dto.RentDto;
import com.alejandro.OpenEarth.entity.Payment;
import com.alejandro.OpenEarth.entity.Rent;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.service.PaymentService;
import com.alejandro.OpenEarth.service.PaypalService;
import com.alejandro.OpenEarth.service.RentService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class PaypalController {

    @Autowired
    @Qualifier("paypalService")
    private PaypalService paypalService;

    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;

    @Autowired
    @Qualifier("rentService")
    private RentService rentService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentCreationDto payment) {
        String approvalLink = paypalService.createOrder(payment.getAmount(), payment.getCurrency(), payment.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", approvalLink));
    }

    @PostMapping("/capturePayment")
    public ResponseEntity<?> capturePayment(@RequestHeader("Authorization") String token, @RequestParam("orderId") String orderId, @RequestBody RentCreationDto rent) {
        try{
            Map<String, Object> captureResponse = paypalService.captureOrder(orderId);
            String status = (String) captureResponse.get("status");

            if(!status.equals("COMPLETED"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Something went wrong on payment process"));

            User user = jwtService.getUser(token);
            Payment payment = paymentService.saveFromCapture(captureResponse, user);
            Rent createdRent = rentService.createRent(user, rent, payment);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("rent", new RentDto(createdRent)));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

}
