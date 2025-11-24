package com.example.demo.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String username;
    private String[] roles;
    public LoginResponse(String token, String username, String[] roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

}
