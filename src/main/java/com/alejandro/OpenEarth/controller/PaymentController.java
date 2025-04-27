package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.service.PaymentService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

//    @PostMapping("/rent/create")
//    public ResponseEntity<?> createPayment(@RequestBody PaymentCreationDto payment){
//        try{
//            Payment pay = paymentService.createPaymentForRent(payment);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(pay);
//        }catch(RuntimeException rtex){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
//        }catch (Exception ex){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
//        }
//    }
//
//    @GetMapping("/owner")
//    public ResponseEntity<?> getTotalEarningsByOwner(@RequestHeader("Authorization") String token){
//        try{
//            Long userId = jwtService.getUser(token).getId();
//            Double earnings = paymentService.getTotalEarningsByOwner(userId);
//
//            return ResponseEntity.status(HttpStatus.OK).body(earnings);
//        }catch(RuntimeException rtex){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
//        }catch (Exception ex){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
//        }
//    }
//
//    @GetMapping("/rent")
//    public ResponseEntity<?> getTotalEarningsByRent(@RequestHeader("Authorization") String token){
//        return null;
//    }
}
