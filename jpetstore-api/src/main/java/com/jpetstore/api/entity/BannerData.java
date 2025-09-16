package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "bannerdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerData {
    
    @Id
    @Column(name = "favcategory")
    private String favoriteCategory;
    
    @Column(name = "bannername")
    private String bannerName;
}
