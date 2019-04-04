package fr.wildcodeschool.metro;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class AdapterStation extends ArrayAdapter<StationMetro> {

    public AdapterStation(Context context, List<StationMetro> menu) {
        super(context, 0, menu);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationMetro item = getItem(position);
        if(null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_for_adapter, parent, false);
        }
        TextView textStation = convertView.findViewById(R.id.tvItemJson);
        textStation.setText(item.getName());
        return convertView;
    }
}
