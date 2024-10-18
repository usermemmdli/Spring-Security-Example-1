package com.example.springsecurityexample.service;

import com.example.springsecurityexample.dao.entity.Customer;
import com.example.springsecurityexample.dao.repository.CustomerRepository;
import com.example.springsecurityexample.dto.request.SignUpRequest;
import com.example.springsecurityexample.dto.response.CustomerResponse;
import com.example.springsecurityexample.mapper.CustomerMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    //injectioni chatgpt bele yazdi qurdalamiram
    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer registerCustomer(SignUpRequest signUpRequest) {
        Customer customer = new Customer();
        customer.setName(signUpRequest.getName());
        customer.setSurname(signUpRequest.getSurname());
        customer.setEmail(signUpRequest.getEmail());
        //parolu sifreleyir
        customer.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        customer.setIsActive(true);
        customer.setCreatedAt(Timestamp.from(Instant.now()));

        return customerRepository.save(customer);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElse(null);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::customerToCustomerResponse)
                .collect(Collectors.toList());
    } //data transferi isleyirmi, yoxlamaq ucun yazdim
}