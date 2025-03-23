package com.roudane.order.domain_notification.port.input;

import com.roudane.order.domain_notification.model.NotificationModel;


import java.util.Set;

public interface IListNotificationUseCase {
    Set<NotificationModel> listOrder();

}
