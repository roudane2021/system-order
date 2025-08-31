package com.roudane.order.infra.persistence;

import com.roudane.order.domain_order.model.OrderModel;
import com.roudane.order.domain_order.port.output.persistence.IOrderPersistenceOutPort;
import com.roudane.order.infra.persistence.entity.OrderEntity;
import com.roudane.order.infra.persistence.mapper.PersistenceOrderMapper;
import com.roudane.order.infra.persistence.repository.OrderJpaRepository;
import com.roudane.transverse.criteria.CriteriaApplication;
import com.roudane.transverse.criteria.OperatorApplication;
import com.roudane.transverse.exception.NotFoundException;
import com.roudane.transverse.module.PageResult;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class OrderPersistenceAdapter implements IOrderPersistenceOutPort {

    private final OrderJpaRepository orderJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        OrderEntity orderEntity = PersistenceOrderMapper.INSTANCE.toEntity(orderModel);
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
        return PersistenceOrderMapper.INSTANCE.toModel(savedEntity);
    }

    @Override
    public Optional<OrderModel> findOrderById(Long orderID) {
        return orderJpaRepository.findById(orderID)
                .map(PersistenceOrderMapper.INSTANCE::toModel);

    }

    @Override
    public OrderModel getOrder(Long orderID) {
        return findOrderById(orderID)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderID));
    }

    @Override
    public Set<OrderModel> findAllOrders() {
        return orderJpaRepository.findAll().stream()
                .map(PersistenceOrderMapper.INSTANCE::toModel)
                .collect(Collectors.toSet());
    }

    @Override
    public PageResult<OrderModel> findOrderCriteria(final List<CriteriaApplication> criteriaApplications, final int page, final int size) {
        final Specification<OrderEntity> specification = constructCriteria(criteriaApplications);
        PageRequest nextPageRequest = PageRequest.of(page, size);
        final Page<OrderEntity> domainPage = orderJpaRepository.findAll(specification, nextPageRequest);
        return PersistenceOrderMapper.INSTANCE.toPageResult(domainPage);
    }

    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        if (orderModel.getId() == null || !orderJpaRepository.existsById(orderModel.getId())) {
            throw new NotFoundException("Order with ID " + orderModel.getId() + " not found, cannot update."); // Consider a domain specific exception
        }
        OrderEntity orderEntity = PersistenceOrderMapper.INSTANCE.toEntity(orderModel);
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        OrderEntity updatedEntity = orderJpaRepository.save(orderEntity);
        return PersistenceOrderMapper.INSTANCE.toModel(updatedEntity);
    }

    private Specification<OrderEntity> constructCriteria(final List<CriteriaApplication> criteriaApplications) {
        final List<CriteriaApplication> criteriaApplicationsFilter = this.filterCriterias(criteriaApplications);
        return (Root<OrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder criterBuilder) -> {
            //toArray à utilisé
            List<Predicate> predicates = criteriaApplicationsFilter.stream().map(c -> createPredicate(c, root, criterBuilder)).collect(Collectors.toList());
            return criterBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }

    private Predicate createPredicate(CriteriaApplication criteriaApplication, Root<OrderEntity> root, CriteriaBuilder criteriaBuilder) {
        OperatorApplication operator = criteriaApplication.getOperator();
        Object value = transferType(root, criteriaApplication);
        switch (criteriaApplication.getOperator()) {
            case EQUALS:
                return  criteriaBuilder.equal(root.get(criteriaApplication.getName()), value);
            case NOT_EQUALS:
                return  criteriaBuilder.notEqual(root.get(criteriaApplication.getName()), value);
            case LESS_THAN:
                return  criteriaBuilder.lt(root.get(criteriaApplication.getName()), (Number) value);
            case LESS_OR_EQUAL:
                return  criteriaBuilder.le(root.get(criteriaApplication.getName()), (Number) value);
            case GREATER_THAN:
                return  criteriaBuilder.gt(root.get(criteriaApplication.getName()), (Number) value);
            case GREATER_OR_EQUAL:
                return  criteriaBuilder.ge(root.get(criteriaApplication.getName()), (Number) value);
            case IN:
                return  root.get(criteriaApplication.getName()).in(criteriaApplication.getListValue());
            case NOT_IN:
                return criteriaBuilder.not(root.get(criteriaApplication.getName()).in(value));
            case LIKE:
                return  criteriaBuilder.like(root.get(criteriaApplication.getName()), "%"+value+"%");
            default:
                throw new UnsupportedOperationException("Opérateur non pris en charge: " + operator);
        }
    }

    private List<CriteriaApplication> filterCriterias(List<CriteriaApplication> criters) {
        if (CollectionUtils.isEmpty(criters)) {
            return new ArrayList<>();
        }
        return criters.stream().filter(
                criter -> Objects.nonNull(criter) && StringUtils.isNotBlank(criter.getName()) && Objects.nonNull(criter.getValue())
                        && isFieldExists(criter.getName(), getEntityFields(OrderEntity.class))
        ).collect(Collectors.toList());
    }

    private static boolean isFieldExists(String fieldName, List<Field> fields) {
        return fields.stream()
                .anyMatch(field -> StringUtils.equals(fieldName, field.getName()));
    }

    private static  List<Field> getEntityFields(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .collect(Collectors.toList());
    }



    private Object transferType(Root<?> root, CriteriaApplication element) {
        Class<?> cl = root.get(element.getName()).getJavaType();
        if (Integer.class.isAssignableFrom(cl)) {
            return Integer.valueOf(element.getValue().toString());
        } else if (Double.class.isAssignableFrom(cl)) {
            return Double.valueOf(element.getValue().toString());
        } else {
            return element.getValue();
        }

    }


}
