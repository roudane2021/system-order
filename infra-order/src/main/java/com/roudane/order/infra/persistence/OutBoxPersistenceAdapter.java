package com.roudane.order.infra.persistence;

import com.roudane.order.domain_order.port.output.persistence.IOutBoxPersistenceOutPort;
import com.roudane.order.infra.persistence.entity.OutboxEntity;
import com.roudane.order.infra.persistence.mapper.PersistenceOutBoxMapper;
import com.roudane.order.infra.persistence.repository.OutboxJpaRepository;
import com.roudane.transverse.enums.OutboxStatus;
import com.roudane.transverse.model.OutboxModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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
        final OutboxEntity  outboxEntity = persistenceOutBoxMapper.toEntity(outboxModel);
         outboxJpaRepository.save(outboxEntity);
    }

    @Override
    @Transactional
    public List<OutboxModel> lockNextEvents(int limit, int maxRetries, int delay) {

        // 1️ Récupérer les IDs des événements NEW
        List<Long> ids = fetchNewEventIds(limit, maxRetries, delay);
        if (CollectionUtils.isEmpty(ids)) return List.of();

        // 2️ Verrouiller les entités
        List<OutboxEntity> lockedEntities = lockEntities(ids);
        if (CollectionUtils.isEmpty(lockedEntities)) return List.of();

        // 3 Passer en PROCESSING
        markAsProcessing(lockedEntities);

        // 4️ Conversion en modèles métier
        return convertToModels(lockedEntities);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void markAsError(Long id, Throwable e, boolean retryable) {
        OutboxEntity entity = outboxJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Outbox event not found: " + id));

        entity.setStatus(OutboxStatus.ERROR);
        entity.setRetryCount(entity.getRetryCount() + 1);
        entity.setErrorMessage(e.getMessage());
        entity.setErrorStacktrace(ExceptionUtils.getStackTrace(e));
        entity.setRetryable(retryable);

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



    private List<Long> fetchNewEventIds(int limit, int maxRetries, int delay) {
        return outboxJpaRepository.findNextNewEvents(limit, maxRetries, delay)
                .stream()
                .map(OutboxEntity::getId)
                .toList();
    }


    private List<OutboxEntity> lockEntities(List<Long> ids) {
        if (ids.isEmpty()) return List.of();

        return entityManager.createNativeQuery("""
            SELECT *
            FROM outbox
            WHERE id IN (:ids)
            FOR UPDATE SKIP LOCKED
            """, OutboxEntity.class)
                .setParameter("ids", ids)
                .getResultList();
    }


    private void markAsProcessing(List<OutboxEntity> entities) {
        entities.forEach(e -> e.setStatus(OutboxStatus.PROCESSING));
        entityManager.flush(); // forcer l'update avant libération du lock
    }


    private List<OutboxModel> convertToModels(List<OutboxEntity> entities) {
        return entities.stream()
                .map(OutboxEntity::toModel)
                .toList();
    }

}
