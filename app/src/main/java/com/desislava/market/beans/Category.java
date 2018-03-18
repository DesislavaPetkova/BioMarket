package com.desislava.market.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desislava on 15-Mar-18.
 */

public class Category {

    String name;

    List<Product> allProducts=new ArrayList<>();

    public Category(String name, List<Product> allProducts) {
        this.name = name;
        this.allProducts = allProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", allProducts=" + allProducts +
                '}';
    }
}
