package com.jpetstore.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String productId;
    private String name;
    private String description;
    private String categoryId;
    private String categoryName;
}