package com.fintech.account_management.service;

import com.fintech.account_management.model.Account;
import com.fintech.account_management.model.Auth;
import com.fintech.account_management.model.Client;
import com.fintech.account_management.repository.AccountRepository;
import com.fintech.account_management.repository.AuthRepository;
import com.fintech.account_management.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class ClientAccountService {
    private final AccountRepository accountRepository;
    private final AuthRepository authRepository;

    private final ClientRepository clientRepository;


    public ClientAccountService(AccountRepository accountRepository, ClientRepository clientRepository, AuthRepository authRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.authRepository = authRepository;
    }

    public List<Account> getAccountsForClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Transactional
    public void deposit(Client client, double amount) {
        checkClientAccess();
        Account account = getAccountForClient(client);
        if (account.isBlocked()) {
            throw new IllegalStateException("Account is locked, deposit not allowed");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
    }
//    public Auth getClientByLogin(String login) {
//        return clientRepository.findByLogin(login);
//    }


    @Transactional
    public void withdraw(Client client, double amount) {
        checkClientAccess();
        Account account = getAccountForClient(client);
        if (account.isBlocked()) {
            throw new IllegalStateException("Account is locked, withdrawal not allowed");
        }
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    private Account getAccountForClient(Client client) {
        List<Account> accounts = getAccountsForClient(client);
        if (accounts.isEmpty()) {
            throw new IllegalStateException("Client has no accounts");
        }
        return accounts.get(0); // Assuming each client has only one account
    }
    private void checkClientAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !authentication.getAuthorities().stream()
                .anyMatch(authority -> "CLIENT".equals(authority.getAuthority()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only authenticated clients can perform this action.");
        }
    }
}