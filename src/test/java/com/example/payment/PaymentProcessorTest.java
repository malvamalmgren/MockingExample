package com.example.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    @InjectMocks
    PaymentProcessor paymentProcessor;

    @Mock
    PaymentGateway paymentGateway;
    @Mock
    PaymentDatabase paymentDatabase;
    @Mock
    NotificationService notificationService;

    @Test
    @DisplayName("Should use executeUpdate when processPayment is successful")
    void shouldUseExecuteUpdateWhenProcessPaymentIsSuccessful() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(true));
        paymentProcessor.processPayment(100);
        verify(paymentDatabase, Mockito.times(1)).executeUpdate(anyString());
    }

    @Test
    @DisplayName("Should use sendPaymentConfirmation when processPayment is successful")
    void shouldUseSendPaymentConfirmationWhenProcessPaymentIsSuccessful() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(true));
        paymentProcessor.processPayment(100);
        verify(notificationService, Mockito.times(1)).sendPaymentConfirmation(anyString(), anyDouble());
    }

    @Test
    @DisplayName("Should not executeUpdate when processPayment is unsuccessful")
    void shouldNotExecuteUpdateWhenProcessPaymentIsUnsuccessful() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(false));
        paymentProcessor.processPayment(100);
        verify(paymentDatabase, never()).executeUpdate(anyString());
    }

    @Test
    @DisplayName("Should not sendPaymentConfirmation when processPayment is unsuccessful")
    void shouldNotSendPaymentConfirmationWhenProcessPaymentIsUnsuccessful() {
        when(paymentGateway.charge(anyString(), anyDouble())).thenReturn(new PaymentApiResponse(false));
        paymentProcessor.processPayment(100);
        verify(notificationService, never()).sendPaymentConfirmation(anyString(), anyDouble());
    }
}
