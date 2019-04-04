package fr.wildcodeschool.metro;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import static fr.wildcodeschool.metro.Metro.extractStation;

public class ListViewStation extends AppCompatActivity {

   static List<StationMetro> stationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_iew_station);
        stationList = extractStation(ListViewStation.this);
        ListView listMetro = findViewById(R.id.lvStations);
        AdapterStation adapter = new AdapterStation(ListViewStation.this, stationList);
        listMetro.setAdapter(adapter);
    }
}