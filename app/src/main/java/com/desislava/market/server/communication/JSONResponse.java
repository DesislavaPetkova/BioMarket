package com.desislava.market.server.communication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.desislava.market.activities.MainActivity;
import com.desislava.market.fragments.MenuListProductFragment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONResponse extends AsyncTask<String, Integer, String> {

    private MenuListProductFragment fragment;
    private String store;
    //private MainActivity activity;
   private ProgressDialog dialog;
   private  AlertDialog.Builder alertDialogBuilder ;
   private Exception ex=null;


    public JSONResponse(final MainActivity mainActivity, String store) {
        this.store = store;
        dialog = new ProgressDialog(mainActivity);
        alertDialogBuilder = new AlertDialog.Builder(mainActivity);
        alertDialogBuilder.setMessage("FUCKED UP server !")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mainActivity.finish();
                    }
                });

    }

    @Override
    protected String doInBackground(String... strings) {
//TODO Update service response depending on store request
        String url = "http://169.24.35.30:8080/" + store;  //  home:192.168.0.103  work:172.22.173.133
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
            Log.e("PORBLEMMMMMM ","Exception");
            ex=e;
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String object) {
        Log.i("onPostExecute", "Enter with response");
        if(dialog.isShowing() && ex==null) {
            ParseServerResponse parseServerResponse = new ParseServerResponse();
            try {
                parseServerResponse.allStoresParseResponse(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }else{
            dialog.cancel();
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }


    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading... please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }
}
