import test.Test2;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        //new SystemStartJFrame();
        //new Test1().test();
        new Test2().test();

        /*String text = "这是一些文本\n测试汉字---再测试汉字\t123.45\n还有更多文本\n测试汉字---再测试汉字\t678.999\n测试汉字---再测试汉字\t143";
        String t2 ="112346";
        String regex = "(?s).*(\\n[\\u4e00-\\u9fa5]+---[\\u4e00-\\u9fa5]+\\t[0-9]+(\\.[0-9]+)?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(t2);

        if (matcher.find()) {
            // group(1) 是因为我们有一个捕获组在正则表达式中
            String lastMatch = matcher.group(1);
            System.out.println("最右边的匹配项是: " + lastMatch);
        } else {
            System.out.println("没有找到匹配项");
        }*/
        /*String input = "Some text\t123\nOther text\t456.78\nEven more text\t999\n123456";
        System.out.println(input);
        // 正则表达式匹配"\t数字或小数\n"
        Pattern pattern = Pattern.compile("\\t(-?\\d+(\\.\\d+)?)\\n", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);

        // 从后向前搜索，直到找到最右边的匹配项
        String lastMatch = null;
        while (matcher.find()) {
            lastMatch = matcher.group(); // 更新为当前找到的匹配项
        }

        // 打印最右边的匹配项
        if (lastMatch != null) {
            System.out.println("最右边的匹配项是: " + lastMatch);
        } else {
            System.out.println("没有找到匹配项。");
        }*/

    }

}
