package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "signon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignOn {
    
    @Id
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "userid")
    private Account account;
}