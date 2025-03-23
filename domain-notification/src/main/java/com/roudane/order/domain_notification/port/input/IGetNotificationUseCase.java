package com.roudane.order.domain_notification.port.input;


import com.roudane.order.domain_notification.model.NotificationModel;

public interface IGetNotificationUseCase {

    NotificationModel getOrder(final Long notificationID);

}
