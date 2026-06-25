package br.org.edu.ifrn.LojaCarro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudAndSecurityFlow() throws Exception {
        // criar carro
        String carroJson = "{\"modelo\":\"TestCar\",\"ano\":2025}";
        var mvcResult = mockMvc.perform(post("/carro/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                .andExpect(status().isOk())
                .andReturn();

        String resp = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(resp);
        assertThat(node.get("modelo").asText()).isEqualTo("TestCar");

        // testar endpoint sem proteção sem token -> deve ser permitido
        mockMvc.perform(get("/secure/ping")).andExpect(status().isOk());

        // obter token
        var loginResp = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"tester\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResp.getResponse().getContentAsString()).get("token").asText();

        // acessar endpoint protegido com token
        mockMvc.perform(get("/secure/ping").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
