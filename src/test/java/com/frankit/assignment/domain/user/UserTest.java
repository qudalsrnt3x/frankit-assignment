package com.frankit.assignment.domain.user;

import com.frankit.assignment.domain.common.Snowflake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final Snowflake snowflake = new Snowflake();

    @DisplayName("Snowflake로 생성된 ID를 갖는 User 객체가 정상 생성된다.")
    @Test
    void test() {
        // given
        long generatedId = snowflake.nextId();
        String email = "test@test.com";
        String password = "encoded-password";

        // when
        User user = User.of(generatedId, email, password, UserRole.CUSTOMER);

        // then
        assertThat(user.getId()).isEqualTo(generatedId);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
    }

}