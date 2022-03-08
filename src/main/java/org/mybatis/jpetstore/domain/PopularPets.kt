package org.mybatis.jpetstore.domain;

import java.io.Serializable;
import java.util.List;

public class PopularPets implements Serializable {

    private static final long serialVersionUID = 1024578535545257459L;

    private String attr1;
    private int sum;
    private String name;
    private String itemId;

    private List<PopularPets> popularPetsList;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;

    }

    public List<PopularPets> getPopularPetsList() {
        return popularPetsList;
    }

    public void setPopularPetsList(List<PopularPets> popularPetsList) {
        this.popularPetsList = popularPetsList;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
