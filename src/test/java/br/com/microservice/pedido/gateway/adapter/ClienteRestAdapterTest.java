package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.Telefone;
import br.com.microservice.pedido.gateway.dto.OutputClienteDTO;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteRestAdapterTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ClienteRestAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ClienteRestAdapter(webClient);
    }

    @Test
    void findById_deveRetornarClienteQuandoExistir() {
        // Arrange
        String clienteId = "123";

        Set<Endereco> enderecos = Set.of(
                new Endereco("27521-000", "123", 100, 100)
        );

        Set<Telefone> telefones = Set.of(
                new Telefone("999999999", "24")
        );
        OutputClienteDTO clienteEsperado = new OutputClienteDTO(
                clienteId,
                "Cliente Teste",
                "12345678900",
                "teste@email.com",
                LocalDate.now(),
                enderecos,
                telefones
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OutputClienteDTO.class)).thenReturn(Mono.just(clienteEsperado));

        // Act
        OutputClienteDTO result = adapter.findById("123");
    }

    @Test
    void findById_deveLancarExcecaoQuandoClienteNaoExistir() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OutputClienteDTO.class)).thenReturn(Mono.empty());

        // Act & Assert
        assertThrows(ProdutoRestAdapterExeception.ProdutoNotFound.class,
                () -> adapter.findById("999"));
    }
}