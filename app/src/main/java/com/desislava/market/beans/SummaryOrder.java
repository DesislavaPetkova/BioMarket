package com.desislava.market.beans;

import java.util.ArrayList;

import static com.desislava.market.activities.ShoppingCartActivity.*;

public class SummaryOrder {
    private String storeName;
    private String total;
    private float deliveryPrice;
    private ArrayList<Cart> cart;
    private UserInfo info;


    public void setInfo(UserInfo info, String store,float deliveryPrice) {
        this.info = info;
        this.storeName=store;
        this.cart = shoppingList;
        this.total = "" + getTotalPrice();
        this.deliveryPrice=deliveryPrice;

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

    public float getDeliveryPrice() {
        return deliveryPrice;
    }

}
