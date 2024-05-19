package test;

import station.Station;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test5 extends Test4{
    public void test5() throws IOException {
        this.readtxt1();
        this.scannerAndDFS();
        List<Station> selectedPath = getShortestPath();
        System.out.println(selectedPath);
        if (selectedPath == null || selectedPath.isEmpty()) {
            System.out.println("路径为空。");
            return;
        }
        int i = 0;
        List<String> nowLine = new ArrayList<>();
        Set<String> sameLine = new HashSet<>(getTransforStationlist().get(selectedPath.get(0).getName()));

        for (; i < selectedPath.size() - 1; i++){
            String current = selectedPath.get(i).getName();
            String next = selectedPath.get(i + 1).getName();
            Set<String> currentLines = new HashSet<>(getTransforStationlist().get(current));
            Set<String> nextLines = new HashSet<>(getTransforStationlist().get(next));

            currentLines.retainAll(nextLines);
            sameLine.retainAll(currentLines);
            if (sameLine.isEmpty()){
                System.out.print("乘" + nowLine.get(0) + ":[" + selectedPath.get(0) + "，" + current + "]，");
                sameLine.clear();
                sameLine.addAll(getTransforStationlist().get(selectedPath.get(i).getName()));
                break;
            }
            nowLine.clear();
            nowLine.addAll(sameLine);
        }
        this.printTransfer(selectedPath, i, nowLine, sameLine);
    }

    public void printTransfer(List<Station> selectedPath, int i, List<String> nowLine, Set<String> sameLine) {
        String start = selectedPath.get(i).getName();
        for (; i < selectedPath.size() - 1; i++){
            String current = selectedPath.get(i).getName();
            String next = selectedPath.get(i + 1).getName();
            Set<String> currentLines = new HashSet<>(getTransforStationlist().get(current));
            Set<String> nextLines = new HashSet<>(getTransforStationlist().get(next));

            currentLines.retainAll(nextLines);
            sameLine.retainAll(currentLines);
            if (sameLine.isEmpty() && i < selectedPath.size() - 2){
                System.out.print("换乘" + nowLine.get(0) + ":[" + start + "，" + current + "]，");
                sameLine.clear();
                sameLine.addAll(getTransforStationlist().get(selectedPath.get(i).getName()));
                printTransfer(selectedPath, i, nowLine, sameLine);
                break;
            } else if(i == selectedPath.size() - 2){
                System.out.println("换乘" + nowLine.get(0) + ":[" + start + "，" + next + "]");
                break;
            }
            nowLine.clear();
            nowLine.addAll(sameLine);
        }
    }
}
