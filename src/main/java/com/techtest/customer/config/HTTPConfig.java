package com.techtest.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Build and provides a pooling enabled HTTP client for the application.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client")
public class HTTPConfig {

    private int connMaxPoolSize;
    private int connMaxPerRoute;

    @Bean
    public HttpClient HttpClient() {

        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(connMaxPoolSize);
        poolingConnectionManager.setDefaultMaxPerRoute(connMaxPerRoute);

        // Configure and build the HTTP Client.
        return HttpClientBuilder.create()
                .setConnectionManager(poolingConnectionManager)
                .build();
    }
}
