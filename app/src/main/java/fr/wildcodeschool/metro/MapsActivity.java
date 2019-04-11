package fr.wildcodeschool.metro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 1234;
    private static final String MTROLIST_JSON = "Toulouse-metro.json";
    private GoogleMap mMap;
    private LocationManager mLocationManager = null;
    private Location locationUser = new Location("");
    private final static String API_KEY = "&key=e083e127-3c7c-4d1b-b5c8-a5838936e4cf";
    private final static String LIGNE_A = "11821949021891694";
    private final static String LIGNE_B = "11821949021892004";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulauncher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent goToListView = new Intent(MapsActivity.this, ListViewStation.class);
                goToListView.putExtra("locationUser", locationUser);
                startActivity(goToListView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocation();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle(R.string.title);
                    builder.setMessage(R.string.textMessageConfirmation);
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        }
                    });
                    builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng coordinate = new LatLng(lat, lng);
                locationUser.setLatitude(lat);
                locationUser.setLongitude(lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                mMap.setMyLocationEnabled(true);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, locationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        checkPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setMinZoomPreference(12.0f);

        //Station Metro A
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://api.tisseo.fr/v1/stop_areas.json?displayCoordXY=1&lineId=" + LIGNE_A + API_KEY;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject stopAreas = response.getJSONObject("stopAreas");
                            JSONArray stopArea = stopAreas.getJSONArray("stopArea");
                            for (int i = 0; i < stopArea.length(); i++) {
                                JSONObject numStation = (JSONObject)stopArea.get(i);
                                String cityName = numStation.getString("cityName");
                                String id = numStation.getString("id");
                                String name = numStation.getString("name");
                                double x = numStation.getDouble("x");
                                double y = numStation.getDouble("y");

                                LatLng coordStation = new LatLng(y, x);

                                mMap.addMarker(new MarkerOptions()
                                        .position(coordStation)
                                        .title(name)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        //Station Metro B
        RequestQueue requestQueueLigneB = Volley.newRequestQueue(this);

        String urlLigneB = "https://api.tisseo.fr/v1/stop_areas.json?displayCoordXY=1&lineId="  + LIGNE_B + API_KEY;

        final JsonObjectRequest jsonObjectRequestLigneB = new JsonObjectRequest(
                Request.Method.GET, urlLigneB, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject stopAreas = response.getJSONObject("stopAreas");
                            JSONArray stopArea = stopAreas.getJSONArray("stopArea");
                            for (int i = 0; i < stopArea.length(); i++) {
                                JSONObject numStation = (JSONObject)stopArea.get(i);
                                String cityName = numStation.getString("cityName");
                                String id = numStation.getString("id");
                                String name = numStation.getString("name");
                                double x = numStation.getDouble("x");
                                double y = numStation.getDouble("y");

                                LatLng coordStation = new LatLng(y, x);

                                mMap.addMarker(new MarkerOptions()
                                        .position(coordStation)
                                        .title(name)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        requestQueueLigneB.add(jsonObjectRequestLigneB);
    }
}