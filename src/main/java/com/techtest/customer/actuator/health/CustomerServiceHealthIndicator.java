package com.techtest.customer.actuator.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * A custom health indicator for customer service micro service.
 */
@Slf4j
@Component
public class CustomerServiceHealthIndicator implements HealthIndicator {

    /**
     * Return the health indicator, ideally this should check a appropriate measurement of a the health
     * of this service.
     * @return Health object with the given status.
     */
    @Override
    public Health health() {
        return Health.up().build();
    }
}
