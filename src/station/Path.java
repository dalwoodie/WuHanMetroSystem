package station;

import java.util.ArrayList;
import java.util.List;

public class Path implements Cloneable{
    List<Station> stations = new ArrayList<>();
    double totalDistance = 0.0;

    public Path() {
    }

    public Path(List<Station> stations, double totalDistance) {
        this.stations = stations;
        this.totalDistance = totalDistance;
    }

    // 添加站点和更新总距离
    void addStation(Station station, double distance) {
        stations.add(station);
        totalDistance += distance;
    }

    // 判断路径是否包含目标站点
    boolean containsDestination(Station stopstation) {
        return stations.contains(stopstation);
    }

    public List<Station> getStations() {
        return stations;
    }

    void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String toString() {
        return "Path{stations = " + stations + ", totalDistance = " + totalDistance + "}";
    }
    @Override
    public Path clone() throws CloneNotSupportedException {
        // 调用Object类的clone方法创建新的实例
        Path clone = (Path) super.clone();
        clone.stations = new ArrayList<>(this.stations); // 假设List是可变的，需要深拷贝
        clone.totalDistance = this.totalDistance;
        return clone;
    }
}
