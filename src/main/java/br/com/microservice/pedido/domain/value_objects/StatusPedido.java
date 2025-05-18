package br.com.microservice.pedido.domain.value_objects;

public enum StatusPedido {
    CRIADO,
    PROCESSANDO,
    AGUARDANDO_PAGAMENTO,
    ENVIADO,
    RECEBIDO,
    FINALIZADO,
    CANCELADO
}
