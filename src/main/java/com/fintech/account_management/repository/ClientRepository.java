package com.fintech.account_management.repository;


import com.fintech.account_management.model.Auth;
import com.fintech.account_management.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, SearchEngineUserByUserAuthentication<Client> {
    Optional<Client> findByAuthentication(Auth auth);
    default Optional<Client> findUserByUserAuthentication(Auth userAuth) {
        return findByAuthentication(userAuth);
    }
    Client findByAuthentication_Login(String login);

}
