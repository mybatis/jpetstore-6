package com.jpetstore.api.controller;

import com.jpetstore.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;
    private final AccountRepository accountRepository;
    
    @GetMapping("/database-status")
    public Map<String, Object> getDatabaseStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("categories_count", categoryRepository.count());
        status.put("products_count", productRepository.count());
        status.put("items_count", itemRepository.count());
        status.put("suppliers_count", supplierRepository.count());
        status.put("inventory_count", inventoryRepository.count());
        status.put("accounts_count", accountRepository.count());
        status.put("message", "Database populated successfully!");
        return status;
    }
    
    @GetMapping("/categories")
    public Object getCategories() {
        return categoryRepository.findAll();
    }
    
    @GetMapping("/products")
    public Object getProducts() {
        return productRepository.findAll();
    }
}
