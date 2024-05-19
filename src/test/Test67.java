/**
 * 输入给定起点、终点后，用户从路径集合中选择一条路径，系统计算该路径对应的乘车费用（普通单程票）
 * 对于同样的路径，计算使用武汉通和日票的乘客的票价（日票则返回0元）
**/
package test;

import station.Station;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Test67 extends Test5{
    public void test6() throws IOException {
        this.scannerAndDFS();
        this.printAllPaths();
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入线路数字（例如选择线路1则输入”1“）：");
        int num = sc.nextInt();
        if (num > getAllPaths().size()){
            System.out.println("输入错误！");
        }
        List<Station> selectedPath = getAllPaths().get(num - 1);
        double distance = getAllPathAndDistances().get(selectedPath);
        System.out.println("该路径普通单程票票价为：" + (price(distance) - price(distance)%1)); //减掉票价后多余的小数
        System.out.println("该路径武汉通票价为：" + (price(distance) - price(distance)%1) * 0.9);
        System.out.println("该路径日票票价为：" + 0);
    }
    // 计算票价，不足公里数按完整公里数计算
    public double price(double distance){
        if (distance <= 4) return 2;
        else if (distance <= 12) return 3 + (distance - 4) / 4;
        else if (distance <= 24) return 5 + (distance - 12) / 6;
        else if (distance <= 40) return 7 + (distance - 24) / 8;
        else if (distance <= 50) return 9;
        else return 10 + (distance - 50) / 20;
    }
}
