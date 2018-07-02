package com.desislava.market.server.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.desislava.market.fragments.LocationFragment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GooglePlaceAsyncTask  extends AsyncTask<String, Integer, String> {

    public interface GooglePlace {
        void placeReady();
    }

    GooglePlace place;

    public GooglePlaceAsyncTask(final LocationFragment locationFragment) {
        this.place = locationFragment;
    }

    private Exception ex = null;

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection http = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            http = (HttpURLConnection) url.openConnection();
            int code=http.getResponseCode();
            Log.i("BACKGROUND CODE: ",""+code);
            if(code==HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()),8192*2);
                while ((line = reader.readLine()) != null) {
                    jsonResults.append(line);
                }
                reader.close();
            }else{
                ex=new Exception();
            }
        } catch (IOException e) {
            ex=e;
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }

        return jsonResults.toString();
    }

    @Override
    protected void onPostExecute(String s) {
       Log.i("Google on post execute:","Enter");

       if(ex==null){
           try {
               ParseServerResponse.parseGooglePlaceResponse(s);
           } catch (JSONException e) {
               e.printStackTrace();
           }
           place.placeReady();

       }else{
           Log.e("Exception",ex.toString());
       }

    }
}
