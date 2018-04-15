package com.desislava.market.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Desislava on 08-Mar-18.
 */

public class Product implements Serializable {

    String name;
    String price;
    String info;
    String origin;

    public Product(String name, String price, String info, String origin) {
        this.name = name;
        this.price = price;
        this.info = info;
        this.origin = origin;
    }

    public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getPrice() {
         return price;
     }

     public void setPrice(String price) {
         this.price = price;
     }

     public String getInfo() {
         return info;
     }

     public void setInfo(String info) {
         this.info = info;
     }

     public String getOrigin() {
         return origin;
     }

     public void setOrigin(String origin) {
         this.origin = origin;
     }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", info='" + info + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}


