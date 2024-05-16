package test;

import station.AdjacentStation;
import station.Station;

import java.io.IOException;
import java.util.*;

public class Test3 extends Test2{
    // 用来存储所有找到的路径及其总距离
    ArrayList<List<Station>> allPaths = new ArrayList<>();
    Map<List<Station>, Double> allPathAndDistances = new HashMap<>();

    public Test3() {
    }

    public Test3(ArrayList<List<Station>> allPaths, Map<List<Station>, Double> allPathAndDistances) {
        this.allPaths = allPaths;
        this.allPathAndDistances = allPathAndDistances;
    }

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
        List<Station> path = new ArrayList<>();
        Set<Station> visited = new HashSet<>();
        this.dfs(path,startStation,destinationStation,visited);
        for (List<Station> P : allPaths) {
            System.out.print("<");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < P.size(); i++) {
                sb.append(P.get(i).getName()).append("、");
            }
            sb.setLength(sb.length() - 1);//移除最后一个顿号
            String transfor = sb.toString();
            System.out.println(transfor + ">");
            System.out.println(String.format("%.3f",allPathAndDistances.get(P)));

        }
    }

    // 深度优先搜索来找到所有路径
    public void dfs(List<Station> path, Station current, Station destination, Set<Station> visited) {
        // 将当前车站添加到路径中
        path.add(current);
        if (current.equals(destination)) {
            // 到达目的地，记录路径和距离
            double totalDistance = calculateTotalDistance(path);
            allPaths.add(new ArrayList<>(path));
            allPathAndDistances.put(new ArrayList<>(path), totalDistance);
            this.findShorterPath();
        }
        else {
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

    //路径中有相同站点则保留较短路径
    public void findShorterPath(){
        ArrayList<Station> currentPath = new ArrayList<>(allPaths.get(allPaths.size()-1));
        //遍历除了当前path外所有的path
        for (int i = 0; i < allPaths.size()- 1; i++){
            ArrayList<Station> path = new ArrayList<>(allPaths.get(i));
            //遍历除了起点终点的所有车站
            for (int j = 1; j < currentPath.size()- 1; j++){
                String currentName = currentPath.get(j).getName();
                for (Station s1 : path) {
                    if (currentName == s1.getName()) {
                        double currentDistance = 0;
                        double distance = 0;
                        for (List<Station> P : allPathAndDistances.keySet()) {
                            if (P == currentPath) {
                                currentDistance = allPathAndDistances.get(P);
                            }
                            if (P == path) {
                                distance = allPathAndDistances.get(P);
                            }
                        }
                        if (currentDistance >= distance) {
                            allPaths.remove(currentPath);
                            allPathAndDistances.remove(currentPath);
                        } else {
                            allPaths.remove(path);
                            allPathAndDistances.remove(path);
                        }
                        break;
                    }
                }
            }
        }
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
