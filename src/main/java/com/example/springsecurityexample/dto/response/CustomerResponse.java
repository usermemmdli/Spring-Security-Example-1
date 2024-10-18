package com.example.springsecurityexample.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}