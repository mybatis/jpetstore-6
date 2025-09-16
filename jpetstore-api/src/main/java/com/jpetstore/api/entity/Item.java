package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    
    @Id
    @Column(name = "itemid")
    private String itemId;
    
    @Column(name = "listprice")
    private BigDecimal listPrice;
    
    @Column(name = "unitcost")
    private BigDecimal unitCost;
    
    @Column(name = "supplier")
    private Integer supplierId;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "attr1")
    private String attribute1;
    
    @Column(name = "attr2")
    private String attribute2;
    
    @Column(name = "attr3")
    private String attribute3;
    
    @Column(name = "attr4")
    private String attribute4;
    
    @Column(name = "attr5")
    private String attribute5;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", referencedColumnName = "productid")
    private Product product;
    
    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
    private Inventory inventory;
}