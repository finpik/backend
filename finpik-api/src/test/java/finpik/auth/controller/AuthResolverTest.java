//package finpik.auth.controller;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import finpik.auth.application.TokenUseCase;
//import finpik.auth.application.dto.TokenRefreshResultDto;
//import jakarta.servlet.http.Cookie;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class AuthResolverTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private TokenUseCase tokenUseCase;
//
//    @Captor
//    private ArgumentCaptor<String> tokenCaptor;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @DisplayName("ContextValue로 RefreshToken이 주입된다")
//    @Test
//    void refresh() throws Exception {
//        // given
//        String fakeRefreshToken = "previous-refresh-token";
//        String newAccessToken = "new-access-token";
//        String newRefreshToken = "new-refresh-token";
//
//        given(tokenUseCase.refresh(any())).willReturn(new TokenRefreshResultDto(newRefreshToken, newAccessToken));
//
//        String graphqlQuery = "{ \"query\": \"mutation { refresh { accessToken } }\" }";
//
//        // when
//        // then
//        mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON).content(graphqlQuery)
//                .cookie(new Cookie("refreshToken", fakeRefreshToken))).andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.refresh.accessToken").value(newAccessToken));
//
//        verify(tokenUseCase).refresh(tokenCaptor.capture());
//        assertThat(tokenCaptor.getValue()).isEqualTo(fakeRefreshToken);
//    }
//}
