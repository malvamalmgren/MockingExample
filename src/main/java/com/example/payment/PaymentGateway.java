package com.example.payment;

public interface PaymentGateway {
    PaymentApiResponse charge(String apiKey, double amount);
}
