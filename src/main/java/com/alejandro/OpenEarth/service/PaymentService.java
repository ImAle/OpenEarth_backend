package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.entity.Payment;
import com.alejandro.OpenEarth.entity.User;

import java.util.Map;

public interface PaymentService {
    //Payment createPaymentForRent(PaymentCreationDto payment);
    Payment saveFromCapture(Map<String,Object> capResp, User user);
    //Double getTotalEarningsByOwner(Long ownerId);
    //Double getEarningsByRent(Long rentId);
}
