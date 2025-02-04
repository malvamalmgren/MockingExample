package com.example.payment;

public interface NotificationService {
    void sendPaymentConfirmation(String mail, double amount);
}
