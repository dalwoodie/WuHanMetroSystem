package test;

import station.AdjacentStation;
import station.Station;

import java.io.IOException;
import java.util.*;

public class Test3 extends Test2{
    // 用来存储所有找到的路径及其总距离
    ArrayList<List<Station>> allPaths = new ArrayList<>();
    Map<List<Station>, Double> pathDistances = new HashMap<>();

    @Override
    public void test()throws IOException {
        this.readtxt2();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始站站点名：");
        String start = scanner.nextLine();
        System.out.println("请输入终点站站点名：");
        String destination = scanner.nextLine();
        Station startStation = this.findStationByName(getStationAndNext().keySet(), start);
        if (startStation == null) {
            System.out.println("起始站点未找到！");
            return;
        }
        Station destinationStation = this.findStationByName(getStationAndNext().keySet(), destination);
        if (destinationStation == null) {
            System.out.println("终点站点未找到！");
            return;
        }



    }

    // 深度优先搜索来找到所有路径
    public void dfs(List<Station> path, Station current, Station destination, Set<Station> visited) {
        path.add(current); // 将当前车站添加到路径中

        if (current.equals(destination)) {
            // 到达目的地，记录路径和距离
            double totalDistance = calculateTotalDistance(path);
            allPaths.add(new ArrayList<>(path));
            pathDistances.put(new ArrayList<>(path), totalDistance);
        } else {
            visited.add(current); // 标记当前车站为已访问

            // 遍历所有相邻车站
            for (AdjacentStation adjacent : getStationAndNext().getOrDefault(current, Collections.emptySet())) {
                if (!visited.contains(adjacent.getStation())) {
                    dfs(path, adjacent.getStation(), destination, visited);
                }
            }
            visited.remove(current); // 回溯时取消标记
        }
        path.remove(path.size() - 1); // 回溯时移除当前车站
    }

    // 计算路径的总距离
    public double calculateTotalDistance(List<Station> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Station current = path.get(i);
            Station next = path.get(i + 1);
            for (AdjacentStation adjacent : getStationAndNext().getOrDefault(current, Collections.emptySet())) {
                if (adjacent.getStation().equals(next)) {
                    total += adjacent.getDistance();
                    break;
                }
            }
        }
        return total;
    }

    // 找到并返回最短路径
    public List<Station> findShortestPath(Station start, Station destination) {
        if (start == null || destination == null) return null;
        List<Station> path = new ArrayList<>();
        Set<Station> visited = new HashSet<>();

        dfs(path, start, destination, visited);

        // 如果没有找到路径，返回null
        if (allPaths.isEmpty()) return null;

        // 筛选距离最短的路径
        List<Station> shortestPath = allPaths.get(0);
        double shortestDistance = pathDistances.get(shortestPath);
        for (List<Station> p : allPaths) {
            double distance = pathDistances.get(p);
            if (distance < shortestDistance) {
                shortestPath = p;
                shortestDistance = distance;
            }
        }
        return shortestPath;
    }
}
