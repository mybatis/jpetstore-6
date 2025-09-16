package com.jpetstore.api.repository;

import com.jpetstore.api.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    
    List<Item> findByProductProductId(String productId);
    
    @Query("SELECT CASE WHEN i.quantity > 0 THEN true ELSE false END FROM Inventory i WHERE i.itemId = :itemId")
    boolean isItemInStock(@Param("itemId") String itemId);
}