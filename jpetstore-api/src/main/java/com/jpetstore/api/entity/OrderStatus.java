package com.jpetstore.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "orderstatus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderStatusId.class)
public class OrderStatus {
    
    @Id
    @Column(name = "orderid")
    private Integer orderId;
    
    @Id
    @Column(name = "linenum")
    private Integer lineNumber;
    
    @Column(name = "timestamp")
    private LocalDate timestamp;
    
    @Column(name = "status")
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Order order;
}
