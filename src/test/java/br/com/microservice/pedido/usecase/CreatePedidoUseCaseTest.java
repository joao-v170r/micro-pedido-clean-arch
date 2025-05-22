/*
package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.gateway.ClienteGateway;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import br.com.microservice.pedido.gateway.EstoqueGateway;
import br.com.microservice.pedido.gateway.PagamentoGateway;
import br.com.microservice.pedido.gateway.ProdutoGateway;
import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.ProdutoDTO;
import br.com.microservice.pedido.gateway.dto.StatusPagamento;
import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePedidoUseCaseTest {

    @Mock
    private PagamentoGateway gatewayPagamento;

    @Mock
    private ClienteGateway gatewayCliente;

    @Mock
    private EstoqueGateway gatewayEstoque;

    @Mock
    private ProdutoGateway gatewayProduto;

    @Mock
    private CrudPedidoGateway gateway;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private CreatePedidoUseCase useCase;

    private InputCreatePedidoDTO input;
    private OutputClienteDTO clientDTO;
    private ProdutoDTO productDTO;
    private Pedido initialPedido;
    private Pedido savedPedido;
    private OutputPagamentoDTO paymentOutputDTO;

    @BeforeEach
    void setUp() {
        HashMap<String, Integer> produtosMap = new HashMap<>();
        produtosMap.put("prod1", 1);
        produtosMap.put("prod2", 2);

        Endereco endereco = new Endereco("12345-678", "Rua Teste, 123", 1, 1);

        input = new InputCreatePedidoDTO(
                "cliente123",
                LocalDateTime.now().minusDays(1),
                produtosMap,
                endereco,
                BigDecimal.valueOf(10.0),
                MetodoPagamento.PIX
        );

        clientDTO = new OutputClienteDTO(
                "cliente123",
                "Nome Cliente",
                "11122233344",
                "cliente@test.com",
                LocalDate.now().minusYears(30),
                Set.of(endereco),
                Collections.emptySet()
        );

        productDTO = new ProdutoDTO(
                "id_prod1",
                "Produto Teste 1",
                "prod1",
                BigDecimal.valueOf(50.0)
        );
        ProdutoDTO productDTO2 = new ProdutoDTO(
                "id_prod2",
                "Produto Teste 2",
                "prod2",
                BigDecimal.valueOf(25.0)
        );

        Set<ProdutoPedido> produtoPedidos = Set.of(
                new ProdutoPedido("prod1", BigDecimal.valueOf(50.0), 1),
                new ProdutoPedido("prod2", BigDecimal.valueOf(25.0), 2)
        );

        initialPedido = Pedido.criar(
                new DadosCliente(clientDTO.id(), clientDTO.nome(), clientDTO.cpf(), clientDTO.email()),
                input.dataCriacao(),
                produtoPedidos,
                input.enderecoEntrega(),
                input.frete(),
                input.metodoPagamento()
        );

        savedPedido = Pedido.reconstruir(
                initialPedido.getMetodoPagamento(),
                initialPedido.getFrete(),
                initialPedido.getEnderecoEntrega(),
                initialPedido.getStatus(),
                initialPedido.getProdutos(),
                initialPedido.getDataCriacao(),
                initialPedido.getCliente(),
                null,
                "pedidoId456" // Assuming an ID is generated on save
        );
        savedPedido.setReciboPagamento("recibo789");


        paymentOutputDTO = new OutputPagamentoDTO(
                "recibo789",
                "pedidoId456",
                BigDecimal.valueOf(110.0), // 10.0 frete + (50.0 * 1) + (25.0 * 2) = 10 + 50 + 50 = 110
                br.com.microservice.pedido.gateway.dto.MoedaPagamento.BRL,
                br.com.microservice.pedido.gateway.dto.MetodoPagamento.PIX,
                "GatewayTest",
                null,
                StatusPagamento.CONCLUIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "codGateway123"
        );

        when(gatewayCliente.findById(anyString())).thenReturn(clientDTO);
        when(gatewayProduto.list(anyList())).thenReturn(List.of(productDTO, productDTO2));
        when(gateway.save(any(Pedido.class))).thenReturn(savedPedido);
        when(gatewayPagamento.solicitaPagamento(any(InputSolicitaPagamentoDTO.class))).thenReturn(paymentOutputDTO);
        doNothing().when(gatewayEstoque).reduzir(any(InputReduzirEstoqueDTO.class));
        try {
            when(mapper.writeValueAsString(any())).thenReturn("someJson");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void create_shouldCreatePedidoSuccessfully() {
        // Act
        PedidoDTO result = useCase.create(input);

        // Assert
        assertNotNull(result);
        assertEquals("pedidoId456", result.id());
        assertEquals("recibo789", result.reciboPagamento());
        assertEquals(StatusPedido.CRIADO, result.status()); // Status remains CRIADO until processing schedule

        // Verify interactions
        verify(gatewayCliente, times(1)).findById("cliente123");
        verify(gatewayProduto, times(1)).list(List.of("prod1", "prod2"));
        verify(gateway, times(2)).save(any(Pedido.class)); // One for initial save, one for updating reciboPagamento
        verify(gatewayPagamento, times(1)).solicitaPagamento(any(InputSolicitaPagamentoDTO.class));
        verify(gatewayEstoque, times(1)).reduzir(any(InputReduzirEstoqueDTO.class));
    }

    @Test
    void create_shouldHandleJsonProcessingExceptionDuringLoggingForClientDTO() throws JsonProcessingException {
        // Arrange
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(clientDTO);

        // Act
        PedidoDTO result = useCase.create(input);

        // Assert
        assertNotNull(result);
        verify(mapper, times(2)).writeValueAsString(any()); // Still tries to log pedido and payment
        verify(gatewayCliente, times(1)).findById("cliente123");
        verify(gatewayProduto, times(1)).list(anyList());
        verify(gateway, times(2)).save(any(Pedido.class));
        verify(gatewayPagamento, times(1)).solicitaPagamento(any(InputSolicitaPagamentoDTO.class));
        verify(gatewayEstoque, times(1)).reduzir(any(InputReduzirEstoqueDTO.class));
    }

    @Test
    void create_shouldHandleJsonProcessingExceptionDuringLoggingForPedido() throws JsonProcessingException {
        // Arrange
        when(mapper.writeValueAsString(any())).thenReturn("someJson");
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(any(Pedido.class)); // Throw on initial pedido log

        // Act
        PedidoDTO result = useCase.create(input);

        // Assert
        assertNotNull(result);
        verify(mapper, times(2)).writeValueAsString(any()); // Still tries to log clientDTO and payment
        verify(gatewayCliente, times(1)).findById("cliente123");
        verify(gatewayProduto, times(1)).list(anyList());
        verify(gateway, times(2)).save(any(Pedido.class));
        verify(gatewayPagamento, times(1)).solicitaPagamento(any(InputSolicitaPagamentoDTO.class));
        verify(gatewayEstoque, times(1)).reduzir(any(InputReduzirEstoqueDTO.class));
    }

    @Test
    void create_shouldHandleJsonProcessingExceptionDuringLoggingForPayment() throws JsonProcessingException {
        // Arrange
        when(mapper.writeValueAsString(any())).thenReturn("someJson");
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(any(OutputPagamentoDTO.class));

        // Act
        PedidoDTO result = useCase.create(input);

        // Assert
        assertNotNull(result);
        verify(mapper, times(3)).writeValueAsString(any()); // Tries for client, pedido and payment
        verify(gatewayCliente, times(1)).findById("cliente123");
        verify(gatewayProduto, times(1)).list(anyList());
        verify(gateway, times(2)).save(any(Pedido.class));
        verify(gatewayPagamento, times(1)).solicitaPagamento(any(InputSolicitaPagamentoDTO.class));
        verify(gatewayEstoque, times(1)).reduzir(any(InputReduzirEstoqueDTO.class));
    }
}*/
