package br.com.microservice.pedido.controller;

import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.rest_controller.RequestCreatePedidoController;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.usecase.CreatePedidoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreatePedidoController.class)
class CreatePedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatePedidoUseCase useCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPedidoERetornar201() throws Exception {
        // Arrange
        RequestCreatePedidoController request = new RequestCreatePedidoController(
                "123",
                LocalDateTime.now(),
                Set.of("prod1", "prod2"),
                new Endereco("27521000", "123", 100, 100),
                new BigDecimal("15.50"),
                MetodoPagamento.PIX
        );

        PedidoDTO pedidoDTO = new PedidoDTO(
            "1",
                "recibo",
                new DadosCliente("123", "nome", "000", "email"),
                LocalDateTime.now(),
                Set.of(new ProdutoPedido("1", BigDecimal.valueOf(10), 10), new ProdutoPedido("2", BigDecimal.valueOf(15), 15)),
                StatusPedido.FINALIZADO,
                new Endereco("27521000", "123", 100, 100),
                BigDecimal.valueOf(10),
                MetodoPagamento.PIX
        );

        when(useCase.create(ArgumentMatchers.any(InputCreatePedidoDTO.class)))
                .thenReturn(pedidoDTO);

        // Act & Assert
        mockMvc.perform(post("/create-pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}