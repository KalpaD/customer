package com.techtest.customer.custom;

import com.techtest.customer.handler.CRMServiceResponseErrorHandler;
import org.apache.http.client.HttpClient;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Customize the RestTemplate bean with timeouts and response error handler.
 */
public class CRMRestTemplateCustomizer implements RestTemplateCustomizer {

    private HttpClient httpClient;
    private int connectTimeout;
    private int readTimeout;

    public CRMRestTemplateCustomizer(HttpClient httpClient, int connectTimeout, int readTimeout) {
        this.httpClient = httpClient;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new
                HttpComponentsClientHttpRequestFactory(this.httpClient);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeout);
        httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout);

        restTemplate.setErrorHandler(new CRMServiceResponseErrorHandler());
        restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
    }
}
