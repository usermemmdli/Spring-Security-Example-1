package com.example.springsecurityexample.config;

import com.example.springsecurityexample.service.CustomerDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomerDetailsService customerDetailsService;

    public SecurityConfig(CustomerDetailsService customerDetailsService) {
        this.customerDetailsService = customerDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() //CSRF qorumasini disable eleyir. api de jwt istifade olunur deye disable oluna biler
                .authorizeRequests()
                //permitAll bu url ile her kese girisi serbest edir
                .antMatchers("/api/auth/**").permitAll()
                //sadece data gelirmi, api isleyirmi deye yoxlamaq ucun
                .antMatchers("/api/v1/customers").permitAll()
                //asagida ise yerde qalan requestler ucun authentifikasiya teleb edir
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean  //Parolu Bcrypt ile hashleyir
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //userin authentifikasiya islerini manage eleyir
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(customerDetailsService) //userin datalarini almaq ucun
                .passwordEncoder(passwordEncoder).and().build();
    }
}