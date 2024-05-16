package test;

import station.AdjacentStation;
import station.Station;

import java.io.IOException;
import java.util.*;

public class Test3 extends Test2 {
    // 用来存储所有找到的路径及其总距离
    private ArrayList<List<Station>> allPaths = new ArrayList<>();
    private Map<List<Station>, Double> allPathAndDistances = new HashMap<>();

    public void test3() throws IOException {
        this.scannerAndDFS();
        this.printAllPaths();
    }
    //输入起始站和终点站并进行dfs搜索
    public void scannerAndDFS () throws IOException{
        this.readtxt2();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始站站点名：");
        String start = scanner.nextLine();
        System.out.println("请输入终点站站点名：");
        String destination = scanner.nextLine();

        Station startStation = findStationByName(getStationAndNext().keySet(), start);
        Station destinationStation = findStationByName(getStationAndNext().keySet(), destination);
        if (startStation != null && destinationStation != null) {
            dfs(new ArrayList<>(), startStation, destinationStation, new HashSet<>());
        } else {
            System.out.println("起始站点或终点站点未找到！");
        }
    }

    public void dfs(List<Station> path, Station current, Station destination, Set<Station> visited) {
        // 将当前车站添加到路径中
        path.add(current);
        if (current.equals(destination)) {
            double totalDistance = calculateTotalDistance(path);
            this.updatePaths(path, totalDistance);
        } else {
            visited.add(current); // 标记当前车站为已访问
            // 遍历所有相邻车站
            for (AdjacentStation adjacent : getStationAndNext().get(current)) {
                if (!visited.contains(adjacent.getStation())) {
                    dfs(new ArrayList<>(path), adjacent.getStation(), destination, new HashSet<>(visited));
                }
            }
            visited.remove(current); // 回溯时取消标记
        }
        path.remove(path.size() - 1); // 回溯时移除当前车站
    }

    public void updatePaths(List<Station> path, double totalDistance) {
        Iterator<List<Station>> iter = allPaths.iterator();
        while (iter.hasNext()) {
            List<Station> existingPath = iter.next();
            if (hasDuplicateStations(path, existingPath)) {
                double existingDistance = allPathAndDistances.get(existingPath);
                if (totalDistance < existingDistance) {
                    iter.remove();
                    allPathAndDistances.remove(existingPath);
                } else {
                    return; // 不添加新路径
                }
            }
        }
        allPaths.add(new ArrayList<>(path));
        allPathAndDistances.put(new ArrayList<>(path), totalDistance);
    }

    // 检查两条路径是否有重复站点
    private boolean hasDuplicateStations(List<Station> path1, List<Station> path2) {
        Set<Station> set1 = new HashSet<>(path1.subList(1, path1.size() - 1)); // 排除起始站和终点站
        Set<Station> set2 = new HashSet<>(path2.subList(1, path2.size() - 1)); // 排除起始站和终点站
        set1.retainAll(set2); // 保留共同元素
        return !set1.isEmpty();
    }

    public void printAllPaths() {
        for (List<Station> P : allPaths) {
            System.out.print("<");
            StringBuilder sb = new StringBuilder();
            for (Station station : P) {
                sb.append(station.getName()).append("、");
            }
            sb.setLength(sb.length() - 1); // 移除最后一个顿号
            String transform = sb.toString();
            System.out.println(transform + ">");
        }
    }
    // 计算路径的总距离
    public double calculateTotalDistance(List<Station> path) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Station current = path.get(i);
            Station next = path.get(i + 1);
            for (AdjacentStation adjacent : getStationAndNext().get(current)) {
                if (adjacent.getStation().equals(next)) {
                    total += adjacent.getDistance();
                    break;
                }
            }
        }
        return total;
    }
    public ArrayList<List<Station>> getAllPaths() {
        return allPaths;
    }
    public void setAllPaths(ArrayList<List<Station>> allPaths) {
        this.allPaths = allPaths;
    }
    public Map<List<Station>, Double> getAllPathAndDistances() {
        return allPathAndDistances;
    }
    public void setAllPathAndDistances(Map<List<Station>, Double> allPathAndDistances) {
        this.allPathAndDistances = allPathAndDistances;
    }
}
