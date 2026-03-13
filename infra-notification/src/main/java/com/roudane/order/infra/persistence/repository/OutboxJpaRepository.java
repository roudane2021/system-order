package com.roudane.order.infra.persistence.repository;

import com.roudane.order.infra.persistence.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {

    @Query(value = """
       SELECT o.* FROM outbox o
        WHERE
        o.status = 'NEW'
        OR ( o.status = 'ERROR' AND o.retry_count < :maxRetries
                AND (
                    o.last_attempt_at IS NULL
                    OR o.last_attempt_at < SYSTIMESTAMP - NUMTODSINTERVAL(:delay, 'SECOND')
                )
            )
        ORDER BY o.created_at
        FETCH FIRST :limit ROWS ONLY

        """, nativeQuery = true)
    List<OutboxEntity> findNextNewEvents(@Param("limit") int limit, @Param("maxRetries") int maxRetries,
                                         @Param("delay") int delay);

    @Modifying
    @Query(value = """
        UPDATE outbox
        SET status = 'PROCESSING'
        WHERE id IN (:ids) AND status = 'NEW'
        """, nativeQuery = true)
    int lockEvents(@Param("ids") List<Long> ids);

    @Modifying
    @Query(value = """
        UPDATE outbox
        SET status = :status,
            sent_at = CASE WHEN :status = 'SENT' THEN CURRENT_TIMESTAMP ELSE NULL END
        WHERE id = :id
        """, nativeQuery = true)
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
