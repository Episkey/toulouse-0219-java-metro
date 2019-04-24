package fr.wildcodeschool.metro;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
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
        Button schedule = convertView.findViewById(R.id.btSchedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSchedule = new Intent(getContext(), StopSchedule.class);
                goSchedule.putExtra("STATION_ID", item.getId());
                getContext().startActivity(goSchedule);
            }
        });
        return convertView;

    }
}