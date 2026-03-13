package com.roudane.transverse.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSentEvent {
    private String notificationId;
    private Long orderId;
    private String customerId;
    private String type;
    private LocalDateTime sentAt;
}
