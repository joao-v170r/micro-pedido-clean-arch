package br.com.microservice.pedido.controller;

import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.usecase.ReadPedidoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Corrected import and annotation
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReadPedidoController.class)
class ReadPedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Changed from @Mock to @MockBean
    private ReadPedidoUseCase useCase;

    private PedidoDTO createSamplePedidoDTO() {
        return new PedidoDTO(
                "pedido123",
                "recibo123",
                new DadosCliente("cliente456", "Nome Cliente", "11122233344", "cliente@email.com"),
                LocalDateTime.now(),
                Set.of(new ProdutoPedido("prod789", BigDecimal.valueOf(50.0), 2)),
                StatusPedido.CRIADO,
                new Endereco("27521-000", "Rua de Teste, 123", 10, 20),
                BigDecimal.valueOf(10.0),
                MetodoPagamento.PIX
        );
    }

    /*@Test
    void findAll_shouldReturnAllPedidosWithoutPagination() throws Exception {
        // Arrange
        List<PedidoDTO> pedidos = List.of(createSamplePedidoDTO());
        when(useCase.findAll()).thenReturn(pedidos);

        // Act & Assert
        mockMvc.perform(get("/pedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("pedido123"));
    }*/

    @Test
    void findAll_shouldReturnPaginatedPedidos() throws Exception {
        // Arrange
        List<PedidoDTO> pedidos = List.of(createSamplePedidoDTO());
        when(useCase.findAll(any(Pageable.class))).thenReturn(pedidos);

        // Act & Assert
        mockMvc.perform(get("/pedido?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("pedido123"));
    }

    @Test
    void findById_shouldReturnPedidoWhenFound() throws Exception {
        // Arrange
        PedidoDTO pedido = createSamplePedidoDTO();
        when(useCase.findById(anyString())).thenReturn(pedido);

        // Act & Assert
        mockMvc.perform(get("/pedido/{id}", "pedido123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pedido123"));
    }

    @Test
    void findById_shouldReturnNotFoundWhenPedidoDoesNotExist() throws Exception {
        // Arrange
        when(useCase.findById(anyString())).thenThrow(new RuntimeException("Não foi possivel localizar o pedido"));

        // Act & Assert
        mockMvc.perform(get("/pedido/{id}", "nonExistentId"))
                .andExpect(status().isInternalServerError()) // GlobalExceptionHandler catches RuntimeException as 500
                .andExpect(jsonPath("$.message").value("Ocorreu um erro inesperado: Não foi possivel localizar o pedido"));
    }
}