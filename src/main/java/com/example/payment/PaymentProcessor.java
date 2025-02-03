package com.example.payment;

public class PaymentProcessor {
    private static final String API_KEY = "sk_test_123456";

    private final PaymentGateway paymentGateway;
    private final PaymentDatabase paymentDatabase;
    private final NotificationService notificationService;

    //Added constructor to allow dependency injection
    public PaymentProcessor(PaymentGateway paymentGateway, PaymentDatabase paymentDatabase, NotificationService emailNotifier) {
        this.paymentGateway = paymentGateway;
        this.paymentDatabase = paymentDatabase;
        this.notificationService = emailNotifier;
    }

    public boolean processPayment(double amount) {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = PaymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (response.isSuccess()) {
            PaymentDatabase.getInstance()
                    .executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
        }

        // Skickar e-post direkt
        if (response.isSuccess()) {
            NotificationService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
