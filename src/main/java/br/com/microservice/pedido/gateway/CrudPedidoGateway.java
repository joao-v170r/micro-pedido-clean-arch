package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.domain.Pedido;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudPedidoGateway {
    Optional<Pedido> findById(String id);
    Boolean existId(String id);
    List<Pedido> findAll();
    Pedido save(Pedido pedido);
    void deleteById(String id);
    List<Pedido> findAll(Pageable page);
}