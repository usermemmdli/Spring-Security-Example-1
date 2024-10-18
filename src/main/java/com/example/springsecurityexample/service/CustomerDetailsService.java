package com.example.springsecurityexample.service;

import com.example.springsecurityexample.dao.entity.Customer;
import com.example.springsecurityexample.dao.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override //bu method authentifikasiya zamani cagrilir. User emaile gore
    // db dan axtarilir eger tapilsa userin datalarini yeni UserDetails return
    // olunur eger tapilmazsa exception atilir.

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.withUsername(customer.getEmail())
                .password(customer.getPassword())
                .authorities("USER") //default rolu user tanimladim.
                // yeni kimki login elese rolu user olacaq
                .build();
    }
}