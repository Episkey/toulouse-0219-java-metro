package fr.wildcodeschool.metro;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static fr.wildcodeschool.metro.Helper.LIGNE_A;
import static fr.wildcodeschool.metro.Helper.LIGNE_B;

public class ListViewStation extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
                Intent goToMapView = new Intent(ListViewStation.this, MapsActivity.class);
                startActivity(goToMapView);
            case R.id.btListView:
                Intent goToListView = new Intent(ListViewStation.this, ListViewStation.class);
                startActivity(goToListView);
                return true;
            case R.id.itemMenuRegister:
                Intent goToRegisterView = new Intent(ListViewStation.this, RegisterActivity.class);
                startActivity(goToRegisterView);
                return true;
            case R.id.itemMenuLogin:
                Intent goToMainActivity = new Intent(ListViewStation.this, MainActivity.class);
                startActivity(goToMainActivity);
                return true;
            case R.id.itemMenuFav:
                Intent goToFavorites = new Intent(ListViewStation.this, Favorites.class);
                startActivity(goToFavorites);
            case R.id.itemMenuLogout:
                mAuth.signOut();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_station);

        Intent intent = getIntent();
        final Location locationUser = intent.getParcelableExtra("mLocationUser");

        final List<StationMetro> stationList = new ArrayList<>();
        ListView listMetro = findViewById(R.id.lvStations);
        final AdapterStation adapter = new AdapterStation(ListViewStation.this, stationList, locationUser);
        listMetro.setAdapter(adapter);

        Helper.extractStation(ListViewStation.this, locationUser, LIGNE_A, new Helper.StationListener() {
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

        Helper.extractStation(ListViewStation.this, locationUser, LIGNE_B, new Helper.StationListener() {
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
}
