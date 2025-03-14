package com.example.account.repository;


import com.example.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserIdAndIsActiveTrue(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByAccountNumber(String accountNumber);
}