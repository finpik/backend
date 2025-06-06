package finpik.sse;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import finpik.jwt.JwtProvider;
import finpik.sse.service.SseEmitterService;
import finpik.user.service.UserService;

@WebMvcTest(SseController.class)
@AutoConfigureMockMvc(addFilters = false)
class SseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SseEmitterService sseEmitterService;

    @MockitoBean
    JwtProvider jwtProvider;

    @MockitoBean
    UserService userService;

    @DisplayName("SSE 연결 시 SseEmitter와 초기 메시지를 반환한다.")
    @Test
    void sseSubscribe() throws Exception {
        // given
        SseEmitter mockEmitter = new SseEmitter(Long.MAX_VALUE);
        given(sseEmitterService.createSseEmitter(1L)).willReturn(mockEmitter);

        // when
        // then
        mockMvc.perform(get("/sse/subscribe").param("profileId", "1")).andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/event-stream;charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("SSE 연결됨"))).andReturn();
    }
}
