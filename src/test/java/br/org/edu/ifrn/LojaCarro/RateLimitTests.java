package br.org.edu.ifrn.LojaCarro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"app.rate.limit=3", "app.rate.windowMillis=10000"})
public class RateLimitTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rateLimitBehavior() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/carro")).andExpect(status().isOk());
        }
        mockMvc.perform(get("/carro")).andExpect(status().isTooManyRequests());
    }
}
