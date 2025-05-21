package br.com.microservice.pedido.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/illegal-argument")
        public void throwIllegalArgument() {
            throw new IllegalArgumentException("argumento inválido");
        }

        @GetMapping("/exception")
        public void throwException() {
            throw new RuntimeException("erro inesperado");
        }
    }
    @Test
    void deveRetornar400ParaIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/illegal-argument"))
                .andExpect(jsonPath("$.path").value("/illegal-argument"));
    }

    @Test
    void deveRetornar422ParaErrosDeValidacao() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/validacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.path").value("/validacao"));
    }

    @Test
    void deveRetornar500ParaExceptionGenerica() throws Exception {
        mockMvc.perform(get("/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.path").value("/exception"));
    }
}