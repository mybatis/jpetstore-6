package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonBackReference
    private Category category;
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Item> items;
}