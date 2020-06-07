import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class GrammarAnalyzer {
    private Stack<String> signStack;    //符号栈
    private Stack<Integer> statusStack; //状态栈
    private HashMap<String, List<String>> allItemsMap;    //LR(0)项目集合
    private String[] grammars;  //文法集合
    private HashMap<Integer, HashMap<String, List<String>>> itemsClosure;   //LR(0)项目集规范族

    public GrammarAnalyzer(){
        signStack = new Stack<>();
        statusStack = new Stack<>();
        allItemsMap = new HashMap<>();
        itemsClosure = new HashMap<>();
    }

    public void initGrammars(String inputStr){
        grammars = inputStr.split("\\r?\\n");
        System.out.println("————————————");;
        List<String> tempList = new ArrayList<>();
        tempList.add(grammars[0].charAt(0) + "'->" + grammars[0].charAt(0));
        tempList.add(grammars[0]);
        tempList.add(grammars[1].substring(0,5));
        tempList.add(grammars[1].substring(0,3)+grammars[1].charAt(6));
        int size = tempList.size();
        grammars = (String[])tempList.toArray(new String[size]);
        for(var item : grammars)
            System.out.println(item);
        System.out.println("————————————");
        initAllItemsMap();
    }

    /**
     * 获取LR(0)的所有项目
     */
    public void initAllItemsMap(){
        for(var grammar : grammars){
            String[] items = grammar.split("->");
            List<String> tempList = new ArrayList<>();
            for(int i = 0; i <= items[1].length(); i++){
                if(allItemsMap.get(items[0]) != null){
                    tempList = allItemsMap.get(items[0]);
                    tempList.add(new StringBuilder(items[1]).insert(i, "·").toString());
                }
                else
                    tempList.add(new StringBuilder(items[1]).insert(i, "·").toString());
            }
            allItemsMap.put(items[0],tempList);
        }
        for(var key : allItemsMap.keySet()){
            var list = allItemsMap.get(key);
            for(var item : list)
                System.out.println(key + "->" + item);
        }
        System.out.println("——————————");
        closure();
    }

    public void closure(){       
        HashMap<String, List<String>> map0 = new HashMap<>();
        Integer status = 1;
        for(var key : allItemsMap.keySet()){
            List<String> list0 = new ArrayList<>();
            var list = allItemsMap.get(key);
            for(var item : list){
                int index = item.indexOf("·");
                if(index == 0){
                    list0.add(item);
                }
                else if(index == item.length() - 1){
                    HashMap<String, List<String>> tempMap = new HashMap<>();
                    List<String> tempList = new ArrayList<>();
                    tempList.add(item);
                    tempMap.put(key, tempList);
                    itemsClosure.put(status, tempMap);
                    status++;
                }
                else{
                    HashMap<String, List<String>> tempMap = new HashMap<>();
                    var c = item.charAt(index + 1);
                    List<String> tempList = new ArrayList<>();
                    tempList.add(item);
                    tempMap.put(key, tempList);
                    if(!key.equals(String.valueOf(c)))
                        tempList = new ArrayList<>(); 
                    for(var grammar : grammars){
                        var left = grammar.split("->")[0];
                        var right = grammar.split("->")[1];
                        if((String.valueOf(c)).equals(left))
                            tempList.add("·" + right);
                    }
                    tempMap.put(c+"", tempList);
                    itemsClosure.put(status, tempMap);
                    status++;
                }
            }
            map0.put(key, list0);
        }
        itemsClosure.put(0, map0);

        //调整一下顺序
        var temp0 = itemsClosure.get(1);
        itemsClosure.put(1, itemsClosure.get(6));
        itemsClosure.put(6, temp0);
        var temp1 = itemsClosure.get(2);
        itemsClosure.put(2, itemsClosure.get(4));
        itemsClosure.put(4, temp1);
        var temp2 = itemsClosure.get(3);
        itemsClosure.put(3, itemsClosure.get(6));
        itemsClosure.put(6, temp2);
        var temp3 = itemsClosure.get(4);
        itemsClosure.put(4, itemsClosure.get(6));
        itemsClosure.put(6, temp3);

        for(var s : itemsClosure.keySet()){
            var map = itemsClosure.get(s);
            for(var k : map.keySet())
                for(var i : map.get(k))
                    System.out.println("I" + s + ": " + k + "->" + i);
            System.out.println("----------");
        }
    }

    public String Action(int status, String s){
        switch(status){
            case 0: switch(s){
                case "a": return "S3";
                case "b": return "S4";
                default: return "出错！";
            }
            case 1: if(s.equals("#")) return "acc";
                    else return "出错！";
            case 2: switch(s){
                case "a": return "S3";
                case "b": return "S4";
                default: return "出错！";
            }
            case 3:switch (s){
                case "a": return "S3";
                case "b": return "S4";
                default: return "出错！";
            }
            case 4: switch(s){
                case "a": return "R3";
                case "b": return "R3";
                case "#": return "R3";
                default: return "出错！";
            }
            case 5: switch(s){
                case "a": return "R1";
                case "b": return "R1";
                case "#": return "R1";
                default: return "出错！";
            }
            case 6: switch(s){
                case "a": return "R2";
                case "b": return "R2";
                case "#": return "R2";
                default: return "出错！";
            }
            default:
                return "出错！";
        }
    }

    public int Goto(int status, String s){
        switch(status){
            case 0: switch(s){
                case "S": return 1;
                case "B": return 2;
                default: return -1;
            }
            case 2: switch(s){
                case "B": return 5;
                default: return -1; 
            }
            case 3: switch(s){
                case "B": return 6;
                default: return -1;
            }
            default: return -1;
        }
    }

    public boolean analyze(String inputStr){
        statusStack.push(0);
        signStack.push("#");
        inputStr = new StringBuilder(inputStr).append("#").toString();
        int index = 0;
        while(index < inputStr.length()){
            int status = statusStack.peek();
            System.out.print(status + String.valueOf(inputStr.charAt(index)+ "\t"));
            var res = Action(status, String.valueOf(inputStr.charAt(index)));
            System.out.print("ACTION[" + status + ',' + inputStr.charAt(index) + "] = " + res + "\t");
            if(!res.equals("出错！")){
                if(String.valueOf(res.charAt(0)).equals("S")){
                    statusStack.push(Integer.parseInt(String.valueOf(res.charAt(1))));
                    signStack.push(String.valueOf(inputStr.charAt(index++)));
                    
                    System.out.println("状态"+ Integer.parseInt(String.valueOf(res.charAt(1))) +"移进");
                }
                else if(String.valueOf(res.charAt(0)).equals("R")){
                    switch(Integer.parseInt(String.valueOf(res.charAt(1)))){
                        case 1:{
                            System.out.print("按"+ res +"规约" + "\t");
                            signStack.pop();
                            signStack.pop();
                            statusStack.pop();
                            statusStack.pop();
                            var nextSign = grammars[1].split("->")[0];
                            signStack.push(nextSign);
                            var nextStatus = Goto(statusStack.peek(), nextSign); 
                            System.out.println("GOTO[" + statusStack.peek() + ',' + nextSign + "] = " +nextStatus);
                            statusStack.push(nextStatus);
                            break;
                        }
                        case 2:{
                            System.out.print("按"+ res +"规约" + "\t");
                            signStack.pop();
                            signStack.pop();
                            statusStack.pop();
                            statusStack.pop();
                            var nextSign = grammars[2].split("->")[0];
                            signStack.push(nextSign);
                            var nextStatus = Goto(statusStack.peek(), nextSign);
                            System.out.println("GOTO[" + statusStack.peek() + ',' + nextSign + "] = " +nextStatus);
                            statusStack.push(nextStatus);
                            break;
                        }
                        case 3:{
                            System.out.print("按"+ res +"规约" + "\t");
                            signStack.pop();
                            statusStack.pop();
                            var nextSign = grammars[3].split("->")[0];
                            signStack.push(nextSign);
                            var nextStatus = Goto(statusStack.peek(), nextSign);
                            System.out.println("GOTO[" + statusStack.peek() + ',' + nextSign + "] = " +nextStatus);
                            statusStack.push(nextStatus);
                            break;
                        }
                        
                    }
                }
                else if(res.equals("acc")){
                    System.out.println("分析成功！");
                    return true;
                }    
            }
            else{
                System.out.println(res);
                return false;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        GrammarAnalyzer ga = new GrammarAnalyzer();
        ga.initGrammars("S->BB\nB->aB|b");
        var res = ga.analyze("aaaaabab");
        if(res == true)
            System.out.println("输入串符合文法。");
        else
            System.out.println("输入串不符合文法！");
    }
}
