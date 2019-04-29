package fr.wildcodeschool.metro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerAdapterStation extends RecyclerView.Adapter<RecyclerAdapterStation.ViewHolder> {

    private static String mUserID;
    List<StationMetro> stationsList;
    private FirebaseAuth mAuth;

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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final StationMetro stationmodel = stationsList.get(i);
        viewHolder.mStationName.setText(stationmodel.getName());
        viewHolder.mStationLine.setText("");
        viewHolder.mDistance.setText(String.format(viewHolder.mDistance.getContext().getString(R.string.distance_metro), Integer.toString(stationmodel.getDistance())));
        viewHolder.btSchedule.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent goSchedule = new Intent(v.getContext(), StopSchedule.class);
                goSchedule.putExtra("STATION_ID", stationmodel.getId());
                v.getContext().startActivity(goSchedule);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mUserID = user.getUid();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userIdRef = database.getReference(mUserID).child(stationmodel.getId());
            userIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        viewHolder.btAddFav.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            viewHolder.btAddFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference userIdRef = database.getReference(mUserID).child(stationmodel.getId());
                        userIdRef.setValue(stationmodel);
                        viewHolder.btAddFav.setChecked(true);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.btAddFav.getContext());
                        builder.setTitle(R.string.Important_message);
                        builder.setMessage(R.string.add_favorite);
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                mUserID = user.getUid();
                                database.getReference(mUserID).child(stationmodel.getId()).removeValue();
                            }
                        });
                        builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewHolder.btAddFav.setChecked(true);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        } else {
            viewHolder.btAddFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(v.getContext());
                    builder.setTitle(R.string.Important_message);
                    builder.setMessage(R.string.warning);
                    builder.setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            v.getContext().startActivity(new Intent(viewHolder.btAddFav.getContext(), MainActivity.class));
                        }
                    });

                    builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewHolder.btAddFav.setChecked(false);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mStationName;
        public TextView mStationLine;
        public TextView mDistance;
        public Button btSchedule;
        public ToggleButton btAddFav;

        public ViewHolder(View v) {
            super(v);
            mStationName = v.findViewById(R.id.tvStationName);
            mStationLine = v.findViewById(R.id.tvStationLine);
            mDistance = v.findViewById(R.id.tvDistance);
            btSchedule = v.findViewById(R.id.btSchedule);
            btAddFav = v.findViewById(R.id.btAddFav);
        }
    }
}
