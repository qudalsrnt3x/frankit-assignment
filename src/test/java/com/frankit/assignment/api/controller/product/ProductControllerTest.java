package com.frankit.assignment.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankit.assignment.api.controller.product.request.ProductCreateRequest;
import com.frankit.assignment.api.service.auth.AuthService;
import com.frankit.assignment.api.service.auth.request.SignupServiceRequest;
import com.frankit.assignment.api.service.auth.response.AuthResponse;
import com.frankit.assignment.domain.user.UserRepository;
import com.frankit.assignment.domain.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("토큰이 있으면 상품을 등록할 수 있다.")
    @Test
    void createProductWithToken() throws Exception {
        // given
        String token = getAccessToken(UserRole.ADMIN);

        ProductCreateRequest createRequest = ProductCreateRequest.builder()
                .name("샘플 상품")
                .description("설명입니다")
                .price(BigDecimal.valueOf(5000))
                .deliveryFee(BigDecimal.valueOf(3000))
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("샘플 상품"))
                .andExpect(jsonPath("$.description").value("설명입니다"));
    }

    @DisplayName("토큰이 있으면 상품을 조회할 수 있다.")
    @Test
    void getProductsWithToken() throws Exception {
        // given
        String token = getAccessToken(UserRole.ADMIN);

        mockMvc.perform(get("/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("page", "1")
                        .param("size", "5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @DisplayName("관리자 권한이 아닌 경우 상품을 생성할 경우 예외가 발생한다.")
    @Test
    void createProductWithInvalidRole() throws Exception {
        // given
        String token = getAccessToken(UserRole.CUSTOMER);

        ProductCreateRequest createRequest = ProductCreateRequest.builder()
                .name("샘플 상품")
                .description("설명입니다")
                .price(BigDecimal.valueOf(5000))
                .deliveryFee(BigDecimal.valueOf(3000))
                .build();

        mockMvc.perform(post("/api/v1/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
            ).andExpect(status().isForbidden());
    }

    private String getAccessToken(UserRole userRole) {
        String email = "test@test.com";
        String password = "test1234";

        SignupServiceRequest request = SignupServiceRequest.of(email, password, userRole);
        AuthResponse auth = authService.signup(request);
        return auth.getAccessToken();
    }

}