package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.gateway.*;
import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.ProdutoDTO;
import br.com.microservice.pedido.gateway.dto.StatusPagamento;
import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import br.com.microservice.pedido.usecase.mapper.PedidoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessamentoPedidoUseCase {
    private final PagamentoGateway gatewayPagamento;
    private final CrudPedidoGateway gateway;
    private final ObjectMapper mapper;

    public void processamentoPedidosCriado() {
        List<Pedido> pedidos = gateway.findAllWithStatus(StatusPedido.CRIADO);
        pedidos.forEach(this::processaPedido);
    }

    private PedidoDTO processaPedido(Pedido pedido) {
        log.info("<<PROCESSAMENTO.PEDIDO>> inicio processamento de pedido[{}]", pedido.getId());

        pedido.setStatus(StatusPedido.PROCESSANDO);

        gateway.save(pedido);

        log.info(
                "<<PROCESSAMENTO.PEDIDO>> processa pagamento[{}] do pedido[{}]",
                pedido.getReciboPagamento(),
                pedido.getId()
        );

        OutputPagamentoDTO outputPagamento = gatewayPagamento.processaPagamento(pedido.getReciboPagamento());

        try {
            log.info("<<PROCESSAMENTO.PEDIDO>> retorno do processamento do pagamento: {}", mapper.writeValueAsString(outputPagamento));
        } catch (JsonProcessingException e) {
            log.info("<<PROCESSAMENTO.PEDIDO>> houve um error ao criar um json do retorno do pagamento");
        }

        switch (outputPagamento.status()) {
            case CONCLUIDO -> {
                log.info("<<PROCESSAMENTO.PEDIDO>> pagamento concluido");
                pedido.setStatus(StatusPedido.PROCESSADO);
            }
            case CANCELADO -> {
                log.info("<<PROCESSAMENTO.PEDIDO>> pagamento cancelado");
                pedido.setStatus(StatusPedido.CANCELADO);
            }
            default -> {
                log.info("<<PROCESSAMENTO.PEDIDO>> aguardando pagamento");
                pedido.setStatus(StatusPedido.CRIADO);
            }
        };

        log.info("<<PROCESSAMENTO.PEDIDO>> fim processamento pedido");
        return PedidoMapper.toDTO(gateway.save(pedido));
    }
}
