package com.alejandro.OpenEarth.service;

import java.util.Map;

public interface PaypalService {
    String createOrder(Double amount, String currency, String description);
    Map<String, Object> captureOrder(String orderId);

}
