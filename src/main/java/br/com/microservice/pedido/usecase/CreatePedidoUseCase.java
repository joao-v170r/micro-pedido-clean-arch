package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.gateway.ClienteGateway;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import br.com.microservice.pedido.gateway.PagamentoGateway;
import br.com.microservice.pedido.gateway.dto.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.OutputClienteDTO;
import br.com.microservice.pedido.gateway.dto.OutputPagamentoDTO;
import br.com.microservice.pedido.usecase.mapper.PedidoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePedidoUseCase {

    private final PagamentoGateway gatewayPagamento;
    private final ClienteGateway gatewayCliente;
    /*private final ProdutoGateway gatewayProduto;*/
    /*private final ProdutoGateway gatewayProduto;*/
    private final CrudPedidoGateway gateway;
    private final ObjectMapper mapper;

    public PedidoDTO create(InputCreatePedidoDTO input) {
        log.info("<<CREATE.PEDIDO>> inicio endpoint de criação de pedido para o cliente[{}]", input.idCliente());

        Set<ProdutoPedido> produtoPedidos = new HashSet<>();

        log.info("<<CREATE.PEDIDO>> buscando dados de cliente");
        OutputClienteDTO clienteDTO = gatewayCliente.findById(input.idCliente());
        try {
            log.info("<<CREATE.PEDIDO>> dados de cliente recebido: {}", mapper.writeValueAsString(clienteDTO));
        } catch (Exception e) {
            log.error("<<CREATE.PEDIDO>> houve um problema ao criar um json do retorno da busca de cliente");
        }
        DadosCliente dadosCliente = new DadosCliente(
                clienteDTO.id(),
                clienteDTO.nome(),
                clienteDTO.cpf(),
                clienteDTO.email()
        );

        log.info("<<CREATE.PEDIDO>> montando dados de produto");
        input.produtos().forEach(idProduto -> {
            /*OutputProdutoDTO produtoDTO = gatewayProduto.findById(idProduto);*/
            produtoPedidos.add(new ProdutoPedido(
                    "id",//produtoDTO.id(),
                    BigDecimal.ONE,//produtoDTO.preco(),
                    2//produtoDTO.quantidade()
            ));
        });

        Pedido pedido = Pedido.criar(
            dadosCliente,
            input.dataCriacao(),
            produtoPedidos,
            input.enderecoEntrega(),
            input.frete(),
            input.metodoPagamento()
        );

        try {
            log.info("<<CREATE.PEDIDO>> criando pedido: {}", mapper.writeValueAsString(pedido));
        } catch (JsonProcessingException e) {
            log.info("<<CREATE.PEDIDO>> criando pedido. houve um error ao transforma o pedido em json");
        }

        pedido = gateway.save(pedido);

        log.info("<<CREATE.PEDIDO>> solicitando pagamento");
        OutputPagamentoDTO outputPagamento = gatewayPagamento.solicitaPagamento(new InputSolicitaPagamentoDTO(
            dadosCliente.idCliente(),
            pedido.getId(),
            pedido.getMetodoPagamento(),
            pedido.getValorTotal()
        ));

        try {
            log.info("<<CREATE.PEDIDO>> retorno do pagamento: {}", mapper.writeValueAsString(outputPagamento));
        } catch (JsonProcessingException e) {
            log.info("<<CREATE.PEDIDO>> houve um error ao criar um json do retorno do pagamento");
        }

        pedido.setReciboPagamento(outputPagamento.id());

        log.info("<<CREATE.PEDIDO>> fim criação pedido");
        return PedidoMapper.toDTO(gateway.save(pedido));
    }
}