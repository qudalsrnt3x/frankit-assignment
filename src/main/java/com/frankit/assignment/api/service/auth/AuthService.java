package com.frankit.assignment.api.service.auth;

import com.frankit.assignment.api.service.JwtTokenProvider;
import com.frankit.assignment.api.service.auth.request.LoginServiceRequest;
import com.frankit.assignment.api.service.auth.request.SignupServiceRequest;
import com.frankit.assignment.api.service.auth.response.AuthResponse;
import com.frankit.assignment.domain.common.Snowflake;
import com.frankit.assignment.domain.user.User;
import com.frankit.assignment.domain.user.UserRepository;
import com.frankit.assignment.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Snowflake snowflake;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse signup(SignupServiceRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User createUser = User.of(snowflake.nextId(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                UserRole.CUSTOMER
        );
        User savedUser = userRepository.save(createUser);

        return jwtTokenProvider.generateToken(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginServiceRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(user);
    }

}
