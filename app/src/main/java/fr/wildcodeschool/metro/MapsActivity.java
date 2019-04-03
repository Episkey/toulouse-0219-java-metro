package fr.wildcodeschool.metro;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager mLocationManager = null;

    private void checkPermission() {
        
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // l'autorisation a été refusée précédemment, on peut prévenir l'utilisateur ici
                //TODO : mettre une fenêtre pour prévenir l'utilisateur?
            }
            else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }
        else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        switch (requestCode) {
            case 100: {
                initLocation();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // l'autorisation a été refusée
                    //TODO : mettre une fenetre ou forcer l'appli à se fermer ?
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
                double lat =  location.getLatitude();
                double lng = location.getLongitude();
                LatLng coordinate = new LatLng(lat, lng);

                mMap.addMarker(new MarkerOptions().position(coordinate).title("Ma position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                mMap.setMinZoomPreference(12.0f);
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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, locationListener);
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

        String json = null;
        try {
            InputStream is = getAssets().open("Toulouse-metro.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            JSONArray root = new JSONArray(json);

            for (int i = 0; i < root.length(); i++) {
                JSONObject stationInfo = root.getJSONObject(i);
                JSONObject fields = stationInfo.getJSONObject("fields");
                for (int j = 0; j < fields.length(); j++) {
                    String stationName = fields.getString("nom");
                    char stationLine = fields.getString("ligne").charAt(0);
                    JSONArray geoPoint = fields.getJSONArray("geo_point_2d");
                    double latStation = geoPoint.getDouble(0);
                    double lngStation = geoPoint.getDouble(1);
                    LatLng coordStation = new LatLng(latStation, lngStation);
                    if (stationLine == 'B') {
                        mMap.addMarker(new MarkerOptions()
                                .position(coordStation)
                                .title(stationName)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }
                    if (stationLine == 'A') {
                        mMap.addMarker(new MarkerOptions()
                                .position(coordStation)
                                .title(stationName)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}