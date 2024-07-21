package com.fintech.account_management.service;

import com.fintech.account_management.config.JwtService;
import com.fintech.account_management.dto.auth.AuthenticationRequest;
import com.fintech.account_management.dto.auth.AuthenticationResponse;
import com.fintech.account_management.dto.registry.RegisterRequestAdmin;
import com.fintech.account_management.dto.registry.RegisterRequestUser;
import com.fintech.account_management.exceptions.UserAuthenticationException;
import com.fintech.account_management.model.*;
import com.fintech.account_management.repository.AccountRepository;
import com.fintech.account_management.repository.AdminRepository;
import com.fintech.account_management.repository.AuthRepository;
import com.fintech.account_management.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SearchEngineUserRoleByUserAuthentication searchEngineUserRoleByUserAuthentication;
    private final ClientRepository clientRepository;
    private final AuthRepository authRepository;
    private final AdminRepository adminRepository;
    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequestUser request) {


        if (authRepository.findByLogin(request.getAuth().getLogin()) != null)
            throw new UserAuthenticationException("Пользователь с таким логином уже существует");

        var userAuth = new Auth(
                request.getAuth().getLogin(),
                passwordEncoder.encode(request.getAuth().getPassword())
        );
        var account = new Account();
        var client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .authentication(userAuth)
                .role(Role.CLIENT)
//                .account(account)
                .build();

        account.setClient(client);
        authRepository.save(userAuth);
        clientRepository.save(client);
        accountRepository.save(account);

        var jwtToken = jwtService.generateToken(client, client.getRole());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(client)
                .build();
    }

    public AuthenticationResponse registerAdmin(RegisterRequestAdmin request) {


        if (authRepository.findByLogin(request.getAuth().getLogin()) != null)
            throw new UserAuthenticationException("Поставшик с таким логином уже существует");

        var userAuth = new Auth(
                request.getAuth().getLogin(),
                passwordEncoder.encode(request.getAuth().getPassword())
        );

        var admin = Admin.builder()
                .authentication(userAuth)
                .role(Role.ADMIN)
                .build();

        authRepository.save(userAuth);
        adminRepository.save(admin);

        var jwtToken = jwtService.generateToken(admin, admin.getRole());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(admin)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            // Получение роли пользователя
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElseThrow(() -> new UserAuthenticationException("У пользователя нет ролей"));

            String jwtToken;
            if (role.equals("CLIENT")) {
                jwtToken = jwtService.generateToken(userDetails, Role.CLIENT);
            } else if (role.equals("ADMIN")) {
                var admins = adminRepository.findAll();
                jwtToken = jwtService.generateToken(userDetails, Role.ADMIN);
            } else {
                throw new UserAuthenticationException("Недопустимая роль пользователя: " + role);
            }

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .user(userDetails)
                    .build();
        } else {
            throw new UserAuthenticationException("Пользователь не найден");
        }
    }

}