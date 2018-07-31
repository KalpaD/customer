package com.techtest.customer.config;

import com.techtest.customer.handler.CRMServiceResponseErrorHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Build and provides a RestTemplete to invoke the POX over HTTP services.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("client")
public class RestTemplateConfig {

    private HttpClient httpClient;
    private int connectTimeout;
    private int readTimeout;

    RestTemplateConfig(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new
                HttpComponentsClientHttpRequestFactory(this.httpClient);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeout);
        httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        restTemplate.setErrorHandler(new CRMServiceResponseErrorHandler());

        return restTemplate;
    }
}
