package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.gateway.*;
import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.ProdutoDTO;
import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputProdutoDTO;
import br.com.microservice.pedido.usecase.mapper.PedidoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePedidoUseCase {

    private final PagamentoGateway gatewayPagamento;
    private final ClienteGateway gatewayCliente;
    private final EstoqueGateway gatewayEstoque;
    private final ProdutoGateway gatewayProduto;
    private final CrudPedidoGateway gateway;
    private final ObjectMapper mapper;

    public PedidoDTO create(InputCreatePedidoDTO input) {
        log.info("<<CREATE.PEDIDO>> inicio endpoint de criação de pedido para o cliente[{}]", input.clienteId());

        Set<ProdutoPedido> produtoPedidos = new HashSet<>();

        log.info("<<CREATE.PEDIDO>> buscando dados de cliente");
        OutputClienteDTO clienteDTO = gatewayCliente.findById(input.clienteId());
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
        List<String> idsProduto = new ArrayList<>();
        List<DebitoProdutoEstoqueDTO> inputEstoque = new ArrayList<>();

        input.produtos().forEach((id, quantidade) -> {
            idsProduto.add(id);
            inputEstoque.add(new DebitoProdutoEstoqueDTO(id, quantidade));
        });

        List<ProdutoDTO> produtos = gatewayProduto.list(idsProduto);

        produtos.forEach(produto -> {
            produtoPedidos.add(new ProdutoPedido(
                    produto.sku(),//produtoDTO.id(),
                    produto.preco(),//produtoDTO.preco(),
                    input.produtos().get(produto.sku())//produtoDTO.quantidade()
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

        log.info("<<CREATE.PEDIDO>> reduzir");
        gatewayEstoque.reduzir(new InputReduzirEstoqueDTO(inputEstoque));

        pedido.setReciboPagamento(outputPagamento.id());

        log.info("<<CREATE.PEDIDO>> fim criação pedido");
        return PedidoMapper.toDTO(gateway.save(pedido));
    }
}