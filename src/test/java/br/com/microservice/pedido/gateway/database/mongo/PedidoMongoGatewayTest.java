package br.com.microservice.pedido.gateway.database.mongo;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;
import br.com.microservice.pedido.gateway.database.mongo.mapper.PedidoGatewayMapper;
import br.com.microservice.pedido.gateway.database.mongo.repository.PedidoRepository;
import br.com.microservice.pedido.gateway.exception.mongo.GatewayExceptionMongo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoMongoGatewayTest {

    @Mock
    PedidoRepository repository;

    @InjectMocks
    PedidoMongoGateway gateway;

//    @Test
//    void findById_deveRetornarPedido() {
//        PedidoEntity entity = new PedidoEntity();
//        entity.setId("123");
//        Mockito.when(repository.findById("123")).thenReturn(Optional.of(entity));
//
//        Optional<Pedido> result = gateway.findById("123");
//        Assertions.assertTrue(result.isPresent());
//        Assertions.assertEquals("123", result.get().getId());
//    }

    @Test
    void findById_idNuloDeveLancarExcecao() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> gateway.findById(null));
    }

    @Test
    void findById_excecaoRepositoryDeveLancarGatewayException() {
        Mockito.when(repository.findById(any())).thenThrow(new RuntimeException("erro"));
        Assertions.assertThrows(GatewayExceptionMongo.class, () -> gateway.findById("id"));
    }

    @Test
    void existId_deveRetornarTrue() {
        Mockito.when(repository.existsById("id")).thenReturn(true);
        Assertions.assertTrue(gateway.existId("id"));
    }

    @Test
    void existId_idNuloDeveLancarExcecao() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> gateway.existId(null));
    }

//    @Test
//    void findAll_deveRetornarLista() {
//        PedidoEntity entity = new PedidoEntity();
//        entity.setId("1");
//        Mockito.when(repository.findAll()).thenReturn(List.of(entity));
//        List<Pedido> pedidos = gateway.findAll();
//        Assertions.assertEquals(1, pedidos.size());
//    }

    @Test
    void findAll_excecaoRepositoryDeveLancarGatewayException() {
        Mockito.when(repository.findAll()).thenThrow(new RuntimeException());
        Assertions.assertThrows(GatewayExceptionMongo.class, () -> gateway.findAll());
    }

//    @Test
//    void save_deveSalvarPedido() {
//        Pedido pedido = new Pedido();
//        pedido.setId("1");
//        PedidoEntity entity = PedidoGatewayMapper.toEntity(pedido);
//        Mockito.when(repository.save(any())).thenReturn(entity);
//        Pedido salvo = gateway.save(pedido);
//        Assertions.assertEquals("1", salvo.getId());
//    }

    @Test
    void save_pedidoNuloDeveLancarExcecao() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> gateway.save(null));
    }

//    @Test
//    void save_excecaoRepositoryDeveLancarGatewayException() {
//        Pedido pedido = new Pedido();
//        Mockito.when(repository.save(any())).thenThrow(new RuntimeException());
//        Assertions.assertThrows(GatewayExceptionMongo.class, () -> gateway.save(pedido));
//    }

    @Test
    void deleteById_deveDeletar() {
        String id = "1";
        Mockito.when(repository.existsById(id)).thenReturn(true);
        gateway.deleteById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteById_idNaoExisteDeveLancarExcecao() {
        Mockito.when(repository.existsById(any())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> gateway.deleteById("id"));
    }

    @Test
    void deleteById_excecaoRepositoryDeveLancarGatewayException() {
        Mockito.when(repository.existsById(any())).thenReturn(true);
        doThrow(new RuntimeException()).when(repository).deleteById(any());
        Assertions.assertThrows(GatewayExceptionMongo.class, () -> gateway.deleteById("id"));
    }

//    @Test
//    void findAllComPage_deveRetornarLista() {
//        Pageable pageable = PageRequest.of(0, 10);
//        PedidoEntity entity = new PedidoEntity();
//        entity.setId("1");
//        Mockito.when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity)));
//        List<Pedido> pedidos = gateway.findAll(pageable);
//        Assertions.assertEquals(1, pedidos.size());
//    }

    @Test
    void findAllComPage_pageNullDeveLancarExcecao() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> gateway.findAll(null));
    }

    @Test
    void findAllComPage_excecaoRepositoryDeveLancarGatewayException() {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(repository.findAll(pageable)).thenThrow(new RuntimeException());
        Assertions.assertThrows(GatewayExceptionMongo.class, () -> gateway.findAll(pageable));
    }
}