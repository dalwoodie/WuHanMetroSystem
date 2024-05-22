package wuhanmetrosystemUI;

import station.Station;
import test.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WuhanMetroSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextArea resultAreaStations, resultAreaRoutes;
    private JTextField stationNameField, distanceField, startStationField, endStationField, routeChoiceField;
    private Test1 test1 = new Test1();
    private Test2 test2 = new Test2();
    private Test3 test3 = new Test3();

    public WuhanMetroSystem() {
        setTitle("武汉地铁系统");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initMainScreen();
        initStationQueryScreen();
        initRouteQueryScreen();

        add(mainPanel);
        cardLayout.show(mainPanel, "Main");
        setLocationRelativeTo(null); // Center the window
    }

    private void initMainScreen() {
        JPanel mainScreen = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("武汉地铁系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainScreen.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton stationQueryButton = new JButton("站点查询");
        JButton routeQueryButton = new JButton("线路查询");

        stationQueryButton.addActionListener(e -> cardLayout.show(mainPanel, "StationQuery"));
        routeQueryButton.addActionListener(e -> cardLayout.show(mainPanel, "RouteQuery"));

        buttonPanel.add(stationQueryButton);
        buttonPanel.add(routeQueryButton);

        mainScreen.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(mainScreen, "Main");

        titleLabel.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // 添加菜单
        JMenuBar menuBar = new JMenuBar();
        JMenu aboutMenu = new JMenu("关于");
        JMenuItem authorMenuItem = new JMenuItem("关于作者");
        authorMenuItem.addActionListener(e -> showAuthorInfo());
        aboutMenu.add(authorMenuItem);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
    }

    private void initStationQueryScreen() {
        JPanel stationQueryScreen = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel stationNameLabel = new JLabel("请输入站点名：");
        stationNameField = new JTextField();
        JLabel distanceLabel = new JLabel("请输入距离（km）：");
        distanceField = new JTextField();
        JButton nearbyStationsButton = new JButton("查询附近站点");
        JButton transferStationsButton = new JButton("查询所有中转站");
        JButton backButton = new JButton("返回");

        nearbyStationsButton.addActionListener(e -> queryNearbyStations());
        transferStationsButton.addActionListener(e -> queryTransferStations());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main"));

        inputPanel.add(stationNameLabel);
        inputPanel.add(stationNameField);
        inputPanel.add(distanceLabel);
        inputPanel.add(distanceField);
        inputPanel.add(nearbyStationsButton);
        inputPanel.add(transferStationsButton);
        inputPanel.add(backButton);

        stationQueryScreen.add(inputPanel, BorderLayout.WEST);

        resultAreaStations = new JTextArea();
        resultAreaStations.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultAreaStations);
        stationQueryScreen.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(stationQueryScreen, "StationQuery");
    }

    private void initRouteQueryScreen() {
        JPanel routeQueryScreen = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1));

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        JLabel startStationLabel = new JLabel("请输入起始站站点名：");
        startStationField = new JTextField();
        JLabel endStationLabel = new JLabel("请输入终点站站点名：");
        endStationField = new JTextField();
        topLeftPanel.add(startStationLabel);
        topLeftPanel.add(startStationField);
        topLeftPanel.add(endStationLabel);
        topLeftPanel.add(endStationField);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        JButton allRoutesButton = new JButton("查询所有路径");
        JButton shortestRouteButton = new JButton("选择最短路径");
        JLabel routeChoiceLabel = new JLabel("选择线路（填写线路后的数字）");
        routeChoiceField = new JTextField();
        JButton confirmButton = new JButton("确定");
        JLabel paymentLabel = new JLabel("选择付款方式");
        JComboBox<String> paymentComboBox = new JComboBox<>(new String[]{"普通单程票", "武汉通", "日票"});
        JButton payButton = new JButton("支付");
        JButton backButton = new JButton("返回");

        allRoutesButton.addActionListener(e -> queryAllRoutes());
        shortestRouteButton.addActionListener(e -> queryShortestRoute());
        confirmButton.addActionListener(e -> confirmRouteChoice());
        payButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "支付完成"));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main"));

        topRightPanel.add(allRoutesButton);
        topRightPanel.add(shortestRouteButton);
        topRightPanel.add(routeChoiceLabel);
        topRightPanel.add(routeChoiceField);
        topRightPanel.add(confirmButton);
        topRightPanel.add(paymentLabel);
        topRightPanel.add(paymentComboBox);
        topRightPanel.add(payButton);
        topRightPanel.add(backButton);

        inputPanel.add(topLeftPanel);
        inputPanel.add(topRightPanel);

        routeQueryScreen.add(inputPanel, BorderLayout.NORTH);

        resultAreaRoutes = new JTextArea();
        resultAreaRoutes.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultAreaRoutes);
        routeQueryScreen.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(routeQueryScreen, "RouteQuery");
        setVisible(true);
    }

    private void queryNearbyStations() {
        test2.getStationAndNext().clear();
        test2.getDistanceMap().clear();

        resultAreaStations.setText("");
        String stationName = stationNameField.getText();
        String distance = distanceField.getText();
        try {
            test1.readtxt1();
            test2.readtxt2();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Station startStation = test2.findStationByName(test2.getStationAndNext().keySet(), stationName);
        if (startStation == null || startStation.getName().isEmpty()) {
            resultAreaStations.setText("输入站点未找到！");
            return;
        }
        double distanceValue;
        try {
            distanceValue = Double.parseDouble(distance);
        } catch (NumberFormatException e) {
            resultAreaStations.setText("距离格式错误！");
            return;
        }
        if (distanceValue < 0){
            resultAreaStations.setText("距离应大于0！");
            return;
        }
        test2.dijkstra(test2.getStationAndNext(), startStation, distanceValue);
        resultAreaStations.append("查询结果：附近站点信息\n");
        for (Map.Entry<Station, Double> entry : test2.getDistanceMap().entrySet()) {
            String station = entry.getKey().getName();
            resultAreaStations.append("<" + station + ", ");
            StringBuilder sb = new StringBuilder();
            for (String line : test1.getTransforStationlist().get(station)) {
                sb.append(line).append("、");
            }
            sb.setLength(sb.length() - 1);
            sb.append(", ");
            sb.append(String.format("%.3f", entry.getValue()) + ">");
            String transfor = sb.toString();
            resultAreaStations.append(transfor + "\n");
        }
    }

    private void queryTransferStations() {
        resultAreaStations.setText("");
        try {
            test1.readtxt1();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultAreaStations.append("查询结果：所有中转站信息\n");
        for (String station:test1.getTransforStationlist().keySet()){
            if (test1.getTransforStationlist().get(station).size()>1){
                StringBuilder sb = new StringBuilder();
                sb.append("<").append(station).append(", <");
                for (String line : test1.getTransforStationlist().get(station)) {
                    sb.append(line).append("、");
                }
                sb.setLength(sb.length() - 1);
                sb.append(">>");
                String transfor = sb.toString();
                resultAreaStations.append(transfor + "\n");
            }
        }
    }

    private void queryAllRoutes() {
        searchAllRoutes();
        resultAreaRoutes.append("查询结果：所有路径信息\n");
        int j = 1;
        for (List<Station> P : test3.getAllPaths()) {
            resultAreaRoutes.append("线路" + j + "：");
            j++;
            printSimplyLine(P);
        }
    }

    private void queryShortestRoute() {
        searchAllRoutes();
        resultAreaRoutes.append("查询结果：最短路径信息\n");
        List<Station> shortestPath = null;
        double shortestDistance = Double.MAX_VALUE;
        for (List<Station> path : test3.getAllPaths()) {
            double distance = test3.getAllPathAndDistances().get(path);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                shortestPath = path;
            }
        }
        if (shortestPath == null || shortestPath.isEmpty()){
            resultAreaRoutes.append("无\n");
            return;
        }
        printSimplyLine(shortestPath);
        double distance = test3.getAllPathAndDistances().get(shortestPath);
        Test67 test67 = new Test67();
        resultAreaRoutes.append("普通单程票票价：" + (test67.price(distance) - test67.price(distance)%1) + "元\n");
        resultAreaRoutes.append("武汉通票价：" + (test67.price(distance) - test67.price(distance)%1) * 0.9 + "元\n");
        resultAreaRoutes.append("日票票价：" + 0 + "元\n");
    }

    private void confirmRouteChoice() {
        String routeChoice = routeChoiceField.getText();
        int num;
        try {
            num = Integer.parseInt(routeChoice);
        } catch (NumberFormatException e) {
            resultAreaRoutes.setText("线路格式错误！");
            return;
        }
        searchAllRoutes();
        if (num > test3.getAllPaths().size()){
            resultAreaRoutes.setText("输入错误！");
            return;
        }
        resultAreaRoutes.append("查询结果：选择路径信息\n");
        List<Station> selectedPath = test3.getAllPaths().get(num - 1);
        printSimplyLine(selectedPath);
        double distance = test3.getAllPathAndDistances().get(selectedPath);
        Test67 test67 = new Test67();
        resultAreaRoutes.append("普通单程票票价：" + (test67.price(distance) - test67.price(distance)%1) + "元\n");
        resultAreaRoutes.append("武汉通票价：" + (test67.price(distance) - test67.price(distance)%1) * 0.9 + "元\n");
        resultAreaRoutes.append("日票票价：" + 0 + "元\n");
    }

    private void searchAllRoutes() {
        test3.getAllPaths().clear();
        test3.getShortestDistanceCache().clear();
        test3.getVisited().clear();
        resultAreaRoutes.setText("");

        String startstation = startStationField.getText();
        String endStation = endStationField.getText();
        try {
            test1.readtxt1();
            test3.readtxt2();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Station startStation = test3.findStationByName(test3.getStationAndNext().keySet(), startstation);
        Station destinationStation = test3.findStationByName(test3.getStationAndNext().keySet(), endStation);
        if (startStation != null && destinationStation != null) {
            int maxSearch = Math.min(test3.getStationAndNext().get(startStation).size(), test3.getStationAndNext().get(destinationStation).size());
            for (int i = 0; i < maxSearch; i++) {
                List<List<Station>> allPath = new ArrayList<>();
                test3.dfs(new ArrayList<>(), startStation, destinationStation, 0, allPath);
                test3.getShortestDistanceCache().clear();
                if (allPath.size() != 0 && test3.getAllPaths().size() < maxSearch){
                    for (List<Station> path : allPath) {
                        for (int j = 1; j < path.size()-1; j++) {
                            test3.getVisited().add(path.get(j));
                        }
                        if (!test3.getAllPaths().contains(path)) {
                            test3.getAllPaths().add(path);
                        }
                    }
                } else {
                    break;
                }
            }
        } else {
            resultAreaRoutes.setText("起始站点或终点站点未找到！\n");
        }
    }

    private void printSimplyLine(List<Station> P) {
        int i = 0;
        List<String> nowLine = new ArrayList<>();
        Set<String> sameLine = new HashSet<>(test1.getTransforStationlist().get(P.get(0).getName()));

        for (; i < P.size() - 1; i++){
            String current = P.get(i).getName();
            String next = P.get(i + 1).getName();
            Set<String> currentLines = new HashSet<>(test1.getTransforStationlist().get(current));
            Set<String> nextLines = new HashSet<>(test1.getTransforStationlist().get(next));

            currentLines.retainAll(nextLines);
            sameLine.retainAll(currentLines);
            if (sameLine.isEmpty()){
                resultAreaRoutes.append("乘" + nowLine.get(0) + ":[" + P.get(0) + "，" + current + "]，");
                sameLine.clear();
                sameLine.addAll(test1.getTransforStationlist().get(P.get(i).getName()));
                break;
            }else if (i == P.size() - 2){
                resultAreaRoutes.append("乘" + nowLine.get(0) + ":[" + P.get(0) + "，" + next + "]\n");
                resultAreaRoutes.append("长度：" + String.format("%.3f",test3.getAllPathAndDistances().get(P)) + "KM\n");
            }
            nowLine.clear();
            nowLine.addAll(sameLine);
        }
        printTransfer(P, i, nowLine, sameLine);
    }

    private void printTransfer(List<Station> selectedPath, int i, List<String> nowLine, Set<String> sameLine) {
        String start = selectedPath.get(i).getName();
        for (; i < selectedPath.size() - 1; i++){
            String current = selectedPath.get(i).getName();
            String next = selectedPath.get(i + 1).getName();
            Set<String> currentLines = new HashSet<>(test1.getTransforStationlist().get(current));
            Set<String> nextLines = new HashSet<>(test1.getTransforStationlist().get(next));

            currentLines.retainAll(nextLines);
            sameLine.retainAll(currentLines);
            if (sameLine.isEmpty() && i < selectedPath.size() - 2){
                resultAreaRoutes.append("换乘" + nowLine.get(0) + ":[" + start + "，" + current + "]，");
                sameLine.clear();
                sameLine.addAll(test1.getTransforStationlist().get(selectedPath.get(i).getName()));
                printTransfer(selectedPath, i, nowLine, sameLine);
                break;
            } else if(i == selectedPath.size() - 2){
                resultAreaRoutes.append("换乘" + nowLine.get(0) + ":[" + start + "，" + next + "]\n");
                resultAreaRoutes.append("长度：" + String.format("%.3f", test3.getAllPathAndDistances().get(selectedPath)) + "KM\n");
                break;
            }
            nowLine.clear();
            nowLine.addAll(sameLine);
        }
    }
    private void showAuthorInfo() {
        JOptionPane.showMessageDialog(this, "作者信息：\n姓名：钟笑\n学号：U202211723\n班级：HUST工程管理2201班");
    }
}
