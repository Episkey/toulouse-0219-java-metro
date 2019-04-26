package fr.wildcodeschool.metro;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static fr.wildcodeschool.metro.Helper.LIGNE_A;
import static fr.wildcodeschool.metro.Helper.LIGNE_B;

public class RecycleViewStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view_station_list);
        Intent intent = getIntent();
        final Location locationUser = intent.getParcelableExtra("mLocationUser");

        final List<StationMetro> stationList = new ArrayList<>();

        RecyclerView recycleListStation = findViewById(R.id.recycle_view_station_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleListStation.setLayoutManager(layoutManager);

         final RecyclerAdapterStation adapter = new  RecyclerAdapterStation(stationList);
        recycleListStation.setAdapter(adapter);

        Helper.extractStation(RecycleViewStation.this, locationUser, LIGNE_A, new Helper.StationListener() {
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

        Helper.extractStation(RecycleViewStation.this, locationUser, LIGNE_B, new Helper.StationListener() {
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