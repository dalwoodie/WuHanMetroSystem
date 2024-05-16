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
                //移除最后一个顿号
                sb.setLength(sb.length() - 1);
                sb.append(">>");
                String transfor = sb.toString();
                System.out.println(transfor);
            }
        }
    }

    public void readtxt1() throws IOException {
        FileReader subwaytxt = new FileReader("C:\\Users\\zhong\\source\\wuhansubwaysystem\\subway.txt");
        int sub;
        String line = "";
        while ((sub = subwaytxt.read()) != -1) {
            this.addline((char) sub);
            this.addbuffer((char) sub);
            if (this.getline().endsWith("线")) {
                line = this.getline();
                continue;
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
        TransforStationlist.remove(null);
        subwaytxt.close();
    }

    public void addline(char c) {
        if (linelist.size() == MAX_LENGTH_4) {
            linelist.remove(0);
        }
        linelist.add(c);
    }

    public void addbuffer(char c) {
        if (bufferlist.size() == MAX_LENGTH_30) {
            bufferlist.remove(0);
        }
        bufferlist.add(c);
    }

    public String getline() {
        StringBuilder sb = new StringBuilder();
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

    public String getbuffer() {
        StringBuilder sb = new StringBuilder();
        for (char c : bufferlist) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void addleft(String line, String buffer, String connector) {
            String station1 = this.getStationLeft(buffer, buffer.lastIndexOf(connector));
            if (!TransforStationlist.containsKey(station1)) {
            // 如果键不在Map中，则创建一个新的HashSet并添加到Map中
            TransforStationlist.put(station1, new HashSet<>());
            }
            TransforStationlist.get(station1).add(line);
    }

    public void addright(String line, String buffer, String connector) {
            String station2 = this.getStationRight(buffer, buffer.lastIndexOf(connector), connector);
            if (!TransforStationlist.containsKey(station2)) {
            // 如果键不在Map中，则创建一个新的HashSet并添加到Map中
            TransforStationlist.put(station2, new HashSet<>());
            }
            TransforStationlist.get(station2).add(line);
    }

    public String getStationLeft(String buffer, int index) {
            int leftNewlineIndex = buffer.lastIndexOf('\n', index);
        // 截取connector左边直到换行符前的所有字符
        return buffer.substring(leftNewlineIndex, index).trim();
    }

    public String getStationRight(String buffer, int index, String connector) {
            int rightSpaceIndex = buffer.indexOf('\t', index + connector.length());
            // 截取connector右边直到空格前的所有字符
        return buffer.substring(index + connector.length(), rightSpaceIndex).trim();
    }

    public boolean check(String buffer, int index, String connector) {
        int leftNewlineIndex = buffer.lastIndexOf('\n', index); //查找connector左边的换行符位置
        int rightSpaceIndex = buffer.indexOf('\t', index + connector.length()); //查找connector右边的空格位置
        // 查找connector到最后有没有“---”或“—”
        int check1 = buffer.lastIndexOf("---");
        int check2 = buffer.lastIndexOf("—");
        //正则确定最后几位是公里数
        Pattern pattern = Pattern.compile("(?s).*(\\t[0-9](\\.[0-9]+)?)$");
        Matcher matcher = pattern.matcher(buffer);

        if (leftNewlineIndex == -1 || rightSpaceIndex == -1) {return false;}
        else if (check1 > index || check2 > index) {return false;}
        else if(buffer.charAt(buffer.length()-1)!='\n') {return false;}
        else return matcher.find();
    }

    public ArrayList<Character> getLinelist() {
        return linelist;
    }

    public void setLinelist(ArrayList<Character> linelist) {
        this.linelist = linelist;
    }

    public ArrayList<Character> getBufferlist() {
        return bufferlist;
    }

    public void setBufferlist(ArrayList<Character> bufferlist) {
        this.bufferlist = bufferlist;
    }

    public Map<String, Set<String>> getTransforStationlist() {
        return TransforStationlist;
    }

    public void setTransforStationlist(Map<String, Set<String>> transforStationlist) {
        TransforStationlist = transforStationlist;
    }
}