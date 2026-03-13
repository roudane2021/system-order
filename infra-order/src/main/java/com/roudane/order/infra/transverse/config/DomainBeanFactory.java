package com.roudane.order.infra.transverse.config;

import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import com.roudane.order.domain_order.port.output.json.IJsonOutPort;
import com.roudane.order.domain_order.port.output.logger.ILoggerPort;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.domain_order.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.order.domain_order.service.OrderApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanFactory {
    @Bean
    public OrderApplicationService orderDomain(final IOrderPersistenceOutPort orderPersistenceOutPort
            , final ILoggerPort loggerPort
             , final  IJsonOutPort jsonOutPort
            , final IOutBoxPersistenceOutPort outBoxPersistenceOutPort) {

        return new OrderApplicationService(orderPersistenceOutPort, outBoxPersistenceOutPort, loggerPort, jsonOutPort);
    }
}
