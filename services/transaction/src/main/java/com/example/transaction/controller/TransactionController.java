package com.example.transaction.controller;

import com.example.transaction.dto.TransactionRequestDto;
import com.example.transaction.dto.TransactionResponseDto;
import com.example.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto) {
        return new ResponseEntity<>(transactionService.createTransaction(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<TransactionResponseDto> getTransactionByReference(@PathVariable String reference) {
        return ResponseEntity.ok(transactionService.getTransactionByReference(reference));
    }

    @GetMapping("/source-account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsBySourceAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySourceAccountId(accountId));
    }

    @GetMapping("/destination-account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByDestinationAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByDestinationAccountId(accountId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionResponseDto>> getAccountTransactions(
        @PathVariable Long accountId,
        Pageable pageable) {
        return ResponseEntity.ok(transactionService.getAccountTransactions(accountId, pageable));
    }
}