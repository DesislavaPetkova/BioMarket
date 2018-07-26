package com.desislava.market.beans;

import java.util.ArrayList;

import static com.desislava.market.activities.ShoppingCartActivity.*;

public class SummaryOrder {
    private String storeName;
    private String total;
    private ArrayList<Cart> cart;
    private UserInfo info;


    public void setInfo(UserInfo info, String store) {
        this.info = info;
        this.storeName=store;
        this.cart = shoppingList;
        this.total = "" + getTotalPrice();
    }

    public String getStoreName() {
        return storeName;
    }

    public String getTotal() {
        return total;
    }

    public ArrayList<Cart> getCart() {
        return cart;
    }

    public UserInfo getInfo() {
        return info;
    }
}
