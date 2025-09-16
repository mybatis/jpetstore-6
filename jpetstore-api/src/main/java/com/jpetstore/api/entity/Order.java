package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    private Integer orderId;
    
    @Column(name = "userid")
    private String userId;
    
    @Column(name = "orderdate")
    private LocalDate orderDate;
    
    @Column(name = "shipaddr1")
    private String shipAddress1;
    
    @Column(name = "shipaddr2")
    private String shipAddress2;
    
    @Column(name = "shipcity")
    private String shipCity;
    
    @Column(name = "shipstate")
    private String shipState;
    
    @Column(name = "shipzip")
    private String shipZip;
    
    @Column(name = "shipcountry")
    private String shipCountry;
    
    @Column(name = "billaddr1")
    private String billAddress1;
    
    @Column(name = "billaddr2")
    private String billAddress2;
    
    @Column(name = "billcity")
    private String billCity;
    
    @Column(name = "billstate")
    private String billState;
    
    @Column(name = "billzip")
    private String billZip;
    
    @Column(name = "billcountry")
    private String billCountry;
    
    @Column(name = "courier")
    private String courier;
    
    @Column(name = "totalprice")
    private BigDecimal totalPrice;
    
    @Column(name = "billtofirstname")
    private String billToFirstName;
    
    @Column(name = "billtolastname")
    private String billToLastName;
    
    @Column(name = "shiptofirstname")
    private String shipToFirstName;
    
    @Column(name = "shiptolastname")
    private String shipToLastName;
    
    @Column(name = "creditcard")
    private String creditCard;
    
    @Column(name = "exprdate")
    private String expiryDate;
    
    @Column(name = "cardtype")
    private String cardType;
    
    @Column(name = "locale")
    private String locale;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LineItem> lineItems;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderStatus> orderStatuses;
}
