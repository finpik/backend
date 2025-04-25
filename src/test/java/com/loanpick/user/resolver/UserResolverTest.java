package com.loanpick.user.resolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import com.loanpick.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CreateUserMutationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("정상적 요청이 왔을 때 요청에 문제없이 동작한다.")
    @Test
    void createUserMutation() throws Exception {
        // given
        String graphqlQuery = """
                    mutation {
                        createUser(input: {
                            username: "홍길동",
                            dateOfBirth: "1990-01-01",
                            gender: MALE,
                            provider: "KAKAO",
                            id: "123456"
                        }) {
                            username
                        }
                    }
                """;

        Map<String, String> requestBody = Map.of("query", graphqlQuery);
        String jsonContent = objectMapper.writeValueAsString(requestBody);

        // when
        // then
        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
                .andExpect(status().isOk());
    }
}
