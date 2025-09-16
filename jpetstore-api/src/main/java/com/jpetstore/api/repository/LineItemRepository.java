package com.jpetstore.api.repository;

import com.jpetstore.api.entity.LineItem;
import com.jpetstore.api.entity.LineItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, LineItemId> {
    
    List<LineItem> findByOrderId(Integer orderId);
    
    List<LineItem> findByItemId(String itemId);
}
