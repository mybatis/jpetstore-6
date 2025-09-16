package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "lineitem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LineItemId.class)
public class LineItem {
    
    @Id
    @Column(name = "orderid")
    private Integer orderId;
    
    @Id
    @Column(name = "linenum")
    private Integer lineNumber;
    
    @Column(name = "itemid")
    private String itemId;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "unitprice")
    private BigDecimal unitPrice;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemid", insertable = false, updatable = false)
    private Item item;
}
