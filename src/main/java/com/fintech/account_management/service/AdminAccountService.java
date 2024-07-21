package com.fintech.account_management.service;

import com.fintech.account_management.model.Account;
import com.fintech.account_management.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdminAccountService {
    private final AccountRepository accountRepository;

    public AdminAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        checkAdminAccess();
        return accountRepository.findAll();
    }

    @Transactional
    public void blockAccount(Long accountId) {
        checkAdminAccess();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));
        account.setBlocked(true);
        accountRepository.save(account);
    }

    @Transactional
    public void unblockAccount(Long accountId) {
        checkAdminAccess();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));
        account.setBlocked(false);
        accountRepository.save(account);
    }

    private void checkAdminAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !authentication.getAuthorities().stream()
                .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only authenticated administrators can perform this action.");
        }
    }
}