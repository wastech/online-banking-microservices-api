package com.example.loan.exception;

public class LoanServiceException extends RuntimeException {
    public LoanServiceException(String message) {
        super(message);
    }
}