package br.com.microservice.pedido.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

class WebClientConfigTest {

    private final WebClientConfig config = new WebClientConfig();
    private final String baseUrlCliente = "http://localhost:8080";
    private final String baseUrlPagamento = "http://localhost:8081";

    @Test
    void webClientBuilder_deveCriarBuilderComHeadersPadrao() {
        WebClient.Builder builder = config.webClientBuilder();

        assertNotNull(builder);
    }

    @Test
    void clienteWebClient_deveCriarWebClientConfigurado() {
        WebClient.Builder builder = config.webClientBuilder();
        WebClient webClient = config.clienteWebClient(builder, baseUrlCliente);

        assertNotNull(webClient);
    }

    @Test
    void pagamentoWebClient_deveCriarWebClientConfigurado() {
        WebClient.Builder builder = config.webClientBuilder();
        WebClient webClient = config.pagamentoWebClient(builder, baseUrlPagamento);

        assertNotNull(webClient);
    }
}