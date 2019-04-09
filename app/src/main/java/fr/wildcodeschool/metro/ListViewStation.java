package fr.wildcodeschool.metro;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import java.util.List;
import static fr.wildcodeschool.metro.Metro.extractStation;

public class ListViewStation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Location locationUser = intent.getParcelableExtra("locationUser");
        setContentView(R.layout.list_view_station);
        List<StationMetro> stationList = extractStation(ListViewStation.this);
        ListView listMetro = findViewById(R.id.lvStations);
        AdapterStation adapter = new AdapterStation(ListViewStation.this, stationList, locationUser);
        listMetro.setAdapter(adapter);
    }
}