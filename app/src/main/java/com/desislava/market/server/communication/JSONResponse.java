package com.desislava.market.server.communication;

import android.os.AsyncTask;

import com.desislava.market.dummy.StoreContent;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONResponse extends AsyncTask<String, Integer, String> {


    @Override
    protected String doInBackground(String... strings) {

        String url="http://192.168.0.103:8080/vegetables";  //home:192.168.0.103  work:172.22.173.184
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
        System.out.println("onPostExecute - enter");
        ParseServerResponse parseServerResponse = new ParseServerResponse();
        try {
            parseServerResponse.convertToJson(object);
        } catch (JSONException e) {

        }
        StoreContent content=new StoreContent(parseServerResponse);

    }
}
