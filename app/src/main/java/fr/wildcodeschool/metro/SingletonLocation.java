package fr.wildcodeschool.metro;

import android.location.Location;

public class SingletonLocation {

    private static SingletonLocation locationInstance;

    public static SingletonLocation getLocationInstance() {
        if(locationInstance == null) {
            locationInstance = new SingletonLocation();
        }
        return locationInstance;
    }

    private SingletonLocation() {}

    private UserLocation userLocation;

    public void openUserLocation(Location location) {
        userLocation = new UserLocation(location);
    }

    public void setUserLocation(Location location) {
        userLocation.setLocation(location);
    }

    public UserLocation getUserLocation(){
        return userLocation;
    }


}
