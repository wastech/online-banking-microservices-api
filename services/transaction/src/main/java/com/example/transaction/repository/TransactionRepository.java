package com.example.transaction.repository;


import com.example.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionReference(String reference);
    List<Transaction> findBySourceAccountId(Long accountId);
    List<Transaction> findByDestinationAccountId(Long accountId);
    Page<Transaction> findBySourceAccountIdOrDestinationAccountId(Long sourceId, Long destId, Pageable pageable);
    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}