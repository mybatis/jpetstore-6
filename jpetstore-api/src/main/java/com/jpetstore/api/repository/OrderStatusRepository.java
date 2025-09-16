package com.jpetstore.api.repository;

import com.jpetstore.api.entity.OrderStatus;
import com.jpetstore.api.entity.OrderStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, OrderStatusId> {
    
    List<OrderStatus> findByOrderId(Integer orderId);
    
    List<OrderStatus> findByOrderIdOrderByLineNumber(Integer orderId);
}
