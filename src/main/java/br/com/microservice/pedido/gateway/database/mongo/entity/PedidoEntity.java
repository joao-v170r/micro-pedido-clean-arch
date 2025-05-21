package br.com.microservice.pedido.gateway.database.mongo.entity;

import br.com.microservice.pedido.domain.value_objects.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection="pedido")
public class PedidoEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    @Field("recibo_pagamento")
    private String reciboPagamento;
    private DadosCliente cliente;
    @Field("dt_cricao")
    private LocalDateTime dataCriacao;
    private List<ProdutoPedido> produtos;
    private StatusPedido status;
    private Endereco endereco;
    private BigDecimal frete;
    @Field("metodo_pagamento")
    private MetodoPagamento metodoPagamento;
}