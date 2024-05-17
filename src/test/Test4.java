package test;

import station.Station;

import java.io.IOException;
import java.util.*;

public class Test4 extends Test3 {
    public void test4() throws IOException{
        this.scannerAndDFS();
        List<Station> shortestPath = getShortestPath();
        if (shortestPath != null) {
            System.out.print("最短路径为：<");
            StringBuilder sb = new StringBuilder();
            for (Station station : shortestPath) {
                sb.append(station.getName()).append("、");
            }
            sb.setLength(sb.length() - 1);
            String transform = sb.toString();
            System.out.println(transform + ">");
        } else {
            System.out.println("未找到最短路径！");
        }
    }
    // 在allPaths中找到最短路径并返回
    public List<Station> getShortestPath() {
        List<Station> shortestPath = null;
        double shortestDistance = Double.MAX_VALUE;
        for (List<Station> path : getAllPaths()) {
                double distance = getAllPathAndDistances().get(path);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    shortestPath = path;
                }
        }
        return shortestPath;
    }
}
