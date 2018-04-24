package com.desislava.market.server.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.desislava.market.activities.MainActivity;
import com.desislava.market.R;
import com.desislava.market.fragments.MenuListProductFragment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONResponse extends AsyncTask<String, Integer, String> {

    private MenuListProductFragment fragment;
    private String store;

    public JSONResponse(MainActivity mainActivity, String store) {
        this.store = store;
        fragment = (MenuListProductFragment) mainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_main_menu_list);
    }

    @Override
    protected String doInBackground(String... strings) {
//TODO Update service response depending on store request
        String url = "http://172.22.173.184:8080/" + store;  //  home:192.168.0.103  work:172.22.173.184
        StringBuffer response = new StringBuffer();
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            Log.i("doInBackground", "Sending GET request to URL: " + url);
            Log.i("doInBackground", "Response Code : " + responseCode);

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
        Log.i("onPostExecute", "Enter with response");
        ParseServerResponse parseServerResponse = new ParseServerResponse();
        try {
            parseServerResponse.allStoresParseResponse(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            Log.i("onPostExecute", "Data changed");
            fragment.dataChange();
        }

    }
}
