package fr.wildcodeschool.metro;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class Helper {

    public final static String LIGNE_A = "11821949021891694";
    public final static String LIGNE_B = "11821949021892004";
    private final static String API_KEY = "&key=e083e127-3c7c-4d1b-b5c8-a5838936e4cf";
    SingletonLocation singletonLocation = SingletonLocation.getLocationInstance();
    UserLocation userLocation = singletonLocation.getUserLocation();

    public static void extractStation(Context context, final UserLocation userLocation, String line, final StationListener listener) {

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = "https://api.tisseo.fr/v1/stop_areas.json?displayCoordXY=1&lineId=" + line + API_KEY;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        List<StationMetro> stations = new ArrayList<>();
                        try {
                            JSONObject stopAreas = response.getJSONObject("stopAreas");
                            JSONArray stopArea = stopAreas.getJSONArray("stopArea");
                            for (int i = 0; i < stopArea.length(); i++) {
                                JSONObject numStation = (JSONObject) stopArea.get(i);
                                String cityName = numStation.getString("cityName");
                                String id = numStation.getString("id");
                                String name = numStation.getString("name");
                                double x = numStation.getDouble("x");
                                double y = numStation.getDouble("y");
                                LatLng coordStation = new LatLng(y, x);
                                StationMetro station = new StationMetro(name, y, x, id);
                                int distance = round(userLocation.getLocation().distanceTo(station.getLocation()));
                                station.setDistance(distance);
                                stations.add(station);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listener.onStationsLoaded(stations);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public interface StationListener {
        void onStationsLoaded(List<StationMetro> stations);
    }
}


