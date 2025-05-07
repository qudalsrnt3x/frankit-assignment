package com.frankit.assignment.api.service.auth.request;

import lombok.Getter;

@Getter
public class SignupServiceRequest {

    private final String email;
    private final String password;

    private SignupServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignupServiceRequest of(String email, String password) {
        return new SignupServiceRequest(email, password);
    }

}
