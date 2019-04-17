package fr.wildcodeschool.metro;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<StationMetro> mStationMetro;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FavoritesAdapter(List<StationMetro> stationMetro) {
        this.mStationMetro = stationMetro;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_layout, parent, false);
        FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(view);
        return favoritesViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        // - get element from your dataset at this position

        // - replace the contents of the view with that element
        holder.mStationName.setText("Station name : " + mStationMetro.get(position).getName());
        holder.mStationLine.setText("Tube Line : ");
        holder.mDistance.setText("Distance : " + mStationMetro.get(position).getDistance() + "meters");
        holder.mTubeSchedule.setText("Next Train : ");
        holder.mInfoMessage.setText("Line info : ");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mStationMetro.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView mStationName;
        public TextView mStationLine;
        public TextView mDistance;
        public TextView mTubeSchedule;
        public TextView mInfoMessage;

        public FavoritesViewHolder(View favoritesView) {
            super(favoritesView);
            mStationName = favoritesView.findViewById(R.id.tvStationName);
            mStationLine = favoritesView.findViewById(R.id.tvStationLine);
            mDistance = favoritesView.findViewById(R.id.tvDistance);
            mTubeSchedule = favoritesView.findViewById(R.id.tvTubeSchedule);
            mInfoMessage = favoritesView.findViewById(R.id.tvInfoMessage);
        }
    }
}

