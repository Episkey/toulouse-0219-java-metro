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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    private static String mUserID;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StationMetro> mStationMetro = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onResume() {
        super.onResume();
        SingletonLocation singletonLocation = SingletonLocation.getLocationInstance();
        final UserLocation userLocation = singletonLocation.getUserLocation();
        //Intent intent = getIntent();
        //final Location locationUser = intent.getParcelableExtra("mLocationUser");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mRecyclerView = findViewById(R.id.favorites_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        if (user != null) {
            mUserID = user.getUid();

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userIdRef = database.getReference(mUserID);
            userIdRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mStationMetro.clear();
                    for (DataSnapshot stationSnapshot : dataSnapshot.getChildren()) {
                        StationMetro station = stationSnapshot.getValue(StationMetro.class);
                        int distance = round(userLocation.getLocation().distanceTo(station.getLocation()));
                        station.setDistance(distance);
                        mStationMetro.add(station);
                        //TODO: appel API avec id pour avoir les horaires
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulauncher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btMapView:
                Intent goToMapView = new Intent(Favorites.this, MapsActivity.class);
                startActivity(goToMapView);
                return true;
            case R.id.btListView:
                Intent goToListView = new Intent(Favorites.this, RecycleViewStation.class);
                startActivity(goToListView);
                return true;
            case R.id.itemMenuRegister:
                Intent goToRegisterView = new Intent(Favorites.this, RegisterActivity.class);
                startActivity(goToRegisterView);
                return true;
            case R.id.itemMenuLogin:
                Intent goToMainActivity = new Intent(Favorites.this, MainActivity.class);
                startActivity(goToMainActivity);
                return true;
            case R.id.itemMenuFav:
                Intent goToFavorites = new Intent(Favorites.this, Favorites.class);
                startActivity(goToFavorites);
                return true;
            case R.id.itemMenuLogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
