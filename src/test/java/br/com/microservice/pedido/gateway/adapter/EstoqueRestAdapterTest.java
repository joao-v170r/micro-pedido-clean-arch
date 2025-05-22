///*
//package br.com.microservice.pedido.gateway.adapter;
//
//import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;
//import br.com.microservice.pedido.gateway.dto.ProdutoEstoqueDTO;
//import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
//import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class EstoqueRestAdapterTest {
//
//    @Mock
//    private WebClient estoqueWebClient;
//    @Mock
//    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
//    @Mock
//    private WebClient.RequestBodySpec requestBodySpec;
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    private EstoqueRestAdapter adapter;
//
//    @BeforeEach
//    void setUp() {
//        adapter = new EstoqueRestAdapter(estoqueWebClient);
//    }
//
//    @Test
//    void reduzir_shouldReturnReducedProductsWhenSuccessful() {
//        // Arrange
//        List<DebitoProdutoEstoqueDTO> debitos = List.of(new DebitoProdutoEstoqueDTO("prod1", 1));
//        InputReduzirEstoqueDTO input = new InputReduzirEstoqueDTO(debitos);
//        List<ProdutoEstoqueDTO> expectedOutput = List.of(new ProdutoEstoqueDTO("id1", "prod1", 9));
//
//        when(estoqueWebClient.put()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
//        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToFlux(ProdutoEstoqueDTO.class)).thenReturn(Flux.fromIterable(expectedOutput));
//
//        // Act
//        List<ProdutoEstoqueDTO> result = adapter.reduzir(input);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(expectedOutput.size(), result.size());
//        assertEquals(expectedOutput.get(0).sku(), result.get(0).sku());
//    }
//
//    @Test
//    void reduzir_shouldThrowExceptionWhenApiCallFails() {
//        // Arrange
//        List<DebitoProdutoEstoqueDTO> debitos = List.of(new DebitoProdutoEstoqueDTO("prod1", 1));
//        InputReduzirEstoqueDTO input = new InputReduzirEstoqueDTO(debitos);
//
//        when(estoqueWebClient.put()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
//        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
//        // >>> IMPORTANT: This line was missing or incorrect <<<
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToFlux(ProdutoEstoqueDTO.class)).thenReturn(Flux.empty()); // Simulate no body or error
//
//        // Act & Assert
//        assertThrows(ProdutoRestAdapterExeception.ProdutoNotFound.class, () -> adapter.reduzir(input));
//    }
//
//    @Test
//    void reduzir_shouldThrowExceptionWhenApiReturnsEmptyList() {
//        // Arrange
//        List<DebitoProdutoEstoqueDTO> debitos = List.of(new DebitoProdutoEstoqueDTO("prod1", 1));
//        InputReduzirEstoqueDTO input = new InputReduzirEstoqueDTO(debitos);
//
//        when(estoqueWebClient.put()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
//        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
//        // >>> IMPORTANT: This line was missing or incorrect <<<
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToFlux(ProdutoEstoqueDTO.class)).thenReturn(Flux.fromIterable(Collections.emptyList()));
//
//        // Act & Assert
//        assertThrows(ProdutoRestAdapterExeception.ProdutoNotFound.class, () -> adapter.reduzir(input));
//    }
//}*/
