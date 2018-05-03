package com.desislava.market.beans;

/**
 * Created by Desislava on 25-Apr-18.
 */

public class Cart {

    private Product product;
    private String quantity;
    private String price;
    private String category;

    public Cart(Product product, String quantity,String category) {
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
    public int getQuantityInt(){

        return Integer.parseInt(quantity);
    }

    public String getPrice() {
        return price;
    }
    public float getPriceAsInt(){
        return Float.parseFloat(price);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
