package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import br.com.microservice.pedido.gateway.ProdutoGateway;
import br.com.microservice.pedido.usecase.mapper.PedidoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadPedidoUseCase {
    private final CrudPedidoGateway gateway;

    public List<PedidoDTO> findAll() {
        return gateway.findAll().stream().map(PedidoMapper::toDTO).toList();
    }

    public List<PedidoDTO> findAll(Pageable page) {
        return gateway.findAll(page).stream().map(PedidoMapper::toDTO).toList();
    }

    public PedidoDTO findById(String id) {
        var opPedido = gateway.findById(id).orElseThrow(() -> new RuntimeException("Não foi possivel localizar o pedido"));
        return PedidoMapper.toDTO(opPedido);
    }
}
