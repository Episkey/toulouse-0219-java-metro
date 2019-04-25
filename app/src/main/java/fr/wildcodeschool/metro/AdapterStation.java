package fr.wildcodeschool.metro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterStation extends ArrayAdapter<StationMetro> {
    Location mLocUser;
    private FirebaseAuth mAuth;
    private static String mUserID;

    public AdapterStation(Context context, List<StationMetro> stationMetro, Location locationUser) {
        super(context, 0, stationMetro);
        mLocUser = locationUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final StationMetro item = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_for_adapter, parent, false);
        }
        TextView textStation = convertView.findViewById(R.id.tvItemJson);
        textStation.setText(item.getName());
        TextView textDistance = convertView.findViewById(R.id.tvDistance);
        textDistance.setText(String.format(convertView.getContext().getString(R.string.meters), item.getDistance()));
        Button addToFavorite = convertView.findViewById(R.id.btAddToFavorite);
        addToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    mUserID = user.getUid();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userIdRef = database.getReference(mUserID);
                    userIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int redundancy = 0;
                            for (DataSnapshot stationSnapshot : dataSnapshot.getChildren()) {
                                StationMetro station = stationSnapshot.getValue(StationMetro.class);
                                if (station.getName().equals(item.getName())) {
                                    redundancy = 1;
                                    break;
                                }
                            }

                            if (redundancy == 0) {
                                DatabaseReference stationRef = database.getReference(mUserID);
                                stationRef.push().setValue(item);
                                Toast.makeText(getContext(), String.format(getContext().getString(R.string.station_added), item.getName()), Toast.LENGTH_LONG).show();

                            } else {
                                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(v.getContext());
                                builder.setTitle(R.string.Important_message);
                                builder.setMessage(R.string.already_in_your_fav);
                                builder.setNegativeButton(R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                } else {
                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(v.getContext());
                    builder.setTitle(R.string.Important_message);
                    builder.setMessage(R.string.warning);
                    builder.setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                        }
                    });
                    builder.setNegativeButton(R.string.decline, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
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