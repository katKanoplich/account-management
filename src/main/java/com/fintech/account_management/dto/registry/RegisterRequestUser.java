package com.fintech.account_management.dto.registry;

import com.fintech.account_management.model.Auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestUser {
    private String firstName;
    private String lastName;
    private int age;
    private Auth auth;
}
