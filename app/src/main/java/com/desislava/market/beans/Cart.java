package com.desislava.market.beans;

/**
 * Created by Desislava on 25-Apr-18.
 */

public class Cart {

    private Product product;
    private String quantity;
    private String price;
    private String category;

    public Cart(Product product, String quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
        this.category=category;
    }



    public Product getProduct() {
        return product;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "product=" + product +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
