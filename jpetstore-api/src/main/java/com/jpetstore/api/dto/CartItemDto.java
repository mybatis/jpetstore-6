package com.jpetstore.api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDto {
    private String itemId;
    private String productId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;
    private String category;
    private String breed;
}
