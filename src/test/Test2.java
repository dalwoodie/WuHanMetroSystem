/**
 * 输入某一站点，输出线路距离小于 n 的所有站点集合，包含站点名称、所在线路、距离给定站点的距离（输入不合规时进行异常处理）
 * （例如：华中科技大学站，距离为 1 的站点为<<珞雄路站，2号线，1>, <光谷大道站，2号线，1>>）
**/
package test;

import station.AdjacentStation;
import station.Station;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 extends Test1{
    private Map<Station,Set<AdjacentStation>> stationAndNext = new HashMap<>(); // 记录站点及其所有相邻站
    private Map<Station, Double> distanceMap = new HashMap<>(); // 记录站点及其距离起始站的最短距离

    public void test2() throws IOException{
        this.readtxt1(); // 获取TransforStationlist的数据
        this.readtxt2();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入站点名：");
        String startStationName = scanner.nextLine();
        System.out.println("请输入距离n：");
        double n = scanner.nextDouble();
        Station startStation = this.findStationByName(stationAndNext.keySet(), startStationName); // 查找起始站点
        if (startStation == null) {
            System.out.println("起始站点未找到！");
            return;
        }
        this.dijkstra(stationAndNext, startStation, n); // 执行Dijkstra
        for (Map.Entry<Station, Double> entry : distanceMap.entrySet()) {
            String station = entry.getKey().getName();
            System.out.print("<" + station + ", ");
            StringBuilder sb = new StringBuilder();
            for (String line : getTransforStationlist().get(station)) {
                sb.append(line).append("、");
            }
            sb.setLength(sb.length() - 1);
            sb.append(", ");
            String transfor = sb.toString();
            System.out.print(transfor);
            System.out.println(String.format("%.3f", entry.getValue()) + ">"); // 输出3位小数，因为double在计算过程中因为进制转换而丢失精度
        }
    }
    // Dijkstra搜索
    public void dijkstra(Map<Station, Set<AdjacentStation>> stationAndNext, Station startStation, double n) {
        PriorityQueue<AdjacentStation> pq = new PriorityQueue<>(); // 建立优先级队列
        pq.add(new AdjacentStation(startStation, 0.0));
        distanceMap.put(startStation, 0.0);
        while (!pq.isEmpty()) {
            AdjacentStation current = pq.poll(); // 将队列中第1位赋值给current并从队列中删除
            Station currentStation = current.getStation();
            double currentDistance = current.getDistance();
            if (currentDistance >= n) {
                continue; // 如果获取的站点距离不小于输入距离则重新开始循环
            }
            for (AdjacentStation adjacent : stationAndNext.get(currentStation)) { // 遍历当前车站所有的相邻站
                Station nextStation = adjacent.getStation();
                double newDist = currentDistance + adjacent.getDistance(); // 更新累计距离
                // 从distanceMap中查找相邻的下一站距离
                if (newDist < distanceMap.getOrDefault(nextStation, Double.MAX_VALUE) && newDist < n) {
                    distanceMap.put(nextStation, newDist); // 如果更小则添加或更新distanceMap
                    pq.add(new AdjacentStation(nextStation, newDist)); // 往队列中添加下一站
                }
            }
        }
        distanceMap.remove(startStation);
    }
    // 通过站点名来查找站点并返回
    public Station findStationByName (Set <Station> stations, String name){
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }
    // 读取文件往stationAndNext中添加数据
    public void readtxt2() throws IOException {
        FileReader subwaytxt = new FileReader("subway.txt");
        int sub;
        while ((sub = subwaytxt.read()) != -1) {
            this.addline((char) sub);
            this.addbuffer((char) sub);
            String buffer = this.getbuffer();
            if (this.check(buffer, buffer.lastIndexOf("---"), "---")) {
                this.addstation(buffer,"---");
            }
            if (this.check(buffer, buffer.lastIndexOf("—"), "—")) {
                this.addstation(buffer,"—");
            }
        }
        stationAndNext.remove(null);
        subwaytxt.close();
    }
    // 获得距离
    public double getdistance(String buffer){
        Pattern pattern = Pattern.compile("(?s).*(\\t[0-9](\\.[0-9]+)?)$");
        Matcher matcher = pattern.matcher(buffer);
        matcher.find();
        String lastMatch = matcher.group(1);
        return Double.parseDouble(lastMatch);
    }
    // 添加站点
    public void addstation(String buffer, String connector){
        Station Station1 = new Station(this.getStationLeft(buffer, buffer.lastIndexOf(connector)));
        Station Station2 = new Station(this.getStationRight(buffer, buffer.lastIndexOf(connector), connector));
        if (!stationAndNext.containsKey(Station1)) {
            stationAndNext.put(Station1, new TreeSet<>());
        }
        stationAndNext.get(Station1).add(new AdjacentStation(Station2,this.getdistance(buffer)));
        if (!stationAndNext.containsKey(Station2)) {
            stationAndNext.put(Station2, new TreeSet<>());
        }
        stationAndNext.get(Station2).add(new AdjacentStation(Station1,this.getdistance(buffer)));
    }

    public Map<Station, Set<AdjacentStation>> getStationAndNext() {
        return stationAndNext;
    }
    public Map<Station, Double> getDistanceMap() {
        return distanceMap;
    }
}
