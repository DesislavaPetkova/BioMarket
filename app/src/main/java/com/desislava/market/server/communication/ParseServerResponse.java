package com.desislava.market.server.communication;

import android.util.Log;

import com.desislava.market.beans.Category;
import com.desislava.market.beans.GooglePlace;
import com.desislava.market.beans.Product;
import com.desislava.market.beans.Store;
import com.desislava.market.utils.Constants;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Parse incoming json response from server and google place JSON received
 */

public class ParseServerResponse {

    List<Product> products;

    List<Category> categories;
    public static List<Store> storeList = new ArrayList<>();

    public static List<GooglePlace> places = new ArrayList<>();

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

    public static void parseGooglePlaceResponse(String object) throws JSONException {
        JSONObject json= new JSONObject(object);

        JSONArray results = json.getJSONArray(Constants.RESULTS);
        Log.i("parseGooglePlResponse", "JSONArray size:" + results.length());
        for (int i = 0; i < results.length(); i++) {
            GooglePlace place = new GooglePlace();
            JSONObject obj = results.getJSONObject(i);
            JSONObject loc = obj.getJSONObject(Constants.GEOMETRY).getJSONObject(Constants.LOCATION);
            place.setLat((Double) loc.get(Constants.LATITUDE));
            place.setLng((Double) loc.get(Constants.LONGITUDE));

            //Get is open now
            place.setOpenNow((Boolean) obj.getJSONObject(Constants.OPEN_HOURS).get(Constants.OPEN_NOW));

            place.setVicinity((String) obj.get(Constants.VICINITY));
            places.add(place);
            Log.i("SINGLE PLACE:" ,""+place.toString());
        }

        //Log.i("parseGooglePlaceRep", " END " + Arrays.asList(places));


    }


}
