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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import static fr.wildcodeschool.metro.Helper.LIGNE_A;
import static fr.wildcodeschool.metro.Helper.LIGNE_B;
import static java.lang.Math.round;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 1234;
    private GoogleMap mMap;
    private LocationManager mLocationManager = null;
    private Location mLocationUser = null;
    private boolean mHasMarkerCreated = false;

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
                goToListView.putExtra("mLocationUser", mLocationUser);
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
                mLocationUser = new Location("");
                mLocationUser.setLatitude(lat);
                mLocationUser.setLongitude(lng);
                if (mMap != null && !mHasMarkerCreated) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                    mMap.setMyLocationEnabled(true);
                    createMarkers();
                }
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
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                if (location != null && mHasMarkerCreated) {
                    createMarkers();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkPermission();
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
        if (mLocationUser != null && !mHasMarkerCreated) {
            createMarkers();
        }
    }

    private void createMarkers() {
        mHasMarkerCreated = true;
        mMap.setInfoWindowAdapter(new CustomInfoMarkerAdapter(MapsActivity.this));
        Helper.extractStation(MapsActivity.this, mLocationUser, LIGNE_A, new Helper.StationListener() {
            @Override
            public void onStationsLoaded(List<StationMetro> stations) {
                for (StationMetro station : stations) {
                    int distance = round(mLocationUser.distanceTo(station.getLocation()));
                    LatLng coordStation = new LatLng(station.getLatitude(), station.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(coordStation)
                            .title(station.getName())
                            .snippet(String.format(getString(R.string.snippet_text), getString(R.string.a), distance))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }
        });

        Helper.extractStation(MapsActivity.this, mLocationUser, LIGNE_B, new Helper.StationListener() {
            @Override
            public void onStationsLoaded(List<StationMetro> stations) {
                for (StationMetro station : stations) {
                    int distance = round(mLocationUser.distanceTo(station.getLocation()));
                    LatLng coordStation = new LatLng(station.getLatitude(), station.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(coordStation)
                            .title(station.getName())
                            .snippet(String.format(getString(R.string.snippet_text), getString(R.string.b), distance))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
            }
        });
    }
}