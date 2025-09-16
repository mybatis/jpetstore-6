package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier", referencedColumnName = "suppid")
    private Supplier supplier;
    
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
    @JsonBackReference
    private Product product;
    
    // Removed bidirectional relationship to avoid Hibernate mapping issues
    // Inventory can be fetched separately if needed
}
