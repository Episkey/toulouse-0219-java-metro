package fr.wildcodeschool.metro;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterStation extends ArrayAdapter<StationMetro> {
    Location mLocUser;

    public AdapterStation(Context context, List<StationMetro> stationMetro, Location locationUser) {
        super(context, 0, stationMetro);
        mLocUser = locationUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final StationMetro item = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_for_adapter, parent, false);
        }
        TextView textStation = convertView.findViewById(R.id.tvItemJson);
        textStation.setText(item.getName());
        TextView textDistance = convertView.findViewById(R.id.tvDistance);
        textDistance.setText(String.format(convertView.getContext().getString(R.string.meters), item.getDistance()));
        Button button = convertView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference stationRef = database.getReference("favorites");
                String title = item.getName();
                stationRef.push().setValue(title);
            }
        });
        return convertView;
    }
}