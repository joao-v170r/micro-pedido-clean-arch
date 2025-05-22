package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.dto.ProdutoDTO;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoRestAdapterTest {

    @Mock
    private WebClient produtoWebClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient.UriSpec uriSpec;

    private ProdutoRestAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProdutoRestAdapter(produtoWebClient);
    }

    @Test
    void list_shouldReturnProductsWhenSuccessful() {
        // Arrange
        List<String> productIds = List.of("prod1", "prod2");
        List<ProdutoDTO> expectedProducts = List.of(
                new ProdutoDTO("id1", "Produto A", "prod1", BigDecimal.valueOf(10.0)),
                new ProdutoDTO("id2", "Produto B", "prod2", BigDecimal.valueOf(20.0))
        );

        when(produtoWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProdutoDTO.class)).thenReturn(Flux.fromIterable(expectedProducts));

        // Act
        List<ProdutoDTO> result = adapter.list(productIds);

        // Assert
        assertNotNull(result);
        assertEquals(expectedProducts.size(), result.size());
        assertEquals(expectedProducts.get(0).sku(), result.get(0).sku());
        assertEquals(expectedProducts.get(1).sku(), result.get(1).sku());
    }/*

    @Test
    void list_shouldThrowExceptionWhenApiCallFails() {
        // Arrange
        List<String> productIds = List.of("prod1");

        when(produtoWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProdutoDTO.class)).thenReturn(Flux.empty()); // Simulate no body or error

        // Act & Assert
        assertThrows(ProdutoRestAdapterExeception.ProdutoNotFound.class, () -> adapter.list(productIds));
    }

    @Test
    void list_shouldThrowExceptionWhenApiReturnsEmptyList() {
        // Arrange
        List<String> productIds = List.of("prod1");

        when(produtoWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProdutoDTO.class)).thenReturn(Flux.fromIterable(Collections.emptyList()));

        // Act & Assert
        assertThrows(ProdutoRestAdapterExeception.ProdutoNotFound.class, () -> adapter.list(productIds));
    }*/
}