package fr.wildcodeschool.metro;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import static java.lang.Math.round;

public class AdapterStation extends ArrayAdapter<StationMetro> {
    Location locUser = new Location("");

    public AdapterStation(Context context, List<StationMetro> stationMetro, Location locationUser) {
        super(context, 0, stationMetro);
        locUser = locationUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationMetro item = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_for_adapter, parent, false);
        }
        TextView textStation = convertView.findViewById(R.id.tvItemJson);
        textStation.setText(item.getName());

        TextView textDistance = convertView.findViewById(R.id.tvDistance);
        int distance = round(locUser.distanceTo(item.getLocation()));
        String distanceInMeters = String.valueOf(distance);
        textDistance.setText(distanceInMeters + " meters");
        return convertView;
    }
}
