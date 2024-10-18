package com.example.springsecurityexample.controller;

import com.example.springsecurityexample.dao.entity.Customer;
import com.example.springsecurityexample.dto.request.LoginRequest;
import com.example.springsecurityexample.dto.request.SignUpRequest;
import com.example.springsecurityexample.dto.response.JwtResponse;
import com.example.springsecurityexample.service.CustomerService;
import com.example.springsecurityexample.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(CustomerService customerService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerCustomer(@RequestBody SignUpRequest signUpRequest) {
        Customer customer = customerService.registerCustomer(signUpRequest);
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        //userin email ve parolunu dogrulamaq ucun
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        //kimlik dogrulandiqdan sonra dogrulanma SecurityContextHolder a elave
        //olunur ve sistemde login olunmus user kimi taninir
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //kimliyi dogrulanan useri emaili ile dbaseden tapir
        Customer customer = customerService.findByEmail(loginRequest.getEmail());

        String accessToken = jwtService.createAccessToken(customer);
        String refreshToken = jwtService.createRefreshToken(customer);
        //JwtResponse classi ile tokenler JSON formatinda return olunur
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }
}