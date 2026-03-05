package com.roudane.order.infra.persistence.repository;

import com.roudane.order.infra.persistence.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OutboxEntity o WHERE o.processed = false")
    List<OutboxEntity> findUnprocessedForUpdate();
}
