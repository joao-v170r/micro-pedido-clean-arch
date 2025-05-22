//package br.com.microservice.pedido.usecase;
//
//import br.com.microservice.pedido.domain.Pedido;
//import br.com.microservice.pedido.domain.enums.StatusPedido;
//import br.com.microservice.pedido.domain.value_objects.*;
//import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
//import br.com.microservice.pedido.dto.PedidoDTO;
//import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
//import br.com.microservice.pedido.gateway.*;
//import br.com.microservice.pedido.gateway.dto.*;
//import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
//import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
//import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;
//import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
//import br.com.microservice.pedido.gateway.dto.output.OutputProdutoDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CreatePedidoUseCaseTest {
//
//    @Mock
//    private PagamentoGateway gatewayPagamento;
//
//    @Mock
//    private ClienteGateway gatewayCliente;
//
//    @Mock
//    private EstoqueGateway gatewayEstoque;
//
//    @Mock
//    private ProdutoGateway gatewayProduto;
//
//    @Mock
//    private CrudPedidoGateway gateway;
//
//    @Mock
//    private ObjectMapper mapper;
//
//    @InjectMocks
//    private CreatePedidoUseCase useCase;
//
//    @Test
//    void create_deveCriarPedidoComSucesso() {
//        // Arrange
//        String clienteId = "123";
//        String produtoId = "prod1";
//        LocalDateTime dataCriacao = LocalDateTime.now();
//
//        // Mock do produto
//        Map<String, Integer> produtos = new HashMap<>();
//        produtos.put(produtoId, 1);
//
//        OutputProdutoDTO produtoDTO = new OutputProdutoDTO(
//                produtoId,
//                "Produto Teste",
//                BigDecimal.TEN,
//                10,
//                "Descrição",
//                LocalDateTime.now()
//        );
//        when(gatewayProduto.list(any())).thenReturn(List.of(produtoDTO));
//
//        // Mock do cliente
//        OutputClienteDTO clienteDTO = new OutputClienteDTO(
//                clienteId,
//                "Cliente Teste",
//                "12345678900",
//                "teste@email.com",
//                LocalDate.now(),
//                Set.of(new Endereco("12345-678", "Rua Teste", 1, 1)),
//                Set.of(new Telefone("999999999", "24"))
//        );
//        when(gatewayCliente.findById(clienteId)).thenReturn(clienteDTO);
//
//        // Mock do pedido
//        Pedido pedido = Pedido.reconstruir(
//                MetodoPagamento.PIX,
//                BigDecimal.TEN,
//                new Endereco("12345-678", "Rua Teste", 1, 1),
//                StatusPedido.CRIADO,
//                Set.of(new ProdutoPedido(produtoId, BigDecimal.TEN, 1)),
//                dataCriacao,
//                new DadosCliente(
//                        clienteId,
//                        "Cliente Teste",
//                        "12345678900",
//                        "teste@email.com"
//                ),
//                "789",
//                "456"
//        );
//        when(gateway.save(any(Pedido.class))).thenReturn(pedido);
//
//        // Mock do pagamento
//        OutputPagamentoDTO pagamentoDTO = new OutputPagamentoDTO(
//                "789",
//                "APROVADO",
//                BigDecimal.TEN,
//                MoedaPagamento.BRL,
//                br.com.microservice.pedido.gateway.dto.MetodoPagamento.PIX,
//                "urlPagamento",
//                null,
//                StatusPagamento.CONCLUIDO,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                "cod"
//        );
//        when(gatewayPagamento.solicitaPagamento(any(InputSolicitaPagamentoDTO.class)))
//                .thenReturn(pagamentoDTO);
//
//        // Mock redução estoque
//        doNothing().when(gatewayEstoque).reduzir(any(InputReduzirEstoqueDTO.class));
//
//        // Input
//        InputCreatePedidoDTO input = new InputCreatePedidoDTO(
//                clienteId,
//                dataCriacao,
//                produtos,
//                new Endereco("12345-678", "Rua Teste", 1, 1),
//                BigDecimal.TEN,
//                MetodoPagamento.PIX
//        );
//
//        // Act
//        PedidoDTO result = useCase.create(input);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("456", result.id());
//        assertEquals("789", result.reciboPagamento());
//
//        // Verify
//        verify(gatewayCliente).findById(clienteId);
//        verify(gatewayProduto).list(any());
//        verify(gateway, times(2)).save(any(Pedido.class));
//        verify(gatewayPagamento).solicitaPagamento(any(InputSolicitaPagamentoDTO.class));
//        verify(gatewayEstoque).reduzir(any(InputReduzirEstoqueDTO.class));
//    }
//}