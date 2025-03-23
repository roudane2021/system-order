package com.roudane.order.domain_notification.port.output;

import com.roudane.order.domain_notification.event.NotificationEvent;


public interface INotificationEventPublisherOutPort {
    void publisherEventOrder(final NotificationEvent orderEvent);
}
