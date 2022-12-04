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
package org.mybatis.jpetstore.web.actions;

import java.util.List;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.*;
import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.service.CatalogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The Class AccountActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class AdminActionBean extends AbstractActionBean {
    private static final String PRODUCT_LIST = "/WEB-INF/jsp/admin/ProductList.jsp";

    private static final String MAIN = "/WEB-INF/jsp/catalog/Main.jsp";
    private static final String VIEW_CATEGORY = "/WEB-INF/jsp/catalog/Category.jsp";
    private static final String VIEW_PRODUCT = "/WEB-INF/jsp/catalog/Product.jsp";
    private static final String VIEW_ITEM = "/WEB-INF/jsp/catalog/Item.jsp";
    private static final String SEARCH_PRODUCTS = "/WEB-INF/jsp/catalog/SearchProducts.jsp";
    private static final String VIEW_ITEM_EDIT = "/WEB-INF/jsp/admin/ItemList.jsp";
    private static final String VIEW_ITEM_ADDFORM = "/WEB-INF/jsp/admin/ItemAddForm.jsp";
    private static final String VIEW_ITEM_UPDATEFORM = "/WEB-INF/jsp/admin/ItemUpdateForm.jsp";

    @SpringBean
    private transient CatalogService catalogService;

    private String keyword;

    private String categoryId;
    private Category category;
    private List<Category> categoryList;

    private String productId;
    private Product product;
    private List<Product> productList;

    private String itemId;
    private Item item;
    private List<Item> itemList;

    private Inventory inventory;
    private Account account;

    private int quantity;

    private int role;

    public Inventory getInventory() { return inventory; }

    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @DefaultHandler
    public ForwardResolution viewMain() {
        return new ForwardResolution(MAIN);
    }




    /**
     * ProductList form.
     *
     * @return the resolution
     */

    //  public Resolution ProductListForm(){ return new ForwardResolution(PRODUCT_LIST); }

    public ForwardResolution viewProductList(){
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();

        if(role == 1) {
            productList = catalogService.getProductList();
            return new ForwardResolution(PRODUCT_LIST);
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }

    public Resolution insertItem(){
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();

        if(role ==1 ) {
            item.setProductId(product.getProductId());
            inventory.setItemId(item.getItemId());

            catalogService.insertItem(item);
            catalogService.insertInventory(inventory);

            if (productId != null) {
                itemList = catalogService.getItemListByProduct(productId);
                product = catalogService.getProduct(productId);
            }
            return new ForwardResolution(VIEW_ITEM_EDIT);
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }

    public Resolution deleteItem(){
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();
        if(role == 1) {
            if (itemId != null) {
                catalogService.deleteItem(itemId);
                catalogService.deleteItemInventory(itemId);
            }
            if (productId != null) {
                itemList = catalogService.getItemListByProduct(productId);
                product = catalogService.getProduct(productId);
            }
            return new ForwardResolution(VIEW_ITEM_EDIT);
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }

    public ForwardResolution viewAddForm(){
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();
        if(role == 1) {
            if (productId != null) {
                product = catalogService.getProduct(productId);
            }
            return new ForwardResolution(VIEW_ITEM_ADDFORM);
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }

    public ForwardResolution viewUpdateForm(){
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();
        if(role == 1) {
            item = catalogService.getItem(itemId);
            product = item.getProduct();

            return new ForwardResolution(VIEW_ITEM_UPDATEFORM);
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }

    public ForwardResolution viewEditItem(){
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();
        if(role == 1) {
            if (productId != null) {
                itemList = catalogService.getItemListByProduct(productId);
                product = catalogService.getProduct(productId);
            }
            return new ForwardResolution(VIEW_ITEM_EDIT);
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }


    public Resolution updateItem() {
        HttpSession session = context.getRequest().getSession();
        AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

        role = accountBean.getAccount().getRole();
        if (role == 1) {
            item.setItemId(item.getItemId());
            catalogService.updateItem(item);

            if (item != null) {
                itemList = catalogService.getItemListByProduct(item.getProductId());
                item = catalogService.getItem(item.getItemId());
                product = catalogService.getProduct(item.getItemId());
            }

            return new RedirectResolution(AdminActionBean.class, "viewEditItem");
        }
        else {
            setMessage("You must log in with Admin ID. ");
            return new ForwardResolution(ERROR);
        }
    }


}