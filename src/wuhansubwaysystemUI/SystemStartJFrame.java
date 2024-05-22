//练手用，已废弃
package wuhansubwaysystemUI;

import javax.swing.*;

public class SystemStartJFrame extends JFrame {

    public SystemStartJFrame(){
        //初始化界面
        initJFrame();
        //初始化菜单
        initJMenuBar();

        this.setVisible(true);
    }

    private void initJMenuBar() {
        //设置菜单
        JMenuBar jMenuBar = new JMenuBar();
        JMenu functionjmenu = new JMenu("功能");
        JMenu aboutjmenu = new JMenu("关于作者");
        //设置条目
        JMenuItem restartItem = new JMenuItem("返回主页");
        JMenuItem messageItem = new JMenuItem("作者信息");
        //添加条目
        functionjmenu.add(restartItem);
        aboutjmenu.add(messageItem);
        //添加菜单
        jMenuBar.add(functionjmenu);
        jMenuBar.add(aboutjmenu);

        this.setJMenuBar(jMenuBar);
    }

    private void initJFrame() {
        //设置界面
        this.setSize(600,450);
        this.setTitle("武汉地铁系统");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
