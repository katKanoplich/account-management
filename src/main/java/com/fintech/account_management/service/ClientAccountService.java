package com.fintech.account_management.service;

import com.fintech.account_management.model.Account;
import com.fintech.account_management.model.Client;
import com.fintech.account_management.repository.AccountRepository;
import com.fintech.account_management.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientAccountService {
    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;


    public ClientAccountService(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public List<Account> getAccountsForClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Transactional
    public void deposit(Client client, double amount) {
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


    @Transactional
    public void withdraw(Client client, double amount) {
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
}