package com.frankit.assignment.api.controller.auth.request;

import com.frankit.assignment.api.service.auth.request.SignupServiceRequest;
import com.frankit.assignment.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "권한은 필수입니다.")
    private UserRole userRole;

    public SignupServiceRequest toServiceDto() {
        return SignupServiceRequest.of(email, password, userRole);
    }

}
