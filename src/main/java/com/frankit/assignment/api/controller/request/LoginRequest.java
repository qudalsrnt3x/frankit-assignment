package com.frankit.assignment.api.controller.request;

import com.frankit.assignment.api.service.auth.request.LoginServiceRequest;
import com.frankit.assignment.api.service.auth.request.SignupServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public LoginServiceRequest toServiceDto() {
        return LoginServiceRequest.of(email, password);
    }

}
