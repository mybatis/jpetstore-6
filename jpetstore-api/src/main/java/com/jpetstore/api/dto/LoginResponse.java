package com.jpetstore.api.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private UserDto user;
    private String message;
}
