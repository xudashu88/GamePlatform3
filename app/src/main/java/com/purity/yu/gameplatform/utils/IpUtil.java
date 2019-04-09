package com.purity.yu.gameplatform.utils;

import java.util.HashMap;
import java.util.Map;

public class IpUtil {
    private static IpUtil instance = null;

    private IpUtil() {
    }

    public static IpUtil getInstance() {
        if (null == instance) {
            instance = new IpUtil();
        }
        return instance;
    }

    /**
     * 整体进行切换 至少一个最多2个
     *
     *
     * @param code
     * @return
     */
    public String getCharByString(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("i", "0");
        map.put("n", "1");
        map.put("p", "2");
        map.put("z", "3");
        map.put("r", "4");
        map.put("a", "5");
        map.put("g", "6");
        map.put("h", "7");
        map.put("x", "8");
        map.put("e", "9");
        map.put("d", "A");
        map.put("y", "B");
        map.put("k", "C");
        map.put("m", "D");
        map.put("w", "E");
        map.put("b", "F");
        return map.get(code);
        //103.231.167.85 ghwhdhaa 測試
        //103.112.28.130 ghhinkxp 武漢
        //103.112.29.145 ghhinmen 北京
        //27.50.51.151 nyzpzzeh
        //yhwkgaea
    }

    public static String decode(String ip){
        //字典
        Map<String,String> map=new HashMap<>();
        map.put("0","i");
        map.put("1","n");
        map.put("2","p");
        map.put("3","z");
        map.put("4","r");
        map.put("5","a");
        map.put("6","g");
        map.put("7","h");
        map.put("8","x");
        map.put("9","e");
        map.put("a","d");
        map.put("b","y");
        map.put("c","k");
        map.put("d","m");
        map.put("e","w");
        map.put("f","b");


        String result="";
        for (String s:ip.split("\\.")) {
            s=Integer.toHexString(Integer.valueOf(s));
            if(s.length()==1){
                s="0"+s;
            }

            for (String i:s.split("")) {
                result+=map.get(i);
            }
        }
        System.out.println(result);
        return result;
    }

    public static void main(String[] args) {
        decode("43.249.205.5");
    }
}
