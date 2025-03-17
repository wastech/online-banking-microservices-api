package com.example.loan.service;

import com.example.loan.dto.LoanRequestDto;
import com.example.loan.dto.LoanResponseDto;
import com.example.loan.dto.PaymentRequestDto;

public interface LoanService {
    LoanResponseDto applyForLoan(LoanRequestDto loanRequestDto);
    LoanResponseDto getLoanDetails(Long loanId);
    LoanResponseDto makePayment(PaymentRequestDto paymentRequestDto);
}