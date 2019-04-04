package fr.wildcodeschool.metro;
public class StationMetro {

    private String name;
    private double latStation;
    private double lngStation;

    public StationMetro(String name, double latStation, double lngStation) {
        this.name = name;
        this.latStation = latStation;
        this.lngStation = lngStation;
    }

    public String getName() {
        return name;
    }

    public double getLatStation() {
        return latStation;
    }

    public double getLngStation() {
        return lngStation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatStation(double latStation) {
        this.latStation = latStation;
    }

    public void setLngStation(double lngStation) {
        this.lngStation = lngStation;
    }
}
