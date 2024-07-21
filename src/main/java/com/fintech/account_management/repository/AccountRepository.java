package com.fintech.account_management.repository;

import com.fintech.account_management.model.Account;
import com.fintech.account_management.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClient(Client client);
}
