package fr.wildcodeschool.metro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private static String mUserID;
    private FirebaseAuth mAuth;
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
        final StationMetro station = mStationMetro.get(position);
        holder.mStationName.setText(station.getName());
        holder.mStationLine.setText("");
        holder.mDistance.setText(String.format(holder.mDistance.getContext().getString(R.string.distance), station.getDistance()));
        holder.btSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSchedule = new Intent(v.getContext(), StopSchedule.class);
                goSchedule.putExtra("STATION_ID", station.getId());
                v.getContext().startActivity(goSchedule);
            }
        });
        holder.btDeleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.Important_message);
                builder.setMessage(R.string.add_favorite);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        mUserID = user.getUid();
                        database.getReference(mUserID).child(station.getId()).removeValue();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                AlertDialog dialog = builder.create();
                dialog.show();
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
        public Button btDeleteFav;
        public Button btSchedule;

        public FavoritesViewHolder(View favoritesView) {
            super(favoritesView);
            mStationName = favoritesView.findViewById(R.id.tvStationName);
            mStationLine = favoritesView.findViewById(R.id.tvStationLine);
            mDistance = favoritesView.findViewById(R.id.tvDistance);
            btSchedule = favoritesView.findViewById(R.id.btSchedule);
            btDeleteFav = favoritesView.findViewById(R.id.btAddFav);
        }
    }
}