package br.com.microservice.pedido.gateway.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean(name = "clienteWebClient")
    public WebClient clienteWebClient(
            WebClient.Builder builder,
            @Value("${microservices.cliente.url}") String baseUrl
    ) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(
                    new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(Duration.ofMillis(5000))
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    )
                )
                .filter(
                    (request, next) -> next.exchange(request)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                ).timeout(Duration.ofSeconds(5)))
                .build();
    }

    @Bean(name = "pagamentoWebClient")
    public WebClient pagamentoWebClient(
            WebClient.Builder builder,
            @Value("${microservices.pagamento.url}") String baseUrl
    ) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .responseTimeout(Duration.ofMillis(5000))
                                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        )
                )
                .filter(
                        (request, next) -> next.exchange(request)
                                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                                ).timeout(Duration.ofSeconds(5)))
                .build();
    }

    /*@Bean(name = "produtoWebClient")
    public WebClient produtoWebClient(
            WebClient.Builder builder,
            @Value("${microservices.produto.url}") String baseUrl
    ) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(
                    new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(Duration.ofMillis(5000))
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    )
                )
                .filter(
                    (request, next) -> next.exchange(request)
                        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                ).timeout(Duration.ofSeconds(5)))
                .build();
    }*/
}
