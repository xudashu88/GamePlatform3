package com.purity.yu.gameplatform.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class DimenTool {

    public static void gen() {

        File file = new File("./app/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder w820 = new StringBuilder();


        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = "";
                    if (tempString.contains("sp")) {
                        end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    } else {
                        end = tempString.substring(tempString.lastIndexOf("<") - 3);
                    }
                    float num = Float.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

//                    sw480.append(start).append((int) Math.round(num * 0.6)).append(end).append("\n");
//                    sw600.append(start).append((int) Math.round(num * 0.75)).append(end).append("\n");
//                    sw720.append(start).append((int) Math.round(num * 0.9)).append(end).append("\n");
//                    sw800.append(tempString).append("\n");
//                    w820.append(tempString).append("\n");
                    w820.append(start).append((int) Math.round(num * 3.0)).append(end).append("\n");

                } else {
//                    sw480.append(tempString).append("\n");
//                    sw600.append(tempString).append("\n");
//                    sw720.append(tempString).append("\n");
//                    sw800.append(tempString).append("\n");
                    w820.append(tempString).append("\n");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  sw480 -->");
            System.out.println(sw480);
            System.out.println("<!--  sw600 -->");
            System.out.println(sw600);

            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);
            System.out.println("<!--  sw800 -->");
            System.out.println(sw800);

//            String sw480file = "./app/src/main/res/values-sw480dp-land/dimens.xml";
//            String sw600file = "./app/src/main/res/values-sw600dp-land/dimens.xml";
//            String sw720file = "./app/src/main/res/values-sw720dp-land/dimens.xml";
//            String sw800file = "./app/src/main/res/values-sw800dp-land/dimens.xml";
            String w820file = "./app/src/main/res/values-sw1080dp-v13/dimens.xml";
//            writeFile(sw480file, sw480.toString());
//            writeFile(sw600file, sw600.toString());
//            writeFile(sw720file, sw720.toString());
//            writeFile(sw800file, sw800.toString());
            writeFile(w820file, w820.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.close();
    }

    public static void main(String[] args) {
        gen();
    }
}