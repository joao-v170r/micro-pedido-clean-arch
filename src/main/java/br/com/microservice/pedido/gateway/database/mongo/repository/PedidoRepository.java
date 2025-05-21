package br.com.microservice.pedido.gateway.database.mongo.repository;

import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PedidoRepository extends MongoRepository<PedidoEntity, String> {
    List<PedidoEntity> findByStatus(StatusPedido status);
}
