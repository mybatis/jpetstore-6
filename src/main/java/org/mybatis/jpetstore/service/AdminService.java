/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.service;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.jpetstore.domain.Inventory;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.mapper.InventoryMapper;
import org.mybatis.jpetstore.mapper.ItemMapper;
import org.mybatis.jpetstore.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ProductService.
 *
 * @author Eduardo Macarron
 */
@Service
public class AdminService {

    private final ProductMapper productMapper;
    private final ItemMapper itemMapper;

    private final InventoryMapper inventoryMapper;

    public AdminService(ProductMapper productMapper, ItemMapper itemMapper, InventoryMapper inventoryMapper) {
        this.productMapper = productMapper;
        this.itemMapper = itemMapper;
        this.inventoryMapper = inventoryMapper;
    }

    public List<Product> getProductList() {
        List<Product> products = new ArrayList<>();
        products.addAll(productMapper.getProductList());
        return products;
    }

    //이거 AdminActionBean의 updateItem()에서 쓸라고 추가
    public Item getItem(String itemId) { //itemId 전달
        return itemMapper.getItem(itemId); //itemId
    }

    public void insertItem(Item item){
        itemMapper.insertItem(item);
    }

    public void insertInventory(Inventory inventory){
        inventoryMapper.insertInventory(inventory);
    }


    public void deleteItem(String itemId){
        itemMapper.deleteItem(itemId);
    }

    public void deleteItemInventory(String itemId){
        inventoryMapper.deleteItemInventory(itemId);
    }
    public List<Item> getItemListByProduct(String productId) {
        return itemMapper.getItemListByProduct(productId);
    }

    public Product getProduct(String productId) {
        return productMapper.getProduct(productId);
    }

    @Transactional
    public void updateItem(Item item){
        itemMapper.updateItem(item);
        inventoryMapper.updateInventory(item);
    }
}