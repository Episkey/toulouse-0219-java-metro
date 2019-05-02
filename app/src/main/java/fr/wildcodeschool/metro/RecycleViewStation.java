package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static fr.wildcodeschool.metro.Helper.LIGNE_A;
import static fr.wildcodeschool.metro.Helper.LIGNE_B;

public class RecycleViewStation extends AppCompatActivity {

    SingletonLocation singletonLocation = SingletonLocation.getLocationInstance();
    UserLocation userLocation = singletonLocation.getUserLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view_station_list);

        final List<StationMetro> stationList = new ArrayList<>();

        RecyclerView recycleListStation = findViewById(R.id.recycle_view_station_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleListStation.setLayoutManager(layoutManager);

        final RecyclerAdapterStation adapter = new RecyclerAdapterStation(stationList);
        recycleListStation.setAdapter(adapter);

        Helper.extractStation(RecycleViewStation.this, userLocation, LIGNE_A, new Helper.StationListener() {
            @Override
            public void onStationsLoaded(List<StationMetro> stations) {
                stationList.addAll(stations);
                Collections.sort(stationList, new Comparator<StationMetro>() {
                    public int compare(StationMetro o1, StationMetro o2) {
                        return o2.getDistance() < o1.getDistance() ? 1 : -1;
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

        Helper.extractStation(RecycleViewStation.this, userLocation, LIGNE_B, new Helper.StationListener() {
            @Override
            public void onStationsLoaded(List<StationMetro> stations) {
                stationList.addAll(stations);
                Collections.sort(stationList, new Comparator<StationMetro>() {
                    public int compare(StationMetro o1, StationMetro o2) {
                        return o2.getDistance() < o1.getDistance() ? 1 : -1;
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulauncher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btMapView:
                Intent goToMapView = new Intent(RecycleViewStation.this, MapsActivity.class);
                startActivity(goToMapView);
                return true;
            case R.id.btListView:
                Intent goToListView = new Intent(RecycleViewStation.this, RecycleViewStation.class);
                startActivity(goToListView);
                return true;
            case R.id.itemMenuRegister:
                Intent goToRegisterView = new Intent(RecycleViewStation.this, RegisterActivity.class);
                startActivity(goToRegisterView);
                return true;
            case R.id.itemMenuLogin:
                Intent goToMainActivity = new Intent(RecycleViewStation.this, MainActivity.class);
                startActivity(goToMainActivity);
                return true;
            case R.id.itemMenuFav:
                Intent goToFavorites = new Intent(RecycleViewStation.this, Favorites.class);
                startActivity(goToFavorites);
                return true;
            case R.id.itemMenuLogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}