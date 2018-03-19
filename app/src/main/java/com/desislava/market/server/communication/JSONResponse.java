package com.desislava.market.server.communication;

import android.os.AsyncTask;

import com.desislava.market.activities.MainActivity;
import com.desislava.market.R;
import com.desislava.market.fragments.MenuListProductFragment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONResponse extends AsyncTask<String, Integer, String> {

    private MainActivity activity;
    private MenuListProductFragment fragment;
    private String store;

    public JSONResponse(MainActivity mainActivity, String store) {
        this.activity = mainActivity;
        this.store = store;
        fragment = (MenuListProductFragment) mainActivity.getSupportFragmentManager().findFragmentById(R.id.menu_list_fragment);
    }

    @Override
    protected String doInBackground(String... strings) {
//TODO Update service response depending on store request
        String url = "http://192.168.0.101:8080/" + store;  //"vegetables"  home:192.168.0.103  work:172.22.173.184
        StringBuffer response = new StringBuffer();
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String object) {
        System.out.println("onPostExecute - enter with response");
        ParseServerResponse parseServerResponse = new ParseServerResponse();
        try {
            parseServerResponse.allStoresParseResponse(object);
        } catch (JSONException e) {

        }
        if (fragment != null) {
            System.out.println("Data changed");
            fragment.dataChange();
        }

    }
}
