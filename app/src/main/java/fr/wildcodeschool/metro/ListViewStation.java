package fr.wildcodeschool.metro;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static fr.wildcodeschool.metro.Helper.LIGNE_A;
import static fr.wildcodeschool.metro.Helper.LIGNE_B;

public class ListViewStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Location locationUser = intent.getParcelableExtra("mLocationUser");

        setContentView(R.layout.list_view_station);

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
