package com.techtest.customer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Holds the endpoint configurations for customer profile service.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "endpoint")
public class EndpointConfig {
    
    private String crmCreateCustomerEndpoint;
    private String crmDeleteCustomerEndpoint;
    private String crmReadCustomerEndpoint;
    private String crmUpdateCustomerEndpoint;

}
