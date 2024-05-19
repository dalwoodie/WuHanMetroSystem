/**
 * 输入起点站和终点站的名称，返回一个包含连接起点和终点的所有路径的集合，每条路径经过的站点不重复（即不包含环路）
**/
package test;

import station.AdjacentStation;
import station.Station;

import java.io.IOException;
import java.util.*;

public class Test3 extends Test2 {
    private List<List<Station>> allPaths = new ArrayList<>(); // 存储所有找到的路径
    private Map<List<Station>, Double> allPathAndDistances = new HashMap<>(); // 存储所有路径及其总距离
    private Map<Station, Double> shortestDistanceCache = new HashMap<>(); // 缓存每个站点到目的站点的最短距离
    private Set<Station> visited = new HashSet<>(); // 记录已访问的站点

    public void test3() throws IOException {
        this.scannerAndDFS();
        this.printAllPaths();
    }
    // 输入并执行DFS
    public void scannerAndDFS() throws IOException {
        this.readtxt2();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入起始站站点名：");
        String start = scanner.nextLine();
        System.out.println("请输入终点站站点名：");
        String destination = scanner.nextLine();

        Station startStation = findStationByName(getStationAndNext().keySet(), start);
        Station destinationStation = findStationByName(getStationAndNext().keySet(), destination);
        if (startStation != null && destinationStation != null) {
            // 循环执行DFS，次数最多为两站点相邻站数量的最小值maxSearch
            int maxSearch = Math.min(getStationAndNext().get(startStation).size(), getStationAndNext().get(destinationStation).size());
            for (int i = 0; i < maxSearch; i++) {
                List<List<Station>> allPath = new ArrayList<>();
                dfs(new ArrayList<>(), startStation, destinationStation, 0, allPath);
                shortestDistanceCache.clear();
                if (allPath.size() != 0 && allPaths.size() < maxSearch){
                    for (List<Station> path : allPath) {
                        for (int j = 1; j < path.size()-1; j++) {
                            visited.add(path.get(j));
                        }
                        if (!allPaths.contains(path)) {
                            allPaths.add(path);
                        }
                    }
                } else break; // 这一轮获取的路径集合为空或总路径集合中路径数达到maxSearch时结束循环
            }
        } else {
            System.out.println("起始站点或终点站点未找到！");
        }
    }
    // DFS搜索
    public void dfs(List<Station> path, Station current, Station destination, double currentDistance, List<List<Station>> allPath) {
        path.add(current); // 将当前站点添加到路径中
        if (current.equals(destination)) { // 如果当前站点是目的站点
            this.updatePaths(path, currentDistance, allPath); // 更新路径集合和距离
        } else {
            visited.add(current); // 将当前站点标记为已访问
            for (AdjacentStation adjacent : getStationAndNext().get(current)) { // 遍历当前站点的相邻站点
                Station nextStation = adjacent.getStation();
                double newDistance = currentDistance + adjacent.getDistance(); // 计算新的距离
                if (!visited.contains(nextStation) && !shouldPrune(nextStation, newDistance)) { // 如果相邻站点未被访问且不需要剪枝
                    dfs(path, nextStation, destination, newDistance, allPath); // 递归调用DFS
                }
            }
            visited.remove(current); // 回溯时取消标记当前站点为已访问
        }
        path.remove(path.size() - 1); // 回溯时移除当前站点
    }
    // 判断是否需要剪枝
    private boolean shouldPrune(Station nextStation, double newDistance) {
        if (shortestDistanceCache.containsKey(nextStation)) { // 如果缓存中有下一站点的最短距离
            double cachedDistance = shortestDistanceCache.get(nextStation);
            // 如果新的距离大于等于缓存的距离
            if (newDistance >= cachedDistance) return true; // 则需要剪枝
        }
        return false; // 否则不需要剪枝
    }
    // 更新路径集合和距离
    public void updatePaths(List<Station> path, double totalDistance, List<List<Station>> allPath) {
        Iterator<List<Station>> iter = allPath.iterator();
        while (iter.hasNext()) {
            List<Station> existingPath = iter.next();
            if (hasDuplicateStations(path, existingPath)) { // 如果存在相同的经过站点
                double existingDistance = allPathAndDistances.get(existingPath);
                if (totalDistance < existingDistance) { // 如果当前路径的距离更短
                    iter.remove(); // 移除原有路径
                    allPathAndDistances.remove(existingPath); // 移除原有路径的距离
                } else
                    return; // 不添加新路径
            }
        }
        allPath.add(new ArrayList<>(path)); // 添加新路径
        allPathAndDistances.put(new ArrayList<>(path), totalDistance); // 添加新路径的距离
        // 更新缓存
        for (Station station : path) {
            shortestDistanceCache.put(station, Math.min(shortestDistanceCache.getOrDefault(station, Double.MAX_VALUE), totalDistance));
        }
    }
    // 判断两条路径是否有重复站点
    private boolean hasDuplicateStations(List<Station> path1, List<Station> path2) {
        Set<Station> set1 = new HashSet<>(path1.subList(1, path1.size() - 1)); // 排除起始站和终点站
        Set<Station> set2 = new HashSet<>(path2.subList(1, path2.size() - 1));
        set1.retainAll(set2); // 保留共同元素
        return !set1.isEmpty(); // 如果存在共同元素，则返回true，表示有重复站点
    }
    // 打印所有找到的路径
    public void printAllPaths() {
        int i = 1;
        for (List<Station> P : allPaths) {
            System.out.print("线路" + i + "：<");
            i++;
            StringBuilder sb = new StringBuilder();
            for (Station station : P) {
                sb.append(station.getName()).append("、");
            }
            sb.setLength(sb.length() - 1);
            String transform = sb.toString();
            System.out.println(transform + ">");
        }
    }

    public List<List<Station>> getAllPaths() { return allPaths; }

    public Map<List<Station>, Double> getAllPathAndDistances() { return allPathAndDistances;}

    public Map<Station, Double> getShortestDistanceCache() { return shortestDistanceCache; }

    public Set<Station> getVisited() { return visited; }
}
