package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @Column(name = "catid")
    private String categoryId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "descn")
    private String description;
    
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
}