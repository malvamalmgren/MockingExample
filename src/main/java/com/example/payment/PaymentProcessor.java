package com.example.payment;

public class PaymentProcessor {
    private static final String API_KEY = "sk_test_123456";

    private final PaymentGateway paymentGateway;
    private final PaymentDatabase paymentDatabase;
    private final NotificationService notificationService;

    //Added constructor to allow DI
    public PaymentProcessor(PaymentGateway paymentGateway, PaymentDatabase paymentDatabase, NotificationService emailNotifier) {
        this.paymentGateway = paymentGateway;
        this.paymentDatabase = paymentDatabase;
        this.notificationService = emailNotifier;
    }
    //Changed method to use DI variables
    public boolean processPayment(double amount) {
        PaymentApiResponse response = paymentGateway.charge(API_KEY, amount);
        if (response.isSuccess()) {
            paymentDatabase
                    .executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
            notificationService.sendPaymentConfirmation("user@example.com", amount);

        }
        return response.isSuccess();
    }
}
