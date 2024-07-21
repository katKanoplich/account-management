package com.fintech.account_management.repository;

import com.fintech.account_management.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Integer> {
    Auth findByLogin(String login);
    Auth findByPassword(String password);
}
