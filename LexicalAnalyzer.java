import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/*** 一个简化的词法分析器
 * @author Bill Lai
 * @version 1.0
 */
public class LexicalAnalyzer {
    private HashMap<String,Integer> map;    //内部定义表
    private List<String> list;  //输出结果列表

    public LexicalAnalyzer(){
        map = new HashMap<>();
        list = new ArrayList<>();
    }

    public static void main(String[] args) {
        LexicalAnalyzer la = new LexicalAnalyzer();
        la.initDefinition(la.map);
        String filePath = "C:\\Users\\kaika\\OneDrive\\VSCode\\codeContent.txt";
        String res = la.readTxtFile(filePath);
        var resList = la.analyze(res, la.map, la.list);
        System.out.println("单词\t种别编码\t内码值");
        for(var item : resList)
            System.out.println(item);
        System.out.println("————结束————");
    }

    //读取txt代码文件
    private String readTxtFile(String filePath){
        StringBuilder sb = new StringBuilder();
        try (FileReader reader = new FileReader(filePath))
        {
            BufferedReader br = new BufferedReader(reader);
            sb.append(br.readLine());
            String s = "";
            while ((s =br.readLine()) != null){
                sb.append(s);
            }
            br.close();
        } 
        catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return sb.toString();
    }

    //初始化内部定义表
    private void initDefinition(HashMap<String, Integer> map){
        map.put("if", 0);
        map.put("then", 1);
        map.put("else", 2);
        map.put("while", 3);
        map.put("begin", 4);
        map.put("do", 5);
        map.put("end", 6);
        map.put("=", 7);
        map.put(";", 8);
        map.put("#", 10);
        map.put("+",34);
        map.put("-", 35);
        map.put("*", 36);
        map.put("/", 37);
        map.put(":=", 38);
        map.put("and", 39);
        map.put("or", 40);
        map.put("not", 41);
        map.put("<=", 42);
        map.put("< ", 42);
        map.put(">=", 42);
        map.put(">", 42);
        map.put("==", 42);
        map.put("(", 48);
        map.put(")", 49);
        map.put("标识符", 56);
        map.put("常数", 57);
    }

    //判断是否为字母
    private boolean isLetter(char c){
        if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
            return true;
        else
            return false;
    }

    //判断是否为数字
    private boolean isDigit(char c){
        if(c >= '0' && c <= '9')
            return true;
        else   
            return false;
    }

    //判断是否可在内部定义表中找到
    private boolean isDefinition(String s, HashMap<String,Integer> map){
        if(map.get(s) != null)
            return true;
        else
            return false;
    }

    //词法分析
    private List<String> analyze(String s, HashMap<String, Integer> map, List<String> list){
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            sb.delete(0, sb.length()); //清空StringBuilder
            if(c == ' ' || c == '\t' || c == '\n' || c == '\r'){}
            else if(isLetter(c)){
                //形如aa,a1……
                while(isLetter(c) || isDigit(c)){
                    sb.append(c);
                    c = chars[++i];
                }
                i--;    //回退一个字符，以下的i--同
                if(isDefinition(sb.toString(), map))
                    list.add(sb.toString() + "\t" + map.get(sb.toString()) + "\t\tnull");
                else
                    list.add(sb.toString() + "\t" + map.get("标识符") + "\t\tnull");
            }
            else if(isDigit(c) || (c == '.')){
                //形如1234，123.4
                while(isDigit(c) || (c == '.' && isDigit(chars[++i]))){
                    if(c == '.')
                        i--;
                    sb.append(c);
                    c = chars[++i];
                }
                i--;
                list.add(sb.toString() + "\t" + map.get("常数") + "\t\t" + sb.toString());  //常数的内码值用本身的值代替
            }
            else switch(c){
                case '+': 
                case '-':
                case '*':
                case '/':
                case '(':
                case ')':
                case ';':   
                    list.add(String.valueOf(c) +'\t' + map.get(String.valueOf(c)) + "\t\tnull");   break;
                    //以上字符皆可在内部定义表中直接查找
                case '#':{
                    if(chars[++i] == '~')
                        return list;
                    else{
                        i--;
                        list.add("Error:未能识别符号 " + "#");
                    }
                    break;
                }   
                case '=':{
                    c = chars[++i];
                    if(c == '=')
                        list.add("==" + '\t' + map.get("==") + "\t\tEQ");
                    else{
                        i--;
                        list.add("=" + '\t' + map.get("=") + "\t\tnull");
                    }
                    break;
                }
                case ':':{
                    c = chars[++i];
                    if(c == '=')
                        list.add(":=" + '\t' + map.get(":=") + "\t\tnull");
                    else{
                        i--;
                        list.add("Error:未能识别符号 " + ":");
                    }
                    break;
                }
                case '>':{
                    c = chars[++i];
                    if(c == '=')
                        list.add(">=" + '\t' + map.get(">=") + "\t\tRE");
                    else{
                        i--;
                        list.add(">" + '\t' + map.get(">") + "\t\tRT");
                    }
                    break;
                }
                case '<':{
                    c = chars[++i];
                    if(c == '=')
                        list.add("<=" + '\t' + map.get("<=") + "\t\tLE");
                    else{
                        i--;
                        list.add("<" + "\t" + map.get("<") + "\t\tLT");
                    }
                    break;
                }
                default:    
                    list.add("未能识别符号 " + c);
            }
        }
        return list;
    }
}
