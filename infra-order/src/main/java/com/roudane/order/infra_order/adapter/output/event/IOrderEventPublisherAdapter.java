package com.roudane.order.infra_order.adapter.output.event;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.port.output.event.IOrderEventPublisherOutPort;
import org.springframework.stereotype.Component;

@Component
public class IOrderEventPublisherAdapter implements IOrderEventPublisherOutPort {
    /**
     * @param orderModel
     */
    @Override
    public void publisherEventOrder(OrderModel orderModel) {

    }
}
