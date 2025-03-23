package com.roudane.order.domain_notification.port.output;



import com.roudane.order.domain_notification.model.NotificationModel;

import java.util.Set;

public interface INotificationPersistenceOutPort {
    NotificationModel updateNotification(final NotificationModel NotificationModel);
    Set<NotificationModel> listNotification();
    NotificationModel getNotification(final Long orderID);
    NotificationModel createNotification(final NotificationModel NotificationModel);
}
