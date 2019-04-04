package fr.wildcodeschool.metro;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Metro {

    static ArrayList<StationMetro> stationMetro = new ArrayList<>();

    public static ArrayList<StationMetro> extractStation(Context context) {
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
                for (int j = 0; j < fields.length(); j++) {
                    String stationName = fields.getString("nom");
                    JSONArray geoPoint = fields.getJSONArray("geo_point_2d");
                    double latStation = geoPoint.getDouble(0);
                    double lngStation = geoPoint.getDouble(1);
                    StationMetro stations = new StationMetro(stationName, latStation, lngStation);
                    stationMetro.add(stations);
                }
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        return stationMetro;
    }
}



