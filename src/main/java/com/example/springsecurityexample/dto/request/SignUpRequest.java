package com.example.springsecurityexample.dto.request;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class SignUpRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean isActive;
    private Timestamp createdAt;
}