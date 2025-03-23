package com.roudane.order.domain_order.port.output.event;

import com.roudane.order.domain_order.event.OrderEvent;
import com.roudane.order.domain_order.model.OrderModel;

public interface IOrderEventPublisherOutPort {
    void publisherEventOrder(final OrderModel orderModel);
}
