package fr.wildcodeschool.metro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import static fr.wildcodeschool.metro.Metro.extractStation;

public class ListViewStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_station);
        List<StationMetro> stationList = extractStation(ListViewStation.this);
        ListView listMetro = findViewById(R.id.lvStations);
        AdapterStation adapter = new AdapterStation(ListViewStation.this, stationList);
        listMetro.setAdapter(adapter);
    }
}