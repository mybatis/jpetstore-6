package com.jpetstore.api.repository;

import com.jpetstore.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByUserId(String userId);
    
    List<Order> findByUserIdOrderByOrderDateDesc(String userId);
}
