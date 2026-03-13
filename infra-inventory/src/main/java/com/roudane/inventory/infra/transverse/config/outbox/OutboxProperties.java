package com.roudane.inventory.infra.transverse.config.outbox;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "outbox")
@Data
public class OutboxProperties {
    private int limit;
    private int maxRetries;
    private int delaySeconds;
    private int pollingDelay;
}
