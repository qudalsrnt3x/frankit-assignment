package com.frankit.assignment.api.service.auth.request;

import lombok.Getter;

@Getter
public class LoginServiceRequest {

    private final String email;
    private final String password;

    private LoginServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginServiceRequest of(String email, String password) {
        return new LoginServiceRequest(email, password);
    }

}
