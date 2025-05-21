package br.com.microservice.pedido.domain;

import br.com.microservice.pedido.domain.value_objects.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class Pedido {
    private final String id;
    @Setter
    private String reciboPagamento;
    private final DadosCliente cliente;
    private final LocalDateTime dataCriacao;
    private final Set<ProdutoPedido> produtos;
    @Setter
    private StatusPedido status;
    @Setter
    private Endereco enderecoEntrega;
    private final BigDecimal frete;
    private final MetodoPagamento metodoPagamento;

    private Pedido(
            MetodoPagamento metodoPagamento,
            BigDecimal frete,
            Endereco enderecoEntrega,
            StatusPedido status,
            Set<ProdutoPedido> produtos,
            LocalDateTime dataCriacao,
            DadosCliente cliente,
            String reciboPagamento,
            String id
    ) {
        this.metodoPagamento = metodoPagamento;
        this.frete = validaFrete(frete);
        this.enderecoEntrega = enderecoEntrega;
        this.status = status;
        this.produtos = produtos;
        this.dataCriacao = validaDataCriacao(dataCriacao);
        this.cliente = cliente;
        this.reciboPagamento = reciboPagamento;
        this.id = id;
    }

    public static Pedido criar(
        DadosCliente cliente,
        LocalDateTime dataCriacao,
        Set<ProdutoPedido> produtos,
        Endereco enderecoEntrega,
        BigDecimal frete,
        MetodoPagamento metodoPagamento
    ) {
        return new Pedido(
            metodoPagamento,
            frete,
            enderecoEntrega,
            StatusPedido.CRIADO,
            produtos,
            dataCriacao,
            cliente,
            null,
            null
        );
    }

    public static Pedido reconstruir(
            MetodoPagamento metodoPagamento,
            BigDecimal frete,
            Endereco enderecoEntrega,
            StatusPedido status,
            Set<ProdutoPedido> produtos,
            LocalDateTime dataCriacao,
            DadosCliente cliente,
            String codPagamento,
            String id
    ) {
        return new Pedido(
                metodoPagamento,
                frete,
                enderecoEntrega,
                status,
                produtos,
                dataCriacao,
                cliente,
                codPagamento,
                id
        );
    }

    private BigDecimal validaFrete(BigDecimal frete) {
        if(frete.doubleValue() < 0) {
            throw new IllegalArgumentException("Frete não pode ser negativo");
        }
        return frete;
    }

    private LocalDateTime validaDataCriacao(LocalDateTime dataCriacao) {
        if(dataCriacao.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data de criação do pedido não pode ser uma data futura");
        }
        return dataCriacao;
    }

    public BigDecimal getValorTotal() {
        AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(frete);

        produtos.forEach(produto -> {
            BigDecimal subtotal = produto.preco().multiply(new BigDecimal(produto.quantidade()));
            valorTotal.set(valorTotal.get().add(subtotal));
        });

        return valorTotal.get();
    }
}
