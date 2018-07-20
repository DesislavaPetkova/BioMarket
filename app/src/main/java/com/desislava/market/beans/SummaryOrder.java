package com.desislava.market.beans;

import java.util.ArrayList;

import static com.desislava.market.activities.ShoppingCartActivity.*;

public class SummaryOrder {
    private String total;
    private ArrayList<Cart> cart;
    private UserInfo info;


    public void setInfo(UserInfo info) {
        this.info = info;
        this.cart = shoppingList;
        this.total = "" + getTotalPrice();
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
