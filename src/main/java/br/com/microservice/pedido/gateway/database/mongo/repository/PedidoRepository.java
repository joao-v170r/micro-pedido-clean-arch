package br.com.microservice.pedido.gateway.database.mongo.repository;

import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<PedidoEntity, String> {
}
