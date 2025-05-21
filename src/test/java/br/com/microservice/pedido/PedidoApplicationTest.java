package br.com.microservice.pedido;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class PedidoApplicationTest {

    @Test
    void main_deveIniciarAplicacaoSemErros() {
        assertDoesNotThrow(() -> PedidoApplication.main(new String[]{}));
    }
}