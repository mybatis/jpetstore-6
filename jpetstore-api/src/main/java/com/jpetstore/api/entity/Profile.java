package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    
    @Id
    @Column(name = "userid")
    private String userId;
    
    @Column(name = "langpref")
    private String languagePreference;
    
    @Column(name = "favcategory")
    private String favouriteCategoryId;
    
    @Column(name = "mylistopt")
    private Boolean listOption;
    
    @Column(name = "banneropt")
    private Boolean bannerOption;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private Account account;
}