package fr.wildcodeschool.metro;

import android.location.Location;

public class SingletonLocation {

    private static SingletonLocation locationInstance;
    private UserLocation userLocation;

    private SingletonLocation() {
    }

    public static SingletonLocation getLocationInstance() {
        if (locationInstance == null) {
            locationInstance = new SingletonLocation();
        }
        return locationInstance;
    }

    public void openUserLocation(Location location) {
        userLocation = new UserLocation(location);
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location location) {
        userLocation.setLocation(location);
    }
}
