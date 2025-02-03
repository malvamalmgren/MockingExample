package com.example.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    @Mock
    PaymentGateway paymentGateway;
    @Mock
    PaymentDatabase paymentDatabase;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    PaymentProcessor paymentProcessor;

    @Test
    @DisplayName("Should use executeUpdate when processPayment is successful")
    void paymentApiResponsShouldReturnFalseIfAmountIsInvalid() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(true));
        paymentProcessor.processPayment(100);
        verify(paymentDatabase, atLeastOnce()).executeUpdate(anyString());
    }

    @Test
    @DisplayName("Should use sendPaymentConfirmation when processPayment is successful")
    void shouldUseSendPaymentConfirmationWhenProcessPaymentIsSuccessful() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(true));
        paymentProcessor.processPayment(100);
        verify(notificationService, atLeastOnce()).sendPaymentConfirmation(anyString(), anyDouble());
    }

    @Test
    @DisplayName("Should not executeUpdate when processPayment is unsuccessful")
    void shouldNotExecuteUpdateWhenProcessPaymentIsUnsuccessful() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(false));
        paymentProcessor.processPayment(100);
        verify(paymentDatabase, never()).executeUpdate(anyString());
    }
}
