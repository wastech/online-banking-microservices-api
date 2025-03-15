package com.example.transaction.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(
    name = "account-service",
    url = "${application.config.account-url}"
)
public interface AccountClient {
    @GetMapping("/{account-id}")
    Optional<AccountResponse> findAccountById(@PathVariable("account-id") Long accountId);

    @PutMapping("/{account-id}/balance")
    void updateBalance(@PathVariable("account-id") Long accountId, @RequestBody BigDecimal newBalance);
}
