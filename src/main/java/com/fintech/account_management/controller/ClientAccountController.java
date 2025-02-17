package com.fintech.account_management.controller;

import com.fintech.account_management.model.Auth;
import com.fintech.account_management.model.Client;
import com.fintech.account_management.service.ClientAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientAccountController {

    private final ClientAccountService clientAccountService;

    public ClientAccountController(ClientAccountService clientAccountService) {
        this.clientAccountService = clientAccountService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{clientId}/accounts")
    public ResponseEntity<?> getAccountsForClient(@PathVariable Long clientId) {
        try {
            Client client = clientAccountService.getClientById(clientId);
            return ResponseEntity.ok(clientAccountService.getAccountsForClient(client));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/{clientId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long clientId, @RequestParam double amount) {
        try {
            Client client = clientAccountService.getClientById(clientId);
            clientAccountService.deposit(client, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/{clientId}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long clientId, @RequestParam double amount) {
        try {
            Client client = clientAccountService.getClientById(clientId);
            clientAccountService.withdraw(client, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
