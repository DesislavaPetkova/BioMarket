package com.desislava.market.server.communication;

import android.util.Log;

import com.desislava.market.beans.Category;
import com.desislava.market.beans.Product;
import com.desislava.market.beans.Store;
import com.desislava.market.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Parse incoming json response from server
 */

public class ParseServerResponse {

    List<Product> products;

    List<Category> categories;
    public static List<Store> storeList = new ArrayList<>();
    public static int jsonVersion;

    public void allStoresParseResponse(String object) throws JSONException {

        JSONObject stores = new JSONObject(object);
        Iterator<String> itrStoreName = stores.keys();
        while (itrStoreName.hasNext()) {
            categories = new ArrayList<>();
            String storeName = itrStoreName.next(); //Name  of the Store
            JSONArray productsArray = stores.getJSONArray(storeName);
            if (storeName.equals("Version")) {
                jsonVersion = Integer.parseInt(productsArray.get(0).toString());
                Log.e("allStoresParseResponse ", "------------JSON version----------------- " + jsonVersion);
                continue;
            }

            for (int i = 0; i < productsArray.length(); i++) {   // 2 categories
                products = new ArrayList<>();
                JSONObject obj = productsArray.getJSONObject(i);
                Iterator<String> iterator = obj.keys(); //while (hasNExt() )-> check !!!!!!!!!!
                String name = iterator.next();//fruits name..vegetables name..
                JSONArray product = obj.getJSONArray(name);
                for (int pr = 0; pr < product.length(); pr++) {
                    JSONObject jsonObject = product.getJSONObject(pr);
                    String namepr = jsonObject.getString(Constants.NAME);
                    String price = jsonObject.getString(Constants.PRICE);
                    String info = jsonObject.getString(Constants.INFO);
                    String origin = jsonObject.getString(Constants.ORIGIN);
                    String imageUrl = jsonObject.getString(Constants.IMAGE_URL);
                    Product productObject = new Product(namepr, price, info, origin, imageUrl);
                    products.add(productObject);
                }
                Category category = new Category(name, products);
                categories.add(category);
            }
            Store store = new Store(storeName, categories);
            storeList.add(store);
        }
        Log.i("List after parsing info", storeList.toString());
    }

}
