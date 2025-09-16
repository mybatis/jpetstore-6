package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "userid")
    private String username;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "firstname")
    private String firstName;
    
    @Column(name = "lastname")
    private String lastName;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "addr1")
    private String address1;
    
    @Column(name = "addr2")
    private String address2;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip")
    private String zip;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "phone")
    private String phone;
    
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;
    
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SignOn signOn;
}