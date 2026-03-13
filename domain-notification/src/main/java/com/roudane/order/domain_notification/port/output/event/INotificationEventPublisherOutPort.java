package com.roudane.order.domain_notification.port.output.event;

import com.roudane.transverse.event.NotificationSentEvent;

public interface INotificationEventPublisherOutPort {
    void publish(NotificationSentEvent event);
}
