package finpik.auth.security.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import finpik.auth.controller.AuthResolver;
import finpik.auth.application.TokenUseCase;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthResolver authResolver;

    @MockitoBean
    private TokenUseCase tokenUseCase;

    @DisplayName("/graphql 은 인증없이 접근할 수 있다.")
    @Test
    void permitAllGraphql() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON).content("{\"query\":\"{ hello }\"}")) // 예시
                .andExpect(status().isOk());
    }

    @DisplayName("cors설정이 허용된 origin에서 통과되어야한다.")
    @Test
    void allowedCors() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(options("/graphql").header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")).andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }
}
