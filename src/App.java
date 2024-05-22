import wuhansubwaysystemUI.WuhanMetroSystem;

import javax.swing.*;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> { WuhanMetroSystem wms = new WuhanMetroSystem();});
        //6个测试函数，不可同时运行,注意加上import
        //new Test1().test1();
        //new Test2().test2();
        //new Test3().test3();
        //new Test4().test4();
        //new Test5().test5();
        //new Test67().test6();
    }
}
