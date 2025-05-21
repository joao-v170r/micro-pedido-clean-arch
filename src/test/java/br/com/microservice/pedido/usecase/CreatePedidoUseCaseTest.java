package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.*;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.gateway.ClienteGateway;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import br.com.microservice.pedido.gateway.PagamentoGateway;
import br.com.microservice.pedido.gateway.dto.*;
import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePedidoUseCaseTest {

    @Mock
    private PagamentoGateway gatewayPagamento;

    @Mock
    private ClienteGateway gatewayCliente;

    @Mock
    private CrudPedidoGateway gateway;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private CreatePedidoUseCase useCase;

    @Test
    void create_deveCriarPedidoComSucesso() {
        // Arrange
        String clienteId = "123";

        Set<Endereco> enderecos = Set.of(
                new Endereco("27521-000", "123", 100, 100)
        );

        Set<Telefone> telefones = Set.of(
                new Telefone("999999999", "24")
        );

        OutputClienteDTO clienteDTO = new OutputClienteDTO(
                clienteId,
                "Cliente Teste",
                "12345678900",
                "teste@email.com",
                LocalDate.now(),
                enderecos,
                telefones
        );

        when(gatewayCliente.findById(clienteId)).thenReturn(clienteDTO);

        Pedido pedido = Pedido.reconstruir(
                MetodoPagamento.PIX,           // metodoPagamento
                BigDecimal.TEN,                // frete
                new Endereco("27521-000", "123", 100, 100),  // enderecoEntrega
                StatusPedido.CRIADO,           // status
                Set.of(new ProdutoPedido("prod1", BigDecimal.TEN, 1)),  // produtos
                LocalDateTime.now(),           // dataCriacao
                new DadosCliente(              // cliente
                        clienteId,
                        "Cliente Teste",
                        "12345678900",
                        "teste@email.com"
                ),
                "789",                         // codPagamento
                "456"                          // id
        );
        when(gateway.save(any(Pedido.class))).thenReturn(pedido);

        OutputPagamentoDTO pagamentoDTO = new OutputPagamentoDTO(
                "789",
                "APROVADO",
                BigDecimal.TEN,
                MoedaPagamento.BRL,
                br.com.microservice.pedido.gateway.dto.MetodoPagamento.PIX,
                "urlPagamento",
                null,
                StatusPagamento.CONCLUIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "cod"
        );
        when(gatewayPagamento.solicitaPagamento(any(InputSolicitaPagamentoDTO.class))).thenReturn(pagamentoDTO);

        InputCreatePedidoDTO input = new InputCreatePedidoDTO(
                clienteId,
                LocalDateTime.now(),
                new HashMap<String, Integer>(),
                new Endereco(
                        "27521-000",
                        "123",
                        100,
                        100
                ),
                BigDecimal.TEN,
                MetodoPagamento.PIX
        );

        // Act
        PedidoDTO result = useCase.create(input);

        // Assert
        assertNotNull(result);
        assertEquals("456", result.id());
        assertEquals("789", result.reciboPagamento());
    }
}