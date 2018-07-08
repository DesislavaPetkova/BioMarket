package com.desislava.market.server.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.desislava.market.fragments.LocationFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DirectionsAsyncTask extends AsyncTask<String, Integer, String> {


    private Exception ex = null;
    private DirectionsReady dir;

    public interface DirectionsReady {
        void directionReady(String directionsList);
    }

    public DirectionsAsyncTask(LocationFragment locationFragment) {
        dir = locationFragment;
    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder response = new StringBuilder();
        URL obj;
        try {
            obj = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i("doInBackground", "Sending GET request to URL: " + strings[0]);
                Log.i("doInBackground", "Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()),8192*2);
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                ex = new Exception();
            }
        } catch (Exception e) {
            Log.e("doInBackground ", "" + e);
            ex = e;
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Directions onPostExecute", "Enter "+s);

        dir.directionReady(s);
    }
}
