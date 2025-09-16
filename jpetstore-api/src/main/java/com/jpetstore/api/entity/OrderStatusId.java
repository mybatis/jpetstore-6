package com.jpetstore.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusId implements Serializable {
    
    private Integer orderId;
    private Integer lineNumber;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        OrderStatusId that = (OrderStatusId) o;
        
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        return lineNumber != null ? lineNumber.equals(that.lineNumber) : that.lineNumber == null;
    }
    
    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        return result;
    }
}
