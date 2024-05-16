package test;

import station.AdjacentStation;
import station.Station;

import java.io.IOException;
import java.util.*;

public class Test3 extends Test2 {
    // 用来存储所有找到的路径及其总距离
    private ArrayList<List<Station>> allPaths = new ArrayList<>();
    private Map<List<Station>, Double> allPathAndDistances = new HashMap<>();

    @Override
    public void test() throws IOException {
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
        this.dfs(path, startStation, destinationStation, visited);
        for (List<Station> P : allPaths) {
            System.out.print("<");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < P.size(); i++) {
                sb.append(P.get(i).getName()).append("、");
            }
            sb.setLength(sb.length() - 1);//移除最后一个顿号
            String transfor = sb.toString();
            System.out.println(transfor + ">");
            //System.out.println(String.format("%.3f", allPathAndDistances.get(P)));//可以输出总距离
        }
    }

    public void dfs(List<Station> path, Station current, Station destination, Set<Station> visited) {
        // 将当前车站添加到路径中
        path.add(current);
        if (current.equals(destination)) {
            double totalDistance = calculateTotalDistance(path);
            ArrayList<List<Station>> copyedAllPaths = new ArrayList<>(allPaths);
            boolean isNewPath = true;
            // 检查新路径与已记录路径的重复站点
            for (List<Station> existingPath : copyedAllPaths) {
                if (hasDuplicateStations(path, existingPath)) {
                    double existingDistance = allPathAndDistances.get(existingPath);
                    // 如果新路径的总距离更短，则替换已记录路径
                    if (totalDistance < existingDistance) {
                        allPaths.remove(existingPath);
                        allPathAndDistances.remove(existingPath);
                    } else {
                        isNewPath = false;
                        break;
                    }
                }
            }
            if (isNewPath) {
                allPaths.add(new ArrayList<>(path));
                allPathAndDistances.put(new ArrayList<>(path), totalDistance);
            }
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

    // 检查两条路径是否有重复站点
    private boolean hasDuplicateStations(List<Station> path1, List<Station> path2) {
        Set<Station> set1 = new HashSet<>(path1.subList(1, path1.size() - 1)); // 排除起始站和终点站
        Set<Station> set2 = new HashSet<>(path2.subList(1, path2.size() - 1)); // 排除起始站和终点站
        set1.retainAll(set2); // 保留共同元素
        return !set1.isEmpty();
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
