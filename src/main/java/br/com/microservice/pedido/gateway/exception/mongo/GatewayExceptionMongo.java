package br.com.microservice.pedido.gateway.exception.mongo;

public class GatewayExceptionMongo extends RuntimeException {
    public GatewayExceptionMongo(String message) {
        super(message);
    }

    public GatewayExceptionMongo(String message, Throwable cause) {
        super(message, cause);
    }
}
