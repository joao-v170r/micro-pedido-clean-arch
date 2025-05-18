package br.com.microservice.pedido.gateway.exception.adapter;

public class ProdutoRestAdapterExeception {
    public static class ProdutoNotFound extends RuntimeException{
        public ProdutoNotFound(String message, Throwable cause) {
            super(message, cause);
        }
        public ProdutoNotFound(String message) {
            super(message);
        }
    }
}
