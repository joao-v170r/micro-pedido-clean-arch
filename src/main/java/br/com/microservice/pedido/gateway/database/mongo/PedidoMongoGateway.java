package br.com.microservice.pedido.gateway.database.mongo;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;
import br.com.microservice.pedido.gateway.database.mongo.mapper.PedidoGatewayMapper;
import br.com.microservice.pedido.gateway.database.mongo.repository.PedidoRepository;
import br.com.microservice.pedido.gateway.exception.mongo.GatewayExceptionMongo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoMongoGateway implements CrudPedidoGateway {

    private final PedidoRepository repository;

    @Override
    public Optional<Pedido> findById(String id) {

        if(id == null || id.isBlank()) {
            throw new IllegalArgumentException("id invalido para consulta do gateway");
        };

        try {
            Optional<PedidoEntity> opPedido = repository.findById(id);
            return opPedido.map(PedidoGatewayMapper::toDomain);
        } catch (Exception e) {
            log.error("houve um error ao buscar um pedido, error: {}", e.getMessage());
            throw new GatewayExceptionMongo("error ao buscar um pedido por id", e);
        }
    }

    @Override
    public Boolean existId(String id) {
        if(id == null || id.isBlank()){
            throw new IllegalArgumentException("id invalido para consulta do gateway");
        }
        return repository.existsById(id);
    }

    @Override
    public List<Pedido> findAll() {
        try {
            return repository.findAll().stream().map(PedidoGatewayMapper::toDomain).toList();
        } catch (Exception e) {
            log.error("error ao buscar todos os pedidos, error: {}", e.getMessage());
            throw new GatewayExceptionMongo("erro ao buscar todos os pedidos",e);
        }
    }

    @Override
    public Pedido save(Pedido pedido) {
        if(pedido == null) {
            throw new IllegalArgumentException("objeto pedido esta nulo");
        }
        try {
            PedidoEntity entity = PedidoGatewayMapper.toEntity(pedido);
            return PedidoGatewayMapper.toDomain(repository.save(entity));
        } catch (Exception e) {
            log.error("houve um error ao savar um pedido, error: {}", e.getMessage());
            throw new GatewayExceptionMongo("error ao salvar pedido", e);
        }
    }

    @Override
    public void deleteById(String id) {
        if (!existId(id)) {
            throw new IllegalArgumentException("id do pedido não foi encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("houver um error ao deletar um pedido, error {}", e.getMessage());
            throw new GatewayExceptionMongo("houver um error ao deletar o pedido", e);
        }
    }

    @Override
    public List<Pedido> findAll(Pageable page) {
        if(page == null) {
             throw new IllegalArgumentException("o objeto pagable esta vazio");
        }
        try {
            return repository.findAll(page).map(PedidoGatewayMapper::toDomain).toList();
        } catch (Exception e) {
            log.error("houver um error ao buscar todos os pedidos com filtro de paginação, error {}", e.getMessage());
            throw new GatewayExceptionMongo("houver um error ao buscar o pedido com paginação", e);
        }
    }
}