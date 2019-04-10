package fr.wildcodeschool.metro;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Metro {

    public static ArrayList<StationMetro> extractStation(Context context) {
        ArrayList<StationMetro> stationMetro = new ArrayList<>();
        String json = null;
        try {
            InputStream is = context.getAssets().open("Toulouse-metro.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONArray root = new JSONArray(json);

            for (int i = 0; i < root.length(); i++) {
                JSONObject stationInfo = root.getJSONObject(i);
                JSONObject fields = stationInfo.getJSONObject("fields");
                String stationName = fields.getString("nom");
                JSONArray geo_point_2d = (JSONArray) fields.get("geo_point_2d");
                double latitude = (double) geo_point_2d.get(0);
                double longitude = (double) geo_point_2d.get(1);
                StationMetro station = new StationMetro(stationName, latitude, longitude);
                stationMetro.add(stations);
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        return stationMetro;
    }
}
