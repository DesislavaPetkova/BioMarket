package com.desislava.market.server.communication;

import android.util.Log;

import com.desislava.market.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DirectionsDataParser {
    private HashMap<String, String> directionsMap ;
    private String[] addr;

    public String[] getAddr() {
        return addr;
    }

    public HashMap<String, String> getGoogleDirectionsMap() {
        return directionsMap;
    }

    private void getDuration(JSONArray googleDirectionsJson) {
        directionsMap = new HashMap<>();
        String duration;
        String distance;

        try {

            duration = googleDirectionsJson.getJSONObject(0).getJSONObject(Constants.DURATION).getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject(Constants.DISTANCE).getString("text");

            directionsMap.put("duration", duration);
            directionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // return directionsMap;
    }


    private void addresses(JSONArray legs) {

        addr = new String[2];
        try {
            addr[0] = legs.getJSONObject(0).getString("start_address");
            addr[1] = legs.getJSONObject(0).getString("end_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String[] parseDirections(String jsonData) {
        Log.i("parseDirections", "Enter");
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray legs = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
            getDuration(legs);
            addresses(legs);
            jsonArray = legs.getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    private String[] getPaths(JSONArray googleStepsJson) {
        Log.i("getPaths", "Enter");
        int count = googleStepsJson.length();
        String[] polylines = new String[count];

        for (int i = 0; i < count; i++) {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    private String getPath(JSONObject googlePathJson) {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }


}
