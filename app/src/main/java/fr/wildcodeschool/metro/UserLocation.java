package fr.wildcodeschool.metro;

import android.location.Location;

public class UserLocation {

    private Location location;

    public UserLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
