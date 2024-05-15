package station;

public class AdjacentStation implements Comparable<AdjacentStation> {
    private Station station;
    private double distance;

    public AdjacentStation() {
    }

    public AdjacentStation(Station station, double distance) {
        this.station = station;
        this.distance = distance;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    @Override
    public int compareTo(AdjacentStation other) {
        return Double.compare(this.distance, other.distance);
    }
}
