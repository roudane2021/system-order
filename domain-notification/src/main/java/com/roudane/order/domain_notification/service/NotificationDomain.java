package com.roudane.order.domain_notification.service;

import com.roudane.order.domain_notification.model.NotificationModel;
import com.roudane.order.domain_notification.port.input.ICreateNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IGetNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IListNotificationUseCase;
import com.roudane.order.domain_notification.port.input.IUpdateNotificationUseCase;

import java.util.Set;

public class NotificationDomain implements ICreateNotificationUseCase, IListNotificationUseCase,
        IGetNotificationUseCase, IUpdateNotificationUseCase {
    /**
     * @param NotificationModel
     * @return
     */
    @Override
    public NotificationModel createOrder(NotificationModel NotificationModel) {
        return null;
    }

    /**
     * @param notificationID
     * @return
     */
    @Override
    public NotificationModel getOrder(Long notificationID) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Set<NotificationModel> listOrder() {
        return null;
    }

    /**
     * @param NotificationModel
     * @return
     */
    @Override
    public NotificationModel updateOrder(NotificationModel NotificationModel) {
        return null;
    }
}
