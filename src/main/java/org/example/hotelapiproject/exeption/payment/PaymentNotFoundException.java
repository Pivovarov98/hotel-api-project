package org.example.hotelapiproject.exeption.payment;

import java.util.NoSuchElementException;

public class PaymentNotFoundException extends NoSuchElementException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
