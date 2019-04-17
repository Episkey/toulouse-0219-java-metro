package fr.wildcodeschool.metro;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
