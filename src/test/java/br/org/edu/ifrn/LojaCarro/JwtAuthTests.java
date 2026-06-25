package br.org.edu.ifrn.LojaCarro;

import br.org.edu.ifrn.LojaCarro.security.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthTests {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGenerateAndParseJwtToken() {
        String token = jwtService.generateToken("tester", 3600_000);

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);

        var claims = jwtService.parseToken(token).getBody();
        assertThat(claims.getSubject()).isEqualTo("tester");
        assertThat(claims.getExpiration()).isNotNull();
    }

    @Test
    void shouldReturnTokenFromLoginEndpoint() throws Exception {
        var loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"tester\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String json = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(json);

        assertThat(node.has("token")).isTrue();
        assertThat(node.get("token").asText()).isNotBlank();
    }

    @Test
    void shouldAccessSecureEndpointWithBearerToken() throws Exception {
        var loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"tester\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        mockMvc.perform(get("/secure/ping")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
