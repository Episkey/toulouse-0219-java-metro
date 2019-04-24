package fr.wildcodeschool.metro;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Favorites extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<StationMetro> mStationMetro;
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
                Intent goToMapView = new Intent(Favorites.this, MapsActivity.class);
                startActivity(goToMapView);
            case R.id.btListView:
                Intent goToListView = new Intent(Favorites.this, ListViewStation.class);
                startActivity(goToListView);
                return true;
            case R.id.itemMenuRegister:
                Intent goToRegisterView = new Intent(Favorites.this, RegisterActivity.class);
                startActivity(goToRegisterView);
                return true;
            case R.id.itemMenuLogin:
                Intent goToMainActivity = new Intent(Favorites.this, MainActivity.class);
                startActivity(goToMainActivity);
                return true;
            case R.id.itemMenuFav:
                Intent goToFavorites = new Intent(Favorites.this, Favorites.class);
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
        setContentView(R.layout.favorites);
        Intent intent = getIntent();
        final Location locationUser = intent.getParcelableExtra("mLocationUser");
        mStationMetro = new ArrayList<>();
        mRecyclerView = findViewById(R.id.favorites_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FavoritesAdapter(mStationMetro);
        mRecyclerView.setAdapter(mAdapter);

        Helper.extractStation(Favorites.this, locationUser, LIGNE_A, new Helper.StationListener() {
            @Override
            public void onStationsLoaded(List<StationMetro> stations) {
                mStationMetro.addAll(stations);
                Collections.sort(mStationMetro, new Comparator<StationMetro>() {
                    public int compare(StationMetro o1, StationMetro o2) {
                        return o2.getDistance() < o1.getDistance() ? 1 : -1;
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
        });

        Helper.extractStation(Favorites.this, locationUser, LIGNE_B, new Helper.StationListener() {
            @Override
            public void onStationsLoaded(List<StationMetro> stations) {
                mStationMetro.addAll(stations);
                Collections.sort(mStationMetro, new Comparator<StationMetro>() {
                    public int compare(StationMetro o1, StationMetro o2) {
                        return o2.getDistance() < o1.getDistance() ? 1 : -1;
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
