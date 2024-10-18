package com.example.springsecurityexample.controller;

import com.example.springsecurityexample.dto.response.CustomerResponse;
import com.example.springsecurityexample.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }//data transferi isleyirmi, yoxlamaq ucun yazdim
}