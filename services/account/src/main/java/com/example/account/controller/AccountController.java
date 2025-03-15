package com.example.account.controller;

import com.example.account.dto.AccountRequestDto;
import com.example.account.dto.AccountResponseDto;
import com.example.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {


    @Autowired
    private  AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto requestDto) {
        return new ResponseEntity<>(accountService.createAccount(requestDto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<AccountResponseDto>> getActiveAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getActiveAccountsByUserId(userId));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AccountResponseDto> deactivateAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.deactivateAccount(id));
    }
    @PutMapping("/{id}/balance")
    public ResponseEntity<AccountResponseDto> updateBalance(@PathVariable Long id, @RequestBody BigDecimal newBalance) {
        return ResponseEntity.ok(accountService.updateBalance(id, newBalance));
    }
}