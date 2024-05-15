package test;

import station.AdjacentStation;
import station.Station;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 extends Test1{
    private Map<Station,Set<AdjacentStation>> stationAndNext = new HashMap<>();

    public Test2() {
    }

    public Test2(Map<Station, Set<AdjacentStation>> stationAndNext) {
        this.stationAndNext = stationAndNext;
    }

    @Override
    public void test() throws IOException{
        this.readtxt1();
        this.readtxt2();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入站点名：");
        String startStationName = scanner.nextLine();
        System.out.println("请输入距离n：");
        double n = scanner.nextDouble();
        // 查找起始站点
        Station startStation = this.findStationByName(stationAndNext.keySet(), startStationName);
        if (startStation == null) {
            System.out.println("起始站点未找到！");
            return;
        }
        // 执行BFS搜索
        this.bfs(stationAndNext, startStation, n);
    }

    public boolean check(Set<Station> check,Station station){
        for (Station s1 : check) {
            if (station.equals(s1)){
                return false;
            }
        }
        return true;
    }

    public void bfs(Map<Station, Set<AdjacentStation>> stationAndNext, Station startStation, double n) {
        LinkedList<Station> queue = new LinkedList<>();
        Set<Station> check = new HashSet<>();
        Map<Station, Double> distanceMap = new HashMap<>(); // 记录每个车站的最小累计距离
        queue.offer(startStation);
        distanceMap.put(startStation, 0.0);
        check.add(startStation);

        while (!queue.isEmpty()) {
            Station currentStation = queue.poll();
            check.add(currentStation);
            double currentDistance = distanceMap.get(currentStation);
            for (AdjacentStation adjacent : stationAndNext.get(currentStation)) {
                Station nextStation = adjacent.getStation();
                double nextDistance = Math.round((currentDistance + adjacent.getDistance()) * 1000.0) / 1000.0;
                if (nextDistance < n && this.check(check,nextStation)) {
                    check.add(adjacent.getStation());
                    queue.offer(nextStation);
                    distanceMap.put(nextStation, nextDistance);
                }
            }
        }
        distanceMap.remove(startStation);
        for (Map.Entry<Station, Double> entry : distanceMap.entrySet()) {
            if (entry.getValue() < n) {
                String station = entry.getKey().getName();
                System.out.print("<" + station + ", ");
                StringBuilder sb = new StringBuilder();
                for (String line : getTransforStationlist().get(station)) {
                    sb.append(line).append("、");
                }
                sb.setLength(sb.length() - 1);//移除最后一个顿号
                sb.append(", ");
                String transfor = sb.toString();
                System.out.print(transfor);
                System.out.println( entry.getValue() + ">");
            }
        }
    }

    public Station findStationByName (Set < Station > stations, String name){
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }

    public void readtxt2() throws IOException {
        FileReader subwaytxt = new FileReader("C:\\Users\\zhong\\source\\wuhansubwaysystem\\subway.txt");
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

    public double getdistance(String buffer){
        Pattern pattern = Pattern.compile("(?s).*(\\t[0-9](\\.[0-9]+)?)$");
        Matcher matcher = pattern.matcher(buffer);
        matcher.find();
        String lastMatch = matcher.group(1);
        return Double.parseDouble(lastMatch);
    }

    public void addstation(String buffer, String connector){
        Station Station1 = new Station(this.getStationLeft(buffer, buffer.lastIndexOf(connector)));
        Station Station2 = new Station(this.getStationRight(buffer, buffer.lastIndexOf(connector), connector));

        // 如果键不在Map中，则创建一个新的HashSet并添加到Map中
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

    public void setStationAndNext(Map<Station, Set<AdjacentStation>> stationAndNext) {
        this.stationAndNext = stationAndNext;
    }
}
