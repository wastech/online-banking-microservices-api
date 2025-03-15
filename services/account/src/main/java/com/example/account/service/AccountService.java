package com.example.account.service;

import com.example.account.auth.AuthClient;
import com.example.account.dto.AccountRequestDto;
import com.example.account.dto.AccountResponseDto;
import com.example.account.exception.ResourceNotFoundException;
import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private  AccountRepository accountRepository;

   private final AuthClient authClient;

    @Transactional
    public AccountResponseDto createAccount(AccountRequestDto requestDto) {
        // Check if the user exists in the auth database
        var customer = authClient.findCustomerById(requestDto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getUserId()));

        // Check if user already has an account
        boolean userExists = accountRepository.existsByUserId(requestDto.getUserId());
        if (userExists) {
            throw new IllegalArgumentException("User already has an account.");
        }

        // Generate a unique account number and ensure it doesn't already exist
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        // Create account
        Account account = Account.builder()
            .accountNumber(accountNumber)
            .userId(requestDto.getUserId())
            .accountType(requestDto.getAccountType())
            .balance(requestDto.getInitialDeposit() != null ? requestDto.getInitialDeposit() : BigDecimal.ZERO)
            .currency(requestDto.getCurrency())
            .createdAt(LocalDateTime.now())
            .isActive(true)
            .build();

        account = accountRepository.save(account);

        return mapToResponseDto(account);
    }


    public List<AccountResponseDto> getAllAccounts() {
        return accountRepository.findAll()
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }


    public AccountResponseDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return mapToResponseDto(account);
    }

    public AccountResponseDto getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        return mapToResponseDto(account);
    }

    public List<AccountResponseDto> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    public List<AccountResponseDto> getActiveAccountsByUserId(Long userId) {
        return accountRepository.findByUserIdAndIsActiveTrue(userId)
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponseDto updateBalance(Long accountId, BigDecimal newBalance) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());

        account = accountRepository.save(account);

        return mapToResponseDto(account);
    }

    @Transactional
    public AccountResponseDto deactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.setIsActive(false);
        account.setUpdatedAt(LocalDateTime.now());

        account = accountRepository.save(account);

        return mapToResponseDto(account);
    }

    private String generateAccountNumber() {
        return "AC" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private AccountResponseDto mapToResponseDto(Account account) {
        return AccountResponseDto.builder()
            .id(account.getId())
            .accountNumber(account.getAccountNumber())
            .userId(account.getUserId())
            .accountType(account.getAccountType())
            .balance(account.getBalance())
            .currency(account.getCurrency())
            .createdAt(account.getCreatedAt())
            .isActive(account.getIsActive())
            .build();
    }
}