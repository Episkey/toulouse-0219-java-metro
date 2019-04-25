package fr.wildcodeschool.metro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustomInfoMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoMarkerAdapter(Context mContext) {
        this.mContext = mContext;
        this.mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_marker_info, null);
    }

    private void rendowWindowText(Marker marker, View view) {
        final String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.title);
        if (!title.equals("")) {
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = view.findViewById(R.id.snippet);
        if (!snippet.equals("")) {
            tvSnippet.setText(snippet);
        }
        Switch addToFavorite = view.findViewById(R.id.swFavorite);
        addToFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {
                    // The toggle is disabled
                }
            }
        });

    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
