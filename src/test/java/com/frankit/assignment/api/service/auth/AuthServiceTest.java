package com.frankit.assignment.api.service.auth;

import com.frankit.assignment.api.service.JwtTokenProvider;
import com.frankit.assignment.api.service.auth.request.LoginServiceRequest;
import com.frankit.assignment.api.service.auth.request.SignupServiceRequest;
import com.frankit.assignment.api.service.auth.response.AuthResponse;
import com.frankit.assignment.domain.user.User;
import com.frankit.assignment.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입에 성공하면 토큰이 발급되고 DB에 유저가 저장된다.")
    @Test
    void signupTest() {
        // given
        String email = "test@test.com";
        String password = "test123";
        SignupServiceRequest request = SignupServiceRequest.of(email, password);

        // when
        AuthResponse result = authService.signup(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();

        User findUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(findUser.getEmail()).isEqualTo(email);

        Long userId = jwtTokenProvider.extractUserId(result.getAccessToken());
        assertThat(userId).isEqualTo(findUser.getId());
    }

    @DisplayName("이미 가입된 이메일로 회원가입을 요청하면 예외가 발생한다.")
    @Test
    void signupFailsWhenEmailExists() {
        // given
        String email = "test@test.com";
        String password = "test123";
        SignupServiceRequest request = SignupServiceRequest.of(email, password);
        authService.signup(request);

        // when, then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @DisplayName("이메일과 비밀번호가 일치하면 로그인에 성공하고 토큰이 발급된다.")
    @Test
    void loginTest() {
        // given
        String email = "login@test.com";
        String password = "test123";

        authService.signup(SignupServiceRequest.of(email, password));

        // when
        AuthResponse result = authService.login(LoginServiceRequest.of(email, password));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();

        Long userId = jwtTokenProvider.extractUserId(result.getAccessToken());
        User findUser = userRepository.findById(userId).orElseThrow();
        assertThat(userId).isEqualTo(findUser.getId());
    }

    @DisplayName("존재하지 않는 이메일로 로그인하면 예외가 발생한다.")
    @Test
    void loginFailsWhenUserNotFound() {

        // given
        String email = "login@test.com";
        String password = "test123";

        LoginServiceRequest request = LoginServiceRequest.of(email, password);

        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다.");
    }

    @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다.")
    @Test
    void loginFailsWhenPasswordMismatch() {
        // given
        String email = "login@test.com";
        String password = "test123";
        String wrongPassword = "wrongpass";

        LoginServiceRequest request = LoginServiceRequest.of(email, wrongPassword);
        authService.signup(SignupServiceRequest.of(email, password));

        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다");
    }

}