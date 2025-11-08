package ru.practicum.telemetry.analyzer.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.*;

@Configuration
@ConfigurationProperties("grpc.client")
public class GrpcConfig {

    @GrpcClient("hub-router")
    private HubRouterControllerBlockingStub hubRouterControllerBlockingStub;

    @Bean
    HubRouterControllerBlockingStub hubRouterClient() {
        return this.hubRouterControllerBlockingStub;
    }
}