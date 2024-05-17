package test;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
    private static final int MAX_LENGTH_4 = 4;
    private static final int MAX_LENGTH_30 = 30;
    private ArrayList<Character> linelist = new ArrayList<>(MAX_LENGTH_4);
    private ArrayList<Character> bufferlist = new ArrayList<>(MAX_LENGTH_30);
    private Map<String, Set<String>> TransforStationlist = new HashMap<>();

    public void test1() throws IOException {
        this.readtxt1();
        for (String station:TransforStationlist.keySet()){
            if (TransforStationlist.get(station).size()>1){
                StringBuilder sb = new StringBuilder();
                sb.append("<").append(station).append(", <");
                for (String line : TransforStationlist.get(station)) {
                    sb.append(line).append("、");
                }
                sb.setLength(sb.length() - 1); // 移除最后一个顿号
                sb.append(">>");
                String transfor = sb.toString();
                System.out.println(transfor);
            }
        }
    }
    // 读取文件往TransforStationlist中添加数据
    public void readtxt1() throws IOException {
        FileReader subwaytxt = new FileReader("C:\\Users\\zhong\\source\\wuhansubwaysystem\\subway.txt");
        int sub;
        String line = "";
        while ((sub = subwaytxt.read()) != -1) {
            this.addline((char) sub);
            this.addbuffer((char) sub);
            if (this.getline().endsWith("线")) {
                line = this.getline();
                continue; // linelist最后一位为“线”时开始新循环
            }
            String buffer = this.getbuffer();
            if (this.check(buffer, buffer.lastIndexOf("---"), "---")) {
                addleft(line, buffer, "---");
                addright(line, buffer, "---");
            }
            if (this.check(buffer, buffer.lastIndexOf("—"), "—")) {
                addleft(line, buffer, "—");
                addright(line, buffer, "—");
            }
        }
        TransforStationlist.remove(null); // 移除TransforStationlist中可能的空键
        subwaytxt.close();
    }
    // 往linelist中添加字符
    public void addline(char c) {
        if (linelist.size() == MAX_LENGTH_4) {
            linelist.remove(0);
        }
        linelist.add(c);
    }
    // 往bufferlist中添加字符
    public void addbuffer(char c) {
        if (bufferlist.size() == MAX_LENGTH_30) {
            bufferlist.remove(0);
        }
        bufferlist.add(c);
    }
    // 返回字符串类型的linelist中储存的数据
    public String getline() {
        StringBuilder sb = new StringBuilder();
        //判断第一位是否为换行符，是的话返回2~4位的字符，否的话返回所有位的字符
        if (linelist.get(0) == '\n') {
            for (int i = 1; i < 4; i++) {
                sb.append(linelist.get(i));
            }
        } else {
            for (char c : linelist) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    // 返回字符串类型的bufferlist中储存的数据
    public String getbuffer() {
        StringBuilder sb = new StringBuilder();
        for (char c : bufferlist) {
            sb.append(c);
        }
        return sb.toString();
    }
    // 将“---”或“—”左边的车站添加到TransforStationlist表中
    public void addleft(String line, String buffer, String connector) {
            String station1 = this.getStationLeft(buffer, buffer.lastIndexOf(connector));
            if (!TransforStationlist.containsKey(station1)) {
                TransforStationlist.put(station1, new HashSet<>()); // 如果键不在Map中，则创建一个新的HashSet并添加到Map中
            }
            TransforStationlist.get(station1).add(line);
    }
    // 将“---”或“—”右边的车站添加到TransforStationlist表中
    public void addright(String line, String buffer, String connector) {
            String station2 = this.getStationRight(buffer, buffer.lastIndexOf(connector), connector);
            if (!TransforStationlist.containsKey(station2)) {
                TransforStationlist.put(station2, new HashSet<>()); // 如果键不在Map中，则创建一个新的HashSet并添加到Map中
            }
            TransforStationlist.get(station2).add(line);
    }

    public String getStationLeft(String buffer, int index) {
        int leftNewlineIndex = buffer.lastIndexOf('\n', index);
        return buffer.substring(leftNewlineIndex, index).trim(); // 截取connector左边直到换行符前的所有字符
    }

    public String getStationRight(String buffer, int index, String connector) {
        int rightSpaceIndex = buffer.indexOf('\t', index + connector.length());
        return buffer.substring(index + connector.length(), rightSpaceIndex).trim(); // 截取connector右边直到制表符的所有字符
    }

    public boolean check(String buffer, int index, String connector) {
        int leftNewlineIndex = buffer.lastIndexOf('\n', index); //查找connector左边的换行符位置
        int rightSpaceIndex = buffer.indexOf('\t', index + connector.length()); //查找connector右边的空格位置
        int check1 = buffer.lastIndexOf("---"); // 最右边的"---"的位置
        int check2 = buffer.lastIndexOf("—"); // 最右边的"—"的位置
        // 正则表达式确定最后几位是公里数
        Pattern pattern = Pattern.compile("(?s).*(\\t[0-9](\\.[0-9]+)?)$");
        Matcher matcher = pattern.matcher(buffer);

        if (leftNewlineIndex == -1 || rightSpaceIndex == -1) { return false; } // 如果没找到左边的换行符或右边的制表符
        else if (check1 > index || check2 > index) { return false; } // 如果“---”的右边有“—”或“—”的右边有“---”
        else if(buffer.charAt(buffer.length()-1)!='\n') { return false; } // 如果最右边不是换行符
        else return matcher.find(); //最右边（不包括换行符）能找到数字或小数
    }

    public ArrayList<Character> getLinelist() {
        return linelist;
    }

    public ArrayList<Character> getBufferlist() {
        return bufferlist;
    }

    public Map<String, Set<String>> getTransforStationlist() {
        return TransforStationlist;
    }
}