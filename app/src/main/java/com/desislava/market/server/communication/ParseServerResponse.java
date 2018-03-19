package com.desislava.market.server.communication;

import com.desislava.market.beans.Category;
import com.desislava.market.beans.Product;
import com.desislava.market.beans.Store;
import com.desislava.market.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parse incoming json response from server
 */

public class ParseServerResponse {

    List<Product> products;

    List<Category> categories;
    public static List<Store> storeList = new ArrayList<>();

    public void allStoresParseResponse(String object) throws JSONException {

        JSONObject stores = new JSONObject(object);
        Iterator<String> itrStoreName = stores.keys();
        while (itrStoreName.hasNext()) {
            categories = new ArrayList<>();
            String storeName = itrStoreName.next(); //Name  of the Store
            JSONArray productsArray = stores.getJSONArray(storeName);
            for (int i = 0; i < productsArray.length(); i++) {   // 2 categories
                products = new ArrayList<>();
                JSONObject obj = productsArray.getJSONObject(i);
                Iterator<String> iterator = obj.keys(); //while (hasNExt() )-> check !!!!!!!!!!
                String name = iterator.next();//fruits name..vegetables name..

                JSONArray product = obj.getJSONArray(name);
                for (int pr = 0; pr < product.length(); pr++) {
                    String namepr = product.getJSONObject(pr).getString(Constants.NAME);
                    String price = product.getJSONObject(pr).getString(Constants.PRICE);
                    String info = product.getJSONObject(pr).getString(Constants.INFO);
                    String origin = product.getJSONObject(pr).getString(Constants.ORIGIN);
                    Product productObject = new Product(namepr, price, info, origin);
                    products.add(productObject);
                }
                Category category = new Category(name, products);
                categories.add(category);
            }
            Store store = new Store(storeName, categories);
            storeList.add(store);
        }
        System.out.println(storeList);
    }


/*    public void singleStoreParseResponse(String object) throws JSONException{
        JSONObject stores = new JSONObject(object);
        Iterator<String> itrStoreName = stores.keys();
        while (itrStoreName.hasNext()) {
            categories = new ArrayList<>();
            String storeName = itrStoreName.next(); //Name  of the Store
            JSONArray productsArray = stores.getJSONArray(storeName);
            for (int i = 0; i < productsArray.length(); i++) {   // 2 categories
                products = new ArrayList<>();
                JSONObject obj = productsArray.getJSONObject(i);
                Iterator<String> iterator = obj.keys(); //while (hasNExt() )-> check !!!!!!!!!!
                String name = iterator.next();//fruits name..vegetables name..

                JSONArray single_product_info = obj.getJSONArray(name);
                for (int pr = 0; pr < single_product_info.length(); pr++) {
                    String namepr = single_product_info.getJSONObject(pr).getString(Constants.NAME);
                    String price = single_product_info.getJSONObject(pr).getString(Constants.PRICE);
                    String info = single_product_info.getJSONObject(pr).getString(Constants.INFO);
                    String origin = single_product_info.getJSONObject(pr).getString(Constants.ORIGIN);
                    Product productObject = new Product(namepr, price, info, origin);
                    products.add(productObject);
                }
                Category category = new Category(name, products);
                categories.add(category);
            }
            Store store = new Store(storeName, categories);
            storeList.add(store);
        }
        System.out.println(storeList);
    } */

    public List<Store> getStoreList() {
        return storeList;
    }
}
