package com.roudane.order.domain_notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationModel {
    private Long id;
    private Long orderId;
    private Long customerId;
    private String message;
    private NotificationStatus status; // ENUM: SENT, FAILED, PENDING
    private LocalDateTime sentDate;


}
