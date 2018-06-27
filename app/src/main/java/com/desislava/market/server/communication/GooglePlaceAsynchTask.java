package com.desislava.market.server.communication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GooglePlaceAsynchTask  extends AsyncTask<String, Integer, String> {

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
            InputStreamReader in = new InputStreamReader(http.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (IOException e) {
            ex=e;
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        Log.i("FInally", "" + jsonResults.toString());

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
       }





    }
}
