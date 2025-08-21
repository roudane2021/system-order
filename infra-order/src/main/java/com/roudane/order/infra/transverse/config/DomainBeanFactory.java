package com.roudane.order.infra.transverse.config;

import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.domain_order.service.OrderDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {
    @Bean
    public OrderDomain orderDomain(final IOrderEventPublisherOutPort orderEventPublisherOutPort
            , final IOrderPersistenceOutPort orderPersistenceOutPort
            , final ILoggerPort loggerPort) {

        return new OrderDomain(orderEventPublisherOutPort, orderPersistenceOutPort, loggerPort);
    }
}
