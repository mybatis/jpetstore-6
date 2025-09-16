package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @Column(name = "productid")
    private String productId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "descn")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "catid")
    private Category category;
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Item> items;
}