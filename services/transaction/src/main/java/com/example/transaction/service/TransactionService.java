//package com.example.transaction.service;
//
//import com.example.transaction.repository.TransactionRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class TransactionService {
//
//    private final TransactionRepository transactionRepository;
//    private final AccountRepository accountRepository;
//    private final AccountService accountService;
//
//    @Transactional
//    public TransactionResponseDto createTransaction(TransactionRequestDto requestDto) {
//        Account sourceAccount = accountRepository.findById(requestDto.getSourceAccountId())
//            .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + requestDto.getSourceAccountId()));
//
//        // Validate transaction type
//        Transaction.TransactionType type = requestDto.getType();
//
//        switch (type) {
//            case DEPOSIT:
//                return handleDeposit(requestDto, sourceAccount);
//            case WITHDRAWAL:
//                return handleWithdrawal(requestDto, sourceAccount);
//            case TRANSFER:
//                return handleTransfer(requestDto, sourceAccount);
//            default:
//                throw new IllegalArgumentException("Invalid transaction type: " + type);
//        }
//    }
//
//    private TransactionResponseDto handleDeposit(TransactionRequestDto requestDto, Account account) {
//        BigDecimal newBalance = account.getBalance().add(requestDto.getAmount());
//
//        // Update account balance
//        accountService.updateBalance(account.getId(), newBalance);
//
//        // Create transaction record
//        Transaction transaction = buildTransaction(
//            requestDto,
//            Transaction.TransactionStatus.COMPLETED,
//            null
//        );
//
//        transaction = transactionRepository.save(transaction);
//
//        return mapToResponseDto(transaction);
//    }
//
//    private TransactionResponseDto handleWithdrawal(TransactionRequestDto requestDto, Account account) {
//        // Check if there are sufficient funds
//        if (account.getBalance().compareTo(requestDto.getAmount()) < 0) {
//            throw new InsufficientFundsException("Insufficient funds in account: " + account.getAccountNumber());
//        }
//
//        BigDecimal newBalance = account.getBalance().subtract(requestDto.getAmount());
//
//        // Update account balance
//        accountService.updateBalance(account.getId(), newBalance);
//
//        // Create transaction record
//        Transaction transaction = buildTransaction(
//            requestDto,
//            Transaction.TransactionStatus.COMPLETED,
//            null
//        );
//
//        transaction = transactionRepository.save(transaction);
//
//        return mapToResponseDto(transaction);
//    }
//
//    private TransactionResponseDto handleTransfer(TransactionRequestDto requestDto, Account sourceAccount) {
//        // Validate destination account
//        if (requestDto.getDestinationAccountId() == null) {
//            throw new IllegalArgumentException("Destination account ID must be provided for transfer");
//        }
//
//        Account destinationAccount = accountRepository.findById(requestDto.getDestinationAccountId())
//            .orElseThrow(() -> new ResourceNotFoundException("Destination account not found with id: " + requestDto.getDestinationAccountId()));
//
//        // Check if there are sufficient funds
//        if (sourceAccount.getBalance().compareTo(requestDto.getAmount()) < 0) {
//            throw new InsufficientFundsException("Insufficient funds in account: " + sourceAccount.getAccountNumber());
//        }
//
//        // Update source account balance
//        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(requestDto.getAmount());
//        accountService.updateBalance(sourceAccount.getId(), newSourceBalance);
//
//        // Update destination account balance
//        BigDecimal newDestBalance = destinationAccount.getBalance().add(requestDto.getAmount());
//        accountService.updateBalance(destinationAccount.getId(), newDestBalance);
//
//        // Create transaction record
//        Transaction transaction = buildTransaction(
//            requestDto,
//            Transaction.TransactionStatus.COMPLETED,
//            destinationAccount.getId()
//        );
//
//        transaction = transactionRepository.save(transaction);
//
//        return mapToResponseDto(transaction);
//    }
//
//    public TransactionResponseDto getTransactionById(Long id) {
//        Transaction transaction = transactionRepository.findById(id)
//            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
//        return mapToResponseDto(transaction);
//    }
//
//    public TransactionResponseDto getTransactionByReference(String reference) {
//        Transaction transaction = transactionRepository.findByTransactionReference(reference)
//            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with reference: " + reference));
//        return mapToResponseDto(transaction);
//    }
//
//    public List<TransactionResponseDto> getTransactionsBySourceAccountId(Long accountId) {
//        return transactionRepository.findBySourceAccountId(accountId)
//            .stream()
//            .map(this::mapToResponseDto)
//            .collect(Collectors.toList());
//    }
//
//    public List<TransactionResponseDto> getTransactionsByDestinationAccountId(Long accountId) {
//        return transactionRepository.findByDestinationAccountId(accountId)
//            .stream()
//            .map(this::mapToResponseDto)
//            .collect(Collectors.toList());
//    }
//
//    public Page<TransactionResponseDto> getAccountTransactions(Long accountId, Pageable pageable) {
//        return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId, pageable)
//            .map(this::mapToResponseDto);
//    }
//
//    private Transaction buildTransaction(TransactionRequestDto requestDto, Transaction.TransactionStatus status, Long destinationAccountId) {
//        return Transaction.builder()
//            .transactionReference(generateTransactionReference())
//            .sourceAccountId(requestDto.getSourceAccountId())
//            .destinationAccountId(destinationAccountId)
//            .amount(requestDto.getAmount())
//            .type(requestDto.getType())
//            .status(status)
//            .description(requestDto.getDescription())
//            .createdAt(LocalDateTime.now())
//            .build();
//    }
//
//    private String generateTransactionReference() {
//        return "TRX" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
//    }
//
//    private TransactionResponseDto mapToResponseDto(Transaction transaction) {
//        return TransactionResponseDto.builder()
//            .id(transaction.getId())
//            .transactionReference(transaction.getTransactionReference())
//            .sourceAccountId(transaction.getSourceAccountId())
//            .destinationAccountId(transaction.getDestinationAccountId())
//            .amount(transaction.getAmount())
//            .type(transaction.getType())
//            .status(transaction.getStatus())
//            .description(transaction.getDescription())
//            .createdAt(transaction.getCreatedAt())
//            .build();
//    }
//}