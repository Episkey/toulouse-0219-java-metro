package fr.wildcodeschool.metro;

import android.location.Location;

public class StationMetro {

    private String name;
    private double latitude;
    private double longitude;
    private int distance = 0;

    public StationMetro(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        Location locationStation = new Location("");
        locationStation.setLatitude(latitude);
        locationStation.setLongitude(longitude);
        return locationStation;
    }

    public int getDistance() {return this.distance; }

    public void setDistance(int distance) { this.distance = distance; }
}
