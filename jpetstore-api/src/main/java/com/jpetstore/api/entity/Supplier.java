package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    
    @Id
    @Column(name = "suppid")
    private Integer supplierId;
    
    @Column(name = "name", length = 80)
    private String name;
    
    @Column(name = "status", nullable = false, length = 2)
    private String status;
    
    @Column(name = "addr1", length = 80)
    private String address1;
    
    @Column(name = "addr2", length = 80)
    private String address2;
    
    @Column(name = "city", length = 80)
    private String city;
    
    @Column(name = "state", length = 80)
    private String state;
    
    @Column(name = "zip", length = 5)
    private String zip;
    
    @Column(name = "phone", length = 80)
    private String phone;
    
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Item> items;
}
