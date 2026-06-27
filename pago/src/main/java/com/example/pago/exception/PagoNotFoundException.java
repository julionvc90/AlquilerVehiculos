package com.example.pago.exception;

public class PagoNotFoundException extends RuntimeException {
    public PagoNotFoundException(String message) {
        super(message);
    }
}
