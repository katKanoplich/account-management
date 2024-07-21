package com.fintech.account_management.repository;

import com.fintech.account_management.model.Auth;

import java.util.Optional;

public interface SearchEngineUserByUserAuthentication<T> {
    Optional<T> findUserByUserAuthentication(Auth userAuth);
}
