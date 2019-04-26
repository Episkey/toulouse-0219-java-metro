package fr.wildcodeschool.metro;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Math.round;

public class Favorites extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StationMetro> mStationMetro = new ArrayList<>();
    private FirebaseAuth mAuth;
    private static String mUserID;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        final Location locationUser = intent.getParcelableExtra("mLocationUser");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mRecyclerView = findViewById(R.id.favorites_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        if (user != null) {
            mUserID = user.getUid();

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userIdRef = database.getReference(mUserID);
            userIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot stationSnapshot : dataSnapshot.getChildren()) {
                        StationMetro station = stationSnapshot.getValue(StationMetro.class);
                        int distance = round(locationUser.distanceTo(station.getLocation()));
                        station.setDistance(distance);
                        mStationMetro.add(station);
                    }
                    Collections.sort(mStationMetro, new Comparator<StationMetro>() {
                        public int compare(StationMetro o1, StationMetro o2) {
                            return o2.getDistance() < o1.getDistance() ? 1 : -1;
                        }
                    });
                    mAdapter = new FavoritesAdapter(mStationMetro);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Favorites.this);
            builder.setTitle(R.string.Important_message);
            builder.setMessage(R.string.must_sign_in);
            builder.setPositiveButton(R.string.sign_in, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Favorites.this, MainActivity.class));
                }
            });
            builder.setNegativeButton(R.string.decline, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
    }
}
