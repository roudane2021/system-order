package com.roudane.order.infra_notification.transverse.config;

import com.roudane.order.domain_notification.service.NotificationDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {

    @Bean
    public NotificationDomain inventoryDomain() {
        return new NotificationDomain();
    }
}
