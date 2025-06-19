package com.bkap.gateway;

import java.util.Map;

import com.bkap.entity.Order;

public interface PaymentGateway {
    String createPaymentRequest(Order order, String ipAddress);
    boolean verifyPaymentCallback(Map<String, String> callbackParams);
}
