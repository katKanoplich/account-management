package com.fintech.account_management.controller;

import com.fintech.account_management.model.Account;
import com.fintech.account_management.service.AdminAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    public AdminAccountController(AdminAccountService adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping
//    public ResponseEntity<List<Account>> getAllAccounts() {
//        List<Account> accounts = adminAccountService.getAllAccounts();
//        return ResponseEntity.ok(accounts);
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Account> getAllAccounts() {
        try {
            return adminAccountService.getAllAccounts();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{accountId}/block")
    public ResponseEntity<?> blockAccount(@PathVariable Long accountId) {
        try {
            adminAccountService.blockAccount(accountId);
            return ResponseEntity.ok("Account is blocked");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{accountId}/unblock")
    public ResponseEntity<?> unblockAccount(@PathVariable Long accountId) {
        try {
            adminAccountService.unblockAccount(accountId);
            return ResponseEntity.ok("Account is unblocked");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}