package org.mybatis.jpetstore.domain;

import java.io.Serializable;

public class Inventory {

    private String itemId;
    private int quantity;

    public String getItemId() { return itemId; }

    public void setItemId(String itemId) { this.itemId = itemId.trim(); }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

}
