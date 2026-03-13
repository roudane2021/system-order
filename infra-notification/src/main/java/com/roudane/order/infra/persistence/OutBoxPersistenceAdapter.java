package com.roudane.order.infra.persistence;

import com.roudane.order.domain_notification.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.order.infra.persistence.entity.OutboxEntity;
import com.roudane.order.infra.persistence.mapper.PersistenceOutBoxMapper;
import com.roudane.order.infra.persistence.repository.OutboxJpaRepository;
import com.roudane.transverse.enums.OutboxStatus;
import com.roudane.transverse.model.OutboxModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutBoxPersistenceAdapter implements IOutBoxPersistenceOutPort {

    private final OutboxJpaRepository outboxJpaRepository;
    private final PersistenceOutBoxMapper persistenceOutBoxMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveEvent(OutboxModel outboxModel) {
        final OutboxEntity outboxEntity = persistenceOutBoxMapper.toEntity(outboxModel);
        outboxJpaRepository.save(outboxEntity);
    }

    @Override
    @Transactional
    public List<OutboxModel> lockNextEvents(int limit, int maxRetries, int delay) {
        List<Long> ids = outboxJpaRepository.findNextNewEvents(limit, maxRetries, delay)
                .stream()
                .map(OutboxEntity::getId)
                .toList();

        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        List<OutboxEntity> locked = entityManager.createNativeQuery("""
            SELECT *
            FROM outbox
            WHERE id IN (:ids)
            FOR UPDATE SKIP LOCKED
            """, OutboxEntity.class)
                .setParameter("ids", ids)
                .getResultList();

        if (CollectionUtils.isEmpty(locked)) {
            return List.of();
        }

        locked.forEach(e -> e.setStatus(OutboxStatus.PROCESSING));
        entityManager.flush();

        return locked.stream()
                .map(OutboxEntity::toModel)
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markAsError(Long id, Exception e) {
        OutboxEntity entity = outboxJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Outbox event not found: " + id));

        entity.setStatus(OutboxStatus.ERROR);
        entity.setRetryCount(entity.getRetryCount() + 1);
        entity.setErrorMessage(e.getMessage());
        entity.setErrorStacktrace(ExceptionUtils.getStackTrace(e));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markAsSent(Long id) {
        OutboxEntity entity = outboxJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Outbox event not found: " + id));

        entity.setStatus(OutboxStatus.SENT);
        entity.setErrorMessage(null);
        entity.setErrorStacktrace(null);
    }
}
