package com.example.loan.service;

import com.example.loan.client.AccountClient;
import com.example.loan.client.AuthClient;
import com.example.loan.dto.LoanRequestDto;
import com.example.loan.dto.LoanResponseDto;
import com.example.loan.dto.PaymentRequestDto;
import com.example.loan.exception.LoanServiceException;
import com.example.loan.model.Loan;
import com.example.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.02"); // 2% monthly interest rate

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private AccountClient accountClient;

    @Override
    public LoanResponseDto applyForLoan(LoanRequestDto loanRequestDto) {
        // Check if customer exists
        authClient.findCustomerById(loanRequestDto.getAuthId())
            .orElseThrow(() -> new LoanServiceException("Customer not found"));

        // Check if account exists
        accountClient.findAccountById(loanRequestDto.getAccountId())
            .orElseThrow(() -> new LoanServiceException("Account not found"));

        // Create new loan
        Loan loan = new Loan();
        loan.setAuthId(loanRequestDto.getAuthId());
        loan.setAccountId(loanRequestDto.getAccountId());
        loan.setAmount(loanRequestDto.getAmount());
        loan.setTenureMonths(loanRequestDto.getTenureMonths());
        loan.setBalance(loanRequestDto.getAmount());
        loan.setDueDate(LocalDate.now().plusMonths(loanRequestDto.getTenureMonths()));
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        loanRepository.save(loan);

        // Update account balance
        accountClient.updateBalance(loanRequestDto.getAccountId(), loanRequestDto.getAmount());

        // Prepare response
        LoanResponseDto response = new LoanResponseDto();
        response.setLoanId(loan.getId());
        response.setAuthId(loan.getAuthId());
        response.setAccountId(loan.getAccountId());
        response.setAmount(loan.getAmount());
        response.setTenureMonths(loan.getTenureMonths());
        response.setBalance(loan.getBalance());
        response.setDueDate(loan.getDueDate());
        response.setStatus(loan.getStatus());

        return response;
    }

    @Override
    public LoanResponseDto getLoanDetails(Long loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (!loanOpt.isPresent()) {
            throw new LoanServiceException("Loan not found");
        }

        Loan loan = loanOpt.get();
        updateLoanStatus(loan);

        LoanResponseDto response = new LoanResponseDto();
        response.setLoanId(loan.getId());
        response.setAuthId(loan.getAuthId());
        response.setAccountId(loan.getAccountId());
        response.setAmount(loan.getAmount());
        response.setTenureMonths(loan.getTenureMonths());
        response.setBalance(loan.getBalance());
        response.setDueDate(loan.getDueDate());
        response.setStatus(loan.getStatus());
        response.setInterest(calculateInterest(loan));

        return response;
    }

    @Override
    public LoanResponseDto makePayment(PaymentRequestDto paymentRequestDto) {
        Optional<Loan> loanOpt = loanRepository.findById(paymentRequestDto.getLoanId());
        if (!loanOpt.isPresent()) {
            throw new LoanServiceException("Loan not found");
        }

        Loan loan = loanOpt.get();
        BigDecimal paymentAmount = paymentRequestDto.getPaymentAmount();

        if (paymentAmount.compareTo(loan.getBalance()) > 0) {
            throw new LoanServiceException("Payment amount exceeds loan balance");
        }

        loan.setBalance(loan.getBalance().subtract(paymentAmount));
        if (loan.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(Loan.LoanStatus.PAID);
        }

        loanRepository.save(loan);

        // Update account balance
        accountClient.updateBalance(loan.getAccountId(), paymentAmount.negate());

        LoanResponseDto response = new LoanResponseDto();
        response.setLoanId(loan.getId());
        response.setAuthId(loan.getAuthId());
        response.setAccountId(loan.getAccountId());
        response.setAmount(loan.getAmount());
        response.setTenureMonths(loan.getTenureMonths());
        response.setBalance(loan.getBalance());
        response.setDueDate(loan.getDueDate());
        response.setStatus(loan.getStatus());
        response.setInterest(calculateInterest(loan));

        return response;
    }

    private void updateLoanStatus(Loan loan) {
        if (loan.getDueDate().isBefore(LocalDate.now()) && loan.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            loan.setStatus(Loan.LoanStatus.OVERDUE);
        } else if (loan.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(Loan.LoanStatus.PAID);
        } else {
            loan.setStatus(Loan.LoanStatus.ACTIVE);
        }
    }

    private BigDecimal calculateInterest(Loan loan) {
        if (loan.getStatus() == Loan.LoanStatus.OVERDUE) {
            long overdueMonths = LocalDate.now().until(loan.getDueDate()).toTotalMonths();
            return loan.getAmount().multiply(INTEREST_RATE).multiply(BigDecimal.valueOf(overdueMonths));
        }
        return BigDecimal.ZERO;
    }
}