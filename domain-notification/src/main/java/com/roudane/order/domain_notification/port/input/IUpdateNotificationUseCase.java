package com.roudane.order.domain_notification.port.input;

import com.roudane.order.domain_notification.model.NotificationModel;


public interface IUpdateNotificationUseCase {
    NotificationModel updateOrder(final NotificationModel NotificationModel);

}
