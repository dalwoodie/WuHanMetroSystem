package test;

import station.Station;

import java.io.IOException;
import java.util.*;

public class Test4 extends Test3 {
    public void test4() throws IOException{
        this.scannerAndDFS();
        List<Station> shortestPath = getShortestPath();
        if (shortestPath != null) {
            System.out.println("最短路径为：");
            for (Station station : shortestPath) {
                System.out.println(station.getName());
            }
        } else {
            System.out.println("未找到最短路径！");
        }
    }

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
