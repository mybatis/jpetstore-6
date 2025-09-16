package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    
    @Id
    @Column(name = "itemid")
    private String itemId;
    
    @Column(name = "qty")
    private Integer quantity;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemid", referencedColumnName = "itemid")
    private Item item;
}