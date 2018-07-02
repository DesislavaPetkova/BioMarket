package com.desislava.market.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desislava on 15-Mar-18.
 */

public class Store {

    private String name;

    private List<Category> allCategory;

    public Store(String name, List<Category> allCategory) {
        this.name = name;
        this.allCategory = allCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getAllCategory() {
        return allCategory;
    }

    public void setAllCategory(List<Category> allCategory) {
        this.allCategory = allCategory;
    }

    @Override
    public String toString() {
        return "Store{" +
                "name='" + name + '\'' +
                ", allCategory=" + allCategory +
                '}';
    }
}
