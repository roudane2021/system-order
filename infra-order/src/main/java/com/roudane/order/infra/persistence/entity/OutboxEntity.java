package com.roudane.order.infra.persistence.entity;

import com.roudane.order.domain_order.model.OutboxModel;
import com.roudane.transverse.enums.OutboxStatus;
import com.roudane.transverse.event.enums.OrderEventType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderEventType eventType;

    @Column(nullable = false)
    @Lob
    private String payload;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column
    @Enumerated(EnumType.STRING)
    private  OutboxStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "error_stacktrace")
    @Lob
    private String errorStacktrace;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        if (this.status == OutboxStatus.SENT) {
            this.sentAt = LocalDateTime.now();
        }
        if (this.status == OutboxStatus.NEW) {
            this.retryCount = 0;
        }
        this.lastAttemptAt = LocalDateTime.now();
    }

    public OutboxModel toModel() {
        return OutboxModel.builder()
                .id(id)
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .eventType(eventType)
                .payload(payload)
                .status(status)
                .createdAt(createdAt)
                .sentAt(sentAt)
                .build();
    }
}
