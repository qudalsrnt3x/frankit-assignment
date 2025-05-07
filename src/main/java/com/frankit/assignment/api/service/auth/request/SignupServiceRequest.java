package com.frankit.assignment.api.service.auth.request;

import com.frankit.assignment.domain.user.UserRole;
import lombok.Getter;

@Getter
public class SignupServiceRequest {

    private final String email;
    private final String password;
    private final UserRole userRole;

    private SignupServiceRequest(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static SignupServiceRequest of(String email, String password, UserRole userRole) {
        return new SignupServiceRequest(email, password, userRole);
    }

}
