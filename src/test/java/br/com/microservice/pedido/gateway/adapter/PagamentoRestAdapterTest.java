package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.dto.*;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoRestAdapterTest {

    @Mock
    private WebClient pagamentoWebClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private PagamentoRestAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PagamentoRestAdapter(pagamentoWebClient);
    }

    @Test
    void solicitaPagamento_deveRetornarPagamentoQuandoSucesso() {
        // Arrange
        InputSolicitaPagamentoDTO input = new InputSolicitaPagamentoDTO(
                "123",
                "123",
                br.com.microservice.pedido.domain.value_objects.MetodoPagamento.PIX,
                BigDecimal.valueOf(10)
        );

        OutputPagamentoDTO pagamentoEsperado = new OutputPagamentoDTO(
                "789",
                "APROVADO",
                BigDecimal.TEN,
                MoedaPagamento.BRL,
                MetodoPagamento.PIX,
                "urlPagamento",
                null,
                StatusPagamento.CONCLUIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "cod123"
        );

        when(pagamentoWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        doReturn(requestBodySpec).when(requestBodySpec).bodyValue(any());
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OutputPagamentoDTO.class)).thenReturn(Mono.just(pagamentoEsperado));

        // Act
        OutputPagamentoDTO result = adapter.solicitaPagamento(input);

        // Assert
        assertNotNull(result);
        assertEquals("789", result.id());
        assertEquals(StatusPagamento.CONCLUIDO, result.status());
    }

    @Test
    void solicitaPagamento_deveLancarExcecaoQuandoFalha() {
        // Arrange
        InputSolicitaPagamentoDTO input = new InputSolicitaPagamentoDTO(
                "123",
                "123",
                br.com.microservice.pedido.domain.value_objects.MetodoPagamento.PIX,
                BigDecimal.valueOf(10)
        );

        when(pagamentoWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        doReturn(requestBodySpec).when(requestBodySpec).bodyValue(any());
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OutputPagamentoDTO.class))
                .thenThrow(new ProdutoRestAdapterExeception.ProdutoNotFound(""));

        // Act & Assert
        assertThrows(ProdutoRestAdapterExeception.ProdutoNotFound.class,
                () -> adapter.solicitaPagamento(input));
    }
}