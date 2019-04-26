package fr.wildcodeschool.metro;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<StationMetro> mStationMetro;

    public FavoritesAdapter(List<StationMetro> stationMetro) {
        this.mStationMetro = stationMetro;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_layout, parent, false);
        FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(view);
        return favoritesViewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoritesViewHolder holder, int position) {
        holder.mStationName.setText(mStationMetro.get(position).getName());
        holder.mStationLine.setText("");
        holder.mDistance.setText(String.format(holder.mDistance.getContext().getString(R.string.distance), mStationMetro.get(position).getDistance()));
        holder.mTubeSchedule.setText("");
        holder.btDeleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Remove from database
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStationMetro.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        public TextView mStationName;
        public TextView mStationLine;
        public TextView mDistance;
        public TextView mTubeSchedule;
        public Button btDeleteFav;

        public FavoritesViewHolder(View favoritesView) {
            super(favoritesView);
            mStationName = favoritesView.findViewById(R.id.tvStationName);
            mStationLine = favoritesView.findViewById(R.id.tvStationLine);
            mDistance = favoritesView.findViewById(R.id.tvDistance);
            mTubeSchedule = favoritesView.findViewById(R.id.tvTubeSchedule);
            btDeleteFav = favoritesView.findViewById(R.id.btAddFav);
        }
    }
}

