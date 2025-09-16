package com.jpetstore.api.controller;

import com.jpetstore.api.dto.CategoryDto;
import com.jpetstore.api.dto.ProductDto;
import com.jpetstore.api.dto.ItemDto;
import com.jpetstore.api.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(catalogService.getAllCategories());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId) {
        return catalogService.getCategory(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(catalogService.getProductsByCategory(categoryId));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        return catalogService.getProduct(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(catalogService.searchProducts(keyword));
    }

    @GetMapping("/products/{productId}/items")
    public ResponseEntity<List<ItemDto>> getItemsByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(catalogService.getItemsByProduct(productId));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable String itemId) {
        return catalogService.getItem(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}