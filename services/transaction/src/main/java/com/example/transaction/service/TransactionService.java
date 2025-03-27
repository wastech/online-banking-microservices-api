package com.example.transaction.service;

import com.example.transaction.account.AccountClient;
import com.example.transaction.account.AccountResponse;
import com.example.transaction.dto.TransactionRequestDto;
import com.example.transaction.dto.TransactionResponseDto;
import com.example.transaction.exception.InsufficientFundsException;
import com.example.transaction.exception.ResourceNotFoundException;
import com.example.transaction.model.Transaction;
import com.example.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final TransactionEventPublisher transactionEventPublisher;

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
        var sourceAccount = accountClient.findAccountById(requestDto.getSourceAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + requestDto.getSourceAccountId()));

        Transaction.TransactionType type = requestDto.getType();
        TransactionResponseDto responseDto;

        switch (type) {
            case DEPOSIT:
                responseDto = handleDeposit(requestDto, sourceAccount);
                break;
            case WITHDRAWAL:
                responseDto = handleWithdrawal(requestDto, sourceAccount);
                break;
            case TRANSFER:
                responseDto = handleTransfer(requestDto, sourceAccount);
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        // Publish transaction event after successful transaction
        Transaction transaction = transactionRepository.findByTransactionReference(responseDto.getTransactionReference())
            .orElseThrow(() -> new RuntimeException("Transaction not found after creation"));
        transactionEventPublisher.publishTransactionEvent(transaction);

        return responseDto;
    }
    private TransactionResponseDto handleDeposit(TransactionRequestDto requestDto, AccountResponse account) {
        BigDecimal newBalance = account.balance().add(requestDto.getAmount());

        // Update account balance
        updateAccountBalance(account.id(), newBalance);

        // Create transaction record
        Transaction transaction = buildTransaction(
            requestDto,
            Transaction.TransactionStatus.COMPLETED,
            null
        );

        transaction = transactionRepository.save(transaction);

        return mapToResponseDto(transaction);
    }

    private TransactionResponseDto handleWithdrawal(TransactionRequestDto requestDto, AccountResponse account) {
        // Check if there are sufficient funds
        if (account.balance().compareTo(requestDto.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + account.accountNumber());
        }

        BigDecimal newBalance = account.balance().subtract(requestDto.getAmount());

        // Update account balance
        updateAccountBalance(account.id(), newBalance);

        // Create transaction record
        Transaction transaction = buildTransaction(
            requestDto,
            Transaction.TransactionStatus.COMPLETED,
            null
        );

        transaction = transactionRepository.save(transaction);

        return mapToResponseDto(transaction);
    }

    private TransactionResponseDto handleTransfer(TransactionRequestDto requestDto, AccountResponse sourceAccount) {
        // Validate destination account
        if (requestDto.getDestinationAccountId() == null) {
            throw new IllegalArgumentException("Destination account ID must be provided for transfer");
        }

        AccountResponse destinationAccount = accountClient.findAccountById(requestDto.getDestinationAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Destination account not found with id: " + requestDto.getDestinationAccountId()));

        // Check if there are sufficient funds
        if (sourceAccount.balance().compareTo(requestDto.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + sourceAccount.accountNumber());
        }

        // Update source account balance
        BigDecimal newSourceBalance = sourceAccount.balance().subtract(requestDto.getAmount());
        updateAccountBalance(sourceAccount.id(), newSourceBalance);

        // Update destination account balance
        BigDecimal newDestBalance = destinationAccount.balance().add(requestDto.getAmount());
        updateAccountBalance(destinationAccount.id(), newDestBalance);

        // Create transaction record
        Transaction transaction = buildTransaction(
            requestDto,
            Transaction.TransactionStatus.COMPLETED,
            destinationAccount.id()
        );

        transaction = transactionRepository.save(transaction);

        return mapToResponseDto(transaction);
    }

    // Method to update account balance through the client
    private void updateAccountBalance(Long accountId, BigDecimal newBalance) {
        accountClient.updateBalance(accountId, newBalance);
    }
    public TransactionResponseDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return mapToResponseDto(transaction);
    }

    public TransactionResponseDto getTransactionByReference(String reference) {
        Transaction transaction = transactionRepository.findByTransactionReference(reference)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with reference: " + reference));
        return mapToResponseDto(transaction);
    }

    public List<TransactionResponseDto> getTransactionsBySourceAccountId(Long accountId) {
        return transactionRepository.findBySourceAccountId(accountId)
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> getTransactionsByDestinationAccountId(Long accountId) {
        return transactionRepository.findByDestinationAccountId(accountId)
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    public Page<TransactionResponseDto> getAccountTransactions(Long accountId, Pageable pageable) {
        return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId, pageable)
            .map(this::mapToResponseDto);
    }

    private Transaction buildTransaction(TransactionRequestDto requestDto, Transaction.TransactionStatus status, Long destinationAccountId) {
        return Transaction.builder()
            .transactionReference(generateTransactionReference())
            .sourceAccountId(requestDto.getSourceAccountId())
            .destinationAccountId(destinationAccountId)
            .amount(requestDto.getAmount())
            .type(requestDto.getType())
            .status(status)
            .description(requestDto.getDescription())
            .createdAt(LocalDateTime.now())
            .build();
    }

    private String generateTransactionReference() {
        return "TRX" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private TransactionResponseDto mapToResponseDto(Transaction transaction) {
        return TransactionResponseDto.builder()
            .id(transaction.getId())
            .transactionReference(transaction.getTransactionReference())
            .sourceAccountId(transaction.getSourceAccountId())
            .destinationAccountId(transaction.getDestinationAccountId())
            .amount(transaction.getAmount())
            .type(transaction.getType())
            .status(transaction.getStatus())
            .description(transaction.getDescription())
            .createdAt(transaction.getCreatedAt())
            .build();
    }
}