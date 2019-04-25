package fr.wildcodeschool.metro;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterStation extends RecyclerView.Adapter<RecyclerAdapterStation.ViewHolder> {

    List<StationMetro> stationsList = new ArrayList<>();

    public RecyclerAdapterStation(List<StationMetro> stationList) {
        stationsList = stationList;
    }

    @Override
    public RecyclerAdapterStation.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_station, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StationMetro stationmodel = stationsList.get(i);
        viewHolder.tvStationName.setText(stationmodel.getName().toString());
        //viewHolder.tvNextDeparture.setText(stationmodel.getDeparture());

    }

    @Override
    public int getItemCount() {

        return stationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStationName, tvNextDeparture;

        public ViewHolder(View v) {
            super(v);
            tvStationName = v.findViewById(R.id.tvStationName);
            tvNextDeparture = v.findViewById(R.id.tvNextDeparture);
        }
    }
}
