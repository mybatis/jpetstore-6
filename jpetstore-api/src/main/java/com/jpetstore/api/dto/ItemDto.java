package com.jpetstore.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String itemId;
    private String productId;
    private String productName;
    private BigDecimal listPrice;
    private BigDecimal unitCost;
    private String status;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private Integer quantity;
    private boolean inStock;
}