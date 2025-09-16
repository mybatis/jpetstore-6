package com.jpetstore.api.service;

import com.jpetstore.api.dto.CategoryDto;
import com.jpetstore.api.dto.ProductDto;
import com.jpetstore.api.dto.ItemDto;
import com.jpetstore.api.entity.Category;
import com.jpetstore.api.entity.Product;
import com.jpetstore.api.entity.Item;
import com.jpetstore.api.repository.CategoryRepository;
import com.jpetstore.api.repository.ProductRepository;
import com.jpetstore.api.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    public CatalogService(CategoryRepository categoryRepository, 
                         ProductRepository productRepository, 
                         ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDto> getCategory(String categoryId) {
        return categoryRepository.findById(categoryId)
                .map(this::convertToDto);
    }

    public List<ProductDto> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> getProduct(String productId) {
        return productRepository.findById(productId)
                .map(this::convertToDto);
    }

    public List<ProductDto> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getItemsByProduct(String productId) {
        return itemRepository.findByProductProductId(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ItemDto> getItem(String itemId) {
        return itemRepository.findById(itemId)
                .map(this::convertToDto);
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getName(),
                category.getDescription()
        );
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getCategoryId(),
                product.getCategory().getName()
        );
    }

    private ItemDto convertToDto(Item item) {
        Integer quantity = item.getInventory() != null ? item.getInventory().getQuantity() : 0;
        boolean inStock = quantity > 0;
        
        return new ItemDto(
                item.getItemId(),
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getListPrice(),
                item.getUnitCost(),
                item.getStatus(),
                item.getAttribute1(),
                item.getAttribute2(),
                item.getAttribute3(),
                item.getAttribute4(),
                item.getAttribute5(),
                quantity,
                inStock
        );
    }
}