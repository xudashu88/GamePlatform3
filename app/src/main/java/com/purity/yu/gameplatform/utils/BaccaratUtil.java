package com.purity.yu.gameplatform.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/8/2.
 */
public class BaccaratUtil {
    private static Context mContext;
    private static BaccaratUtil instance = null;

    public void init(Context context) {
        mContext = context;
    }

    public static BaccaratUtil getInstance() {
        if (null == instance) {
            instance = new BaccaratUtil();
        }
        return instance;
    }

    public static int getBacCardScore(List<String> cards) {
        Map<String, Integer> map = new HashMap<>();
        map.put("11", 1);
        map.put("12", 2);
        map.put("13", 3);
        map.put("14", 4);
        map.put("15", 5);
        map.put("16", 6);
        map.put("17", 7);
        map.put("18", 8);
        map.put("19", 9);
        map.put("1a", 0);
        map.put("1b", 0);
        map.put("1c", 0);
        map.put("1d", 0);
        map.put("21", 1);
        map.put("22", 2);
        map.put("23", 3);
        map.put("24", 4);
        map.put("25", 5);
        map.put("26", 6);
        map.put("27", 7);
        map.put("28", 8);
        map.put("29", 9);
        map.put("2a", 0);
        map.put("2b", 0);
        map.put("2c", 0);
        map.put("2d", 0);
        map.put("31", 1);
        map.put("32", 2);
        map.put("33", 3);
        map.put("34", 4);
        map.put("35", 5);
        map.put("36", 6);
        map.put("37", 7);
        map.put("38", 8);
        map.put("39", 9);
        map.put("3a", 0);
        map.put("3b", 0);
        map.put("3c", 0);
        map.put("3d", 0);
        map.put("41", 1);
        map.put("42", 2);
        map.put("43", 3);
        map.put("44", 4);
        map.put("45", 5);
        map.put("46", 6);
        map.put("47", 7);
        map.put("48", 8);
        map.put("49", 9);
        map.put("4a", 0);
        map.put("4b", 0);
        map.put("4c", 0);
        map.put("4d", 0);

        int score = 0;
        for (String card : cards) {
            score += map.get(card);
        }
        score = score % 10;//取个位数
        return score;
    }

    public static int getDtCardScore(String cards) {
        Map<String, Integer> map = new HashMap<>();
        map.put("11", 1);
        map.put("12", 2);
        map.put("13", 3);
        map.put("14", 4);
        map.put("15", 5);
        map.put("16", 6);
        map.put("17", 7);
        map.put("18", 8);
        map.put("19", 9);
        map.put("1a", 10);
        map.put("1b", 11);
        map.put("1c", 12);
        map.put("1d", 13);
        map.put("21", 1);
        map.put("22", 2);
        map.put("23", 3);
        map.put("24", 4);
        map.put("25", 5);
        map.put("26", 6);
        map.put("27", 7);
        map.put("28", 8);
        map.put("29", 9);
        map.put("2a", 10);
        map.put("2b", 11);
        map.put("2c", 12);
        map.put("2d", 13);
        map.put("31", 1);
        map.put("32", 2);
        map.put("33", 3);
        map.put("34", 4);
        map.put("35", 5);
        map.put("36", 6);
        map.put("37", 7);
        map.put("38", 8);
        map.put("39", 9);
        map.put("3a", 10);
        map.put("3b", 11);
        map.put("3c", 12);
        map.put("3d", 13);
        map.put("41", 1);
        map.put("42", 2);
        map.put("43", 3);
        map.put("44", 4);
        map.put("45", 5);
        map.put("46", 6);
        map.put("47", 7);
        map.put("48", 8);
        map.put("49", 9);
        map.put("4a", 10);
        map.put("4b", 11);
        map.put("4c", 12);
        map.put("4d", 13);

        return map.get(cards);
    }

    public static String getSingleCardScore(String cards) {
        //1-黑桃 2-红桃 3-梅花 4-方块 5-王
        Map<String, String> map = new HashMap<>();
        map.put("00", "无数据");
        map.put("11", "黑桃" + "A");
        map.put("12", "黑桃" + 2);
        map.put("13", "黑桃" + 3);
        map.put("14", "黑桃" + 4);
        map.put("15", "黑桃" + 5);
        map.put("16", "黑桃" + 6);
        map.put("17", "黑桃" + 7);
        map.put("18", "黑桃" + 8);
        map.put("19", "黑桃" + 9);
        map.put("1a", "黑桃" + 10);
        map.put("1b", "黑桃" + "J");
        map.put("1c", "黑桃" + "Q");
        map.put("1d", "黑桃" + "K");
        map.put("21", "红桃" + "A");
        map.put("22", "红桃" + 2);
        map.put("23", "红桃" + 3);
        map.put("24", "红桃" + 4);
        map.put("25", "红桃" + 5);
        map.put("26", "红桃" + 6);
        map.put("27", "红桃" + 7);
        map.put("28", "红桃" + 8);
        map.put("29", "红桃" + 9);
        map.put("2a", "红桃" + 10);
        map.put("2b", "红桃" + "J");
        map.put("2c", "红桃" + "Q");
        map.put("2d", "红桃" + "K");
        map.put("31", "梅花" + "A");
        map.put("32", "梅花" + 2);
        map.put("33", "梅花" + 3);
        map.put("34", "梅花" + 4);
        map.put("35", "梅花" + 5);
        map.put("36", "梅花" + 6);
        map.put("37", "梅花" + 7);
        map.put("38", "梅花" + 8);
        map.put("39", "梅花" + 9);
        map.put("3a", "梅花" + 10);
        map.put("3b", "梅花" + "J");
        map.put("3c", "梅花" + "Q");
        map.put("3d", "梅花" + "K");
        map.put("41", "方块" + "A");
        map.put("42", "方块" + 2);
        map.put("43", "方块" + 3);
        map.put("44", "方块" + 4);
        map.put("45", "方块" + 5);
        map.put("46", "方块" + 6);
        map.put("47", "方块" + 7);
        map.put("48", "方块" + 8);
        map.put("49", "方块" + 9);
        map.put("4a", "方块" + 10);
        map.put("4b", "方块" + "J");
        map.put("4c", "方块" + "Q");
        map.put("4d", "方块" + "K");
        map.put("51", "小王 ");
        map.put("52", "大王 ");
        if (cards.equals("")) {
        }
        return map.get(cards);
    }

    public static int switchCard(String card) {
        int _cardCode = 0;
        if (card.equals("11")) {//1黑桃 2红桃 3梅花 4方片 10-J-Q-K 转A-B-C-D 红桃Q用2B标识
            _cardCode = R.drawable.card_11;
        } else if (card.equals("21")) {
            _cardCode = R.drawable.card_21;
        } else if (card.equals("31")) {
            _cardCode = R.drawable.card_31;
        } else if (card.equals("41")) {
            _cardCode = R.drawable.card_41;
        } else if (card.equals("12")) {
            _cardCode = R.drawable.card_12;
        } else if (card.equals("22")) {
            _cardCode = R.drawable.card_22;
        } else if (card.equals("32")) {
            _cardCode = R.drawable.card_32;
        } else if (card.equals("42")) {
            _cardCode = R.drawable.card_42;
        } else if (card.equals("13")) {
            _cardCode = R.drawable.card_13;
        } else if (card.equals("23")) {
            _cardCode = R.drawable.card_23;
        } else if (card.equals("33")) {
            _cardCode = R.drawable.card_33;
        } else if (card.equals("43")) {
            _cardCode = R.drawable.card_43;
        } else if (card.equals("14")) {
            _cardCode = R.drawable.card_14;
        } else if (card.equals("24")) {
            _cardCode = R.drawable.card_24;
        } else if (card.equals("34")) {
            _cardCode = R.drawable.card_34;
        } else if (card.equals("44")) {
            _cardCode = R.drawable.card_44;
        } else if (card.equals("15")) {
            _cardCode = R.drawable.card_15;
        } else if (card.equals("25")) {
            _cardCode = R.drawable.card_25;
        } else if (card.equals("35")) {
            _cardCode = R.drawable.card_35;
        } else if (card.equals("45")) {
            _cardCode = R.drawable.card_45;
        } else if (card.equals("16")) {
            _cardCode = R.drawable.card_16;
        } else if (card.equals("26")) {
            _cardCode = R.drawable.card_26;
        } else if (card.equals("36")) {
            _cardCode = R.drawable.card_36;
        } else if (card.equals("46")) {
            _cardCode = R.drawable.card_46;
        } else if (card.equals("17")) {
            _cardCode = R.drawable.card_17;
        } else if (card.equals("27")) {
            _cardCode = R.drawable.card_27;
        } else if (card.equals("37")) {
            _cardCode = R.drawable.card_37;
        } else if (card.equals("47")) {
            _cardCode = R.drawable.card_47;
        } else if (card.equals("18")) {
            _cardCode = R.drawable.card_18;
        } else if (card.equals("28")) {
            _cardCode = R.drawable.card_28;
        } else if (card.equals("38")) {
            _cardCode = R.drawable.card_38;
        } else if (card.equals("48")) {
            _cardCode = R.drawable.card_48;
        } else if (card.equals("19")) {
            _cardCode = R.drawable.card_19;
        } else if (card.equals("29")) {
            _cardCode = R.drawable.card_29;
        } else if (card.equals("39")) {
            _cardCode = R.drawable.card_39;
        } else if (card.equals("49")) {
            _cardCode = R.drawable.card_49;
        } else if (card.equals("1a")) {
            _cardCode = R.drawable.card_1a;
        } else if (card.equals("2a")) {
            _cardCode = R.drawable.card_2a;
        } else if (card.equals("3a")) {
            _cardCode = R.drawable.card_3a;
        } else if (card.equals("4a")) {
            _cardCode = R.drawable.card_4a;
        } else if (card.equals("1b")) {
            _cardCode = R.drawable.card_1b;
        } else if (card.equals("2b")) {
            _cardCode = R.drawable.card_2b;
        } else if (card.equals("3b")) {
            _cardCode = R.drawable.card_3b;
        } else if (card.equals("4b")) {
            _cardCode = R.drawable.card_4b;
        } else if (card.equals("1c")) {
            _cardCode = R.drawable.card_1c;
        } else if (card.equals("2c")) {
            _cardCode = R.drawable.card_2c;
        } else if (card.equals("3c")) {
            _cardCode = R.drawable.card_3c;
        } else if (card.equals("4c")) {
            _cardCode = R.drawable.card_4c;
        } else if (card.equals("1d")) {
            _cardCode = R.drawable.card_1d;
        } else if (card.equals("2d")) {
            _cardCode = R.drawable.card_2d;
        } else if (card.equals("3d")) {
            _cardCode = R.drawable.card_3d;
        } else if (card.equals("4d")) {
            _cardCode = R.drawable.card_4d;
        } else if (card.equals("51")) {
            _cardCode = R.drawable.small_king;
        } else if (card.equals("52")) {
            _cardCode = R.drawable.big_king;
        }
        return _cardCode;
    }

    /**
     * 语言播报点数 百家乐
     *
     * @param type  0庄 1闲
     * @param score
     */
    public static void sayBacDot(int type, int score) {
        if (type == 1) {//闲点数
            if (score == 0) {
                SoundPoolUtil.getInstance().play("36");
                SoundPoolUtil.getInstance().play("36", 0);//0次  播一次
            } else if (score == 1) {
                SoundPoolUtil.getInstance().play("27");
                SoundPoolUtil.getInstance().play("27", 0);
            } else if (score == 2) {
                SoundPoolUtil.getInstance().play("28");
                SoundPoolUtil.getInstance().play("28", 0);
            } else if (score == 3) {
                SoundPoolUtil.getInstance().play("29");
                SoundPoolUtil.getInstance().play("29", 0);
            } else if (score == 4) {
                SoundPoolUtil.getInstance().play("30");
                SoundPoolUtil.getInstance().play("30", 0);
            } else if (score == 5) {
                SoundPoolUtil.getInstance().play("31");
                SoundPoolUtil.getInstance().play("31", 0);
            } else if (score == 6) {
                SoundPoolUtil.getInstance().play("32");
                SoundPoolUtil.getInstance().play("32", 0);
            } else if (score == 7) {
                SoundPoolUtil.getInstance().play("33");
                SoundPoolUtil.getInstance().play("33", 0);
            } else if (score == 8) {
                SoundPoolUtil.getInstance().play("34");
                SoundPoolUtil.getInstance().play("34", 0);
            } else if (score == 9) {
                SoundPoolUtil.getInstance().play("35");
                SoundPoolUtil.getInstance().play("35", 0);
            }
        } else if (type == 0) {//庄点数
            if (score == 0) {
                SoundPoolUtil.getInstance().play("26");
                SoundPoolUtil.getInstance().play("26", 0);
            } else if (score == 1) {
                SoundPoolUtil.getInstance().play("17");
                SoundPoolUtil.getInstance().play("17", 0);
            } else if (score == 2) {
                SoundPoolUtil.getInstance().play("18");
                SoundPoolUtil.getInstance().play("18", 0);
            } else if (score == 3) {
                SoundPoolUtil.getInstance().play("19");
                SoundPoolUtil.getInstance().play("19", 0);
            } else if (score == 4) {
                SoundPoolUtil.getInstance().play("20");
                SoundPoolUtil.getInstance().play("20", 0);
            } else if (score == 5) {
                SoundPoolUtil.getInstance().play("21");
                SoundPoolUtil.getInstance().play("21", 0);
            } else if (score == 6) {
                SoundPoolUtil.getInstance().play("22");
                SoundPoolUtil.getInstance().play("22", 0);
            } else if (score == 7) {
                SoundPoolUtil.getInstance().play("23");
                SoundPoolUtil.getInstance().play("23", 0);
            } else if (score == 8) {
                SoundPoolUtil.getInstance().play("24");
                SoundPoolUtil.getInstance().play("24", 0);
            } else if (score == 9) {
                SoundPoolUtil.getInstance().play("25");
                SoundPoolUtil.getInstance().play("25", 0);
            }
        }
    }

    /**
     * 语言播报点数 龙虎
     *
     * @param type  0龙 1虎
     * @param score 53龙赢 54虎赢 55和局 56-64龙一点到九点 65龙10 66龙J 67龙Q 68龙K 69-81同龙
     */
    public static void sayDtDot(int type, int score) {
        if (type == 0) {//龙点数
            if (score == 1) {
                SoundPoolUtil.getInstance().play("56", 0);
            } else if (score == 2) {
                SoundPoolUtil.getInstance().play("57", 0);
            } else if (score == 3) {
                SoundPoolUtil.getInstance().play("58", 0);
            } else if (score == 4) {
                SoundPoolUtil.getInstance().play("59", 0);
            } else if (score == 5) {
                SoundPoolUtil.getInstance().play("60", 0);
            } else if (score == 6) {
                SoundPoolUtil.getInstance().play("61", 0);
            } else if (score == 7) {
                SoundPoolUtil.getInstance().play("62", 0);
            } else if (score == 8) {
                SoundPoolUtil.getInstance().play("63", 0);
            } else if (score == 9) {
                SoundPoolUtil.getInstance().play("64", 0);
            } else if (score == 10) {
                SoundPoolUtil.getInstance().play("65", 0);
            } else if (score == 11) {
                SoundPoolUtil.getInstance().play("66", 0);
            } else if (score == 12) {
                SoundPoolUtil.getInstance().play("67", 0);
            } else if (score == 13) {
                SoundPoolUtil.getInstance().play("68", 0);
            }
        } else if (type == 1) {//虎点数
            if (score == 1) {
                SoundPoolUtil.getInstance().play("69", 0);
            } else if (score == 2) {
                SoundPoolUtil.getInstance().play("70", 0);
            } else if (score == 3) {
                SoundPoolUtil.getInstance().play("71", 0);
            } else if (score == 4) {
                SoundPoolUtil.getInstance().play("72", 0);
            } else if (score == 5) {
                SoundPoolUtil.getInstance().play("73", 0);
            } else if (score == 6) {
                SoundPoolUtil.getInstance().play("74", 0);
            } else if (score == 7) {
                SoundPoolUtil.getInstance().play("75", 0);
            } else if (score == 8) {
                SoundPoolUtil.getInstance().play("76", 0);
            } else if (score == 9) {
                SoundPoolUtil.getInstance().play("77", 0);
            } else if (score == 10) {
                SoundPoolUtil.getInstance().play("78", 0);
            } else if (score == 11) {
                SoundPoolUtil.getInstance().play("79", 0);
            } else if (score == 12) {
                SoundPoolUtil.getInstance().play("80", 0);
            } else if (score == 13) {
                SoundPoolUtil.getInstance().play("81", 0);
            }
        }
    }

    public static void sayBacDot(int type, int score, final boolean isPlayPair, final boolean isBankPair) {
        if (type == 1) {//闲赢
//            new android.os.Handler().postDelayed(new Runnable() {
//                public void run() {
            if (isPlayPair && isBankPair) {
                SoundPoolUtil.getInstance().play("13", 1);//闲赢庄对闲对
            } else if (isPlayPair) {
                SoundPoolUtil.getInstance().play("12", 1);//闲赢闲对
            } else if (isBankPair) {
                SoundPoolUtil.getInstance().play("11", 1);//闲赢庄对
            } else {
                SoundPoolUtil.getInstance().play("6", 1);//闲赢
            }
//                }
//            }, 2200);
        } else if (type == 0) {//庄赢
//            new android.os.Handler().postDelayed(new Runnable() {
//                public void run() {
            if (isPlayPair && isBankPair) {
                SoundPoolUtil.getInstance().play("10", 1);//庄赢庄对闲对
            } else if (isPlayPair) {
                SoundPoolUtil.getInstance().play("9", 1);//庄赢闲对
            } else if (isBankPair) {
                SoundPoolUtil.getInstance().play("8", 1);//庄赢庄对
            } else {
                SoundPoolUtil.getInstance().play("5", 1);//庄赢
            }
//                }
//            }, 2200);
        } else {
            if (isBankPair && isPlayPair) {
                SoundPoolUtil.getInstance().play("16", 0);//和局庄对闲对
            } else if (isBankPair) {
                SoundPoolUtil.getInstance().play("14", 0);//和局庄对
            } else if (isPlayPair) {
                SoundPoolUtil.getInstance().play("15", 0);//和局闲对
            } else {
                SoundPoolUtil.getInstance().play("7", 0);//和局
            }
        }
    }

    public static void sayDtDot(int type, boolean isTie) {
        if (type == 0) {//龙赢
            SoundPoolUtil.getInstance().play("53", 1);
        } else if (type == 1) {//虎赢
            SoundPoolUtil.getInstance().play("54", 1);
        } else if (isTie) {//和局
            SoundPoolUtil.getInstance().play("55", 1);
        }
    }

    public static List<Integer> str2ListReInt(String str) {
        List<Integer> list = new ArrayList<>();
        String _str = str.replace("[", "").replace("]", "");
        if (!TextUtils.isEmpty(_str)) {
            String[] _intArr = _str.split(",");
            for (int i = 0; i < _intArr.length; i++) {
                String string = _intArr[i].substring(1, _intArr[i].length() - 1).trim();
                list.add(Integer.parseInt(string));
            }
        }
        return list;
    }

    public static List<String> str2List2(String str) {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        if (!str.equals("[]")) {
            String _str = str.replace("[", "").replace("]", "").substring(1, str.length() - 2);
            if (!TextUtils.isEmpty(_str)) {
                String[] _intArr = _str.split(",");
                for (String string : _intArr) {
                    list.add(string.replaceAll("[^a-z^A-Z^1-9]", ""));
                }
            }
        }
        return list;
    }

    public List<Integer> initMaxScore(int size, List<Integer> list) {
        List<Integer> _maxScoreList = new ArrayList<>();
        MathRandomUtil mathRandom = new MathRandomUtil();
        Random random = new Random();
        for (int i = 0; i < size; i++) {//21
            int maxScore = mathRandom.scoreRandom(random);
            _maxScoreList.add(maxScore);
        }
        list.clear();
        return _maxScoreList;
    }

    public List<Integer> initBoardMessage(int size, List<Integer> list) {
        List<Integer> integerList = new ArrayList<>();
        MathRandomUtil mathRandom = new MathRandomUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {//21
            //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
            int temp = mathRandom.PercentageRandom();
            integerList.add(temp);
            sb.append(temp + ",");
        }
//        String _temp = "1,8,1,11,5,0,8,4,2,5,1,8,0,0,8,4,1,1,7,0,0,4,0,0,0,1,5,1,0,0,0,1,1,0,1,1,1,0,4,2,0,0,0,10,8,0,1,1,0,1,9,0,3,1,1,0,1,0,1,0,0,4,0,1,0,1,0,1,0,0,0,1,8,4,0,1,1,2,0,1,";
        LogUtil.i("initBoardMessage=" + sb.toString());
//        String _temp = "0,1,0,1,1,0," +
//                "2,0,1,0,0,0," +
//                "1,1,0,1,1,1," +
//                "1,0,1,0,1,1," +
//                "0,1,0,1,1,0," +
//                "0,0,0,0,1,0," +
//                "1,0,1,0,0,2," +
//                "0,0,1,0,0,1," +
//                "1,1,0,0,2,0," +
//                "0,1,1,0,0,2," +
//                "2,";//  全路完美2 ok
//                String _temp = "1,0,1,0,0,1," +
//                "0,0,0,0,0,0," +
//                "0,1,1,0,1,1," +
//                "2,0,1,2,0,0," +
//                "1,1,0,0,1,0," +
//                "0,0,0,1,1,0," +
//                "0,1,1,1,0,0," +
//                "0,0,0,0,1,0," +
//                "1,0,0,0,2,2," +
//                "0,";//  全路完美1 ok
//        String _temp = "1,0,1,1,1,1," +
//                "0,2,0,0,1,0," +
//                "0,0,0,2,1,1," +
//                "1,0,0,0,2,1," +
//                "1,2,0,0,0,2," +
//                "1,0,0,1,1,1," +
//                "0,1,0,2,0,0," +
//                "1,1,0,1,1,";//  全路完美 ok
//        String _temp = "1,6,2,1,7,0," +
//                "0,0,2,1,0,0," +
//                "1,0,0,1,2,0,";// ok
        //        String _temp = "0,1,1,1,1,2," +
//                "1,1,1,2,0,0," +
//                "2,0,2,1,1,1," +
//                "1,1,1,1,1,0," +
//                "1,1,2,1,1,0," +
//                "1,1,0,1,1,1," +
//                "0,0,1,1,1,1," +
//                "0,0,0,1,0,0," +
//                "1,1,0,0,1,0," +
//                "0,2,0,2,0,0," +
//                "1,1,1,1,0,0," +
//                "2,";//全路ok1 67
//        String _temp = "0,1,1,0,0,0," +
//                "0,0,0,0,2,0," +
//                "0,0,1,1,0,0," +
//                "1,1,1,1,1,1," +
//                "1,1,0,2,0,0," +
//                "0,0,0,2,1,1," +
//                "1,2,2,1,0,1," +
//                "1,0,0,1,2,0,";//4个拐弯 全路ok2 48
        String _temp = "0,0,0,0,0,0," +
                "2,2,2,1,1,1," +
                "1,1,1,1,0,0," +
                "0,1,0,0,0,0," +
                "0,0,2,2,0,0," +
                "0,2,1,0,0,1," +
                "0,0,1,1,1,0," +
                "1,2,2,2,2,1," +
                "0,1,0,0,1,";//4个拐弯 全路ok2 53  2018-8-25 17:47
//        String _temp = "0,1,1,0,0,0," +
//                       "0,0,0,0,2,0," +
//                "0,0,1,1,0,0," +
//                "1,1,1,1,1,1," +
//                "1,1,0,2,0,0," +
//                "0,0,0,2,1,1," +
//                "1,2,2,1,0,1," +
//                "1,0,0,1,2,0," +
//                "0,1,1,1,1,0," +
//                "0,0,0,1,0,1," +
//                "1,1,0,0,0,0," +
//                "0,";//大眼路 ok
//        String _temp = "9,0,0,0,1,1," +
//                       "1,6,1,3,1,1," +
//                "4,1,1,0,1,0," +
//                "0,3,0,1,1,0," +
//                "9,0,0,1,1,1," +
//                "0,1,0,1,5,1," +
//                "1,1,1,1,1,0," +
//                "0,1,1,6,0,0," +
//                "0,1,1,1,1,0," +
//                "0,0,0,1,0,1," +
//                "1,1,0,0,0,0," +
//                "0,";//大眼路 ok

//        String _temp = "2, 2, 0, 1, 1, 0," +
//                " 0, 1, 0, 1, 1, 6," +
//                " 2, 1, 9, 4, 0, 0," +
//                " 0, 0, 1, 1, 2, 1," +
//                " 1, 1, 0, 1, 0, 2," +
//                " 0, 1, 4, 0, 0, 2," +
//                " 6, 0, 1, 0, 1, 0," +
//                " 0, 1, 0, 1, 0, 1," +
//                " 6, 1, 1, 1, 1, 0, 1";// 大眼路 ok
//                String _temp = "1,0,1,0,1,0," +
//                "0,0,1,0,0,1," +
//                "2,1,1,0,1,0," +
//                "0,0,0,1,1,0," +
//                "1,1,2,1,1,0," +
//                "1,1,0,1,1,1," +
//                "0,0,1,1,1,1," +
//                "0,0,0,1,0,0," +
//                "1,1,0,0,1,0," +
//                "0,2,0,2,0,0," +
//                "1,1,1,1,0,0," +
//                "2,";//大眼路 ok
//        String[] arr = _temp.split(",");
//        for (int i = 0; i < arr.length; i++) {
//            integerList.add(Integer.parseInt(arr[i]));
//        }
        list.clear();
        return integerList;
    }

    /**
     * 有点小问题
     *
     * @param context
     */
    public void openSound(Context context) {
        try {
            int languageType = CommSharedUtil.getInstance(context).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
            SoundPoolUtil.getInstance().release();
            if (languageType == LanguageType.LANGUAGE_EN) {
                SoundPoolUtil.getInstance().initBacEN(context);
            } else {
                SoundPoolUtil.getInstance().initBacZH(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSound() {
        SoundPoolUtil.getInstance().release();
    }

    /*
     * float计算
     * @param value
     * @type 0加法 1减法 2除法
     * */
    public static String floatMath(String value1, String value2, int type) {
        String result = "";
//        result.setScale(1);//保留1位小数  默认四舍五入
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        if (type == 0) {
            result = b1.add(b2).toString();//floatValue() 会损失精度
        } else if (type == 1) {
            result = b1.subtract(b2).toString();
        } else if (type == 2) {
            result = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return result;
    }

    public static List<Integer> addChip(int score) {
        List<Integer> chip = new ArrayList<>();
        //50000 10000 5000 1000 500 200 100 50 20 10
        while (score > 9) {
            if (score > 49999) {
                chip.add(50000);
                score -= 50000;
            } else if (score > 9999) {
                chip.add(10000);
                score -= 10000;
            } else if (score > 4999) {
                chip.add(5000);
                score -= 5000;
            } else if (score > 999) {
                chip.add(1000);
                score -= 1000;
            } else if (score > 499) {
                chip.add(500);
                score -= 500;
            } else if (score > 199) {
                chip.add(200);
                score -= 200;
            } else if (score > 99) {
                chip.add(100);
                score -= 100;
            } else if (score > 49) {
                chip.add(50);
                score -= 50;
            } else if (score > 19) {
                chip.add(20);
                score -= 20;
            } else if (score > 9) {
                chip.add(10);
                score -= 10;
            }
        }
        return chip;
    }

    public boolean isNumber(String string) {
        return string.matches("[0-9]+");
    }

    public boolean isLogin() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN))) {
            ToastUtil.showToast(mContext, "请登录");
            return false;
        }
        return true;
    }

    public String changeIp(EditText et_ip) {
        String _ip = et_ip.getText().toString();
        StringBuffer sb2 = new StringBuffer();
        if (_ip.length() == 8) {//2个为一组
            char[] _char = _ip.toCharArray();
            StringBuffer sb1 = new StringBuffer();
            for (int i = 0; i < _char.length; i++) {
                if (i % 2 == 0) {
                    sb1.append(IpUtil.getInstance().getCharByString(String.valueOf(_char[i])));
                } else {
                    sb1.append(IpUtil.getInstance().getCharByString(String.valueOf(_char[i])) + " ");
                }
            }

            String[] _str = sb1.toString().split(" ");

            for (int i = 0; i < _str.length; i++) {
                if (_str[i].contains("null")) {
                    ToastUtil.show(mContext, mContext.getResources().getString(R.string.ip_invalid));
                    return _ip;
                } else {
                    if (i == _str.length - 1) {
                        sb2.append(Integer.parseInt(_str[i], 16));
                    } else
                        sb2.append(Integer.parseInt(_str[i], 16) + ".");
                }
            }
        }
        return sb2.toString();
    }

    public String changeIp(String et_ip) {
        String _ip = et_ip;
        StringBuffer sb2 = new StringBuffer();
        if (_ip.length() == 8) {//2个为一组
            char[] _char = _ip.toCharArray();
            StringBuffer sb1 = new StringBuffer();
            for (int i = 0; i < _char.length; i++) {
                if (i % 2 == 0) {
                    sb1.append(IpUtil.getInstance().getCharByString(String.valueOf(_char[i])));
                } else {
                    sb1.append(IpUtil.getInstance().getCharByString(String.valueOf(_char[i])) + " ");
                }
            }
            String[] _str = sb1.toString().split(" ");

            for (int i = 0; i < _str.length; i++) {
                if (_str[i].contains("null")) {
                    ToastUtil.show(mContext, mContext.getResources().getString(R.string.ip_invalid));
                    return _ip;
                } else {
                    if (i == _str.length - 1) {
                        sb2.append(Integer.parseInt(_str[i], 16));
                    } else
                        sb2.append(Integer.parseInt(_str[i], 16) + ".");
                }
            }
        }
        return sb2.toString();
    }

    public String getBaseUrl() {
        String base = BaccaratUtil.getInstance().changeIp(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
        String baseUrl = ServiceIpConstant.BASE_BEFORE + base + ServiceIpConstant.BASE_AFTER;
        return baseUrl;
    }

    public String getScaleX(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format((float) a / b);
    }

    public void toast(RelativeLayout rl_show, TextView tv_show, String meg) {
        rl_show.setBackground(mContext.getResources().getDrawable(R.drawable.toast));
        tv_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit10));
        tv_show.setTextColor(Color.WHITE);
        tv_show.setText(meg);
        alphaShow(rl_show, 1300);
    }

    public void toast(RelativeLayout rl_show, TextView tv_show, String meg, int duration) {
        rl_show.setBackground(mContext.getResources().getDrawable(R.drawable.toast));
        tv_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit10));
        tv_show.setTextColor(Color.WHITE);
        tv_show.setText(meg);
        alphaShow(rl_show, duration);
    }

    public void win(RelativeLayout rl_show, TextView tv_show, int duration, String meg, Drawable background, float dimen, int color) {
        rl_show.setBackground(background);//background mContext.getResources().getDrawable(R.drawable.result_popup_gradient
        tv_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen);//dimen mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit16
        tv_show.setTextColor(color);//Color.RED
        tv_show.setText(meg);
        alphaShow(rl_show, duration);
    }

    public void alphaShow(View view, int duration) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 1f, 0f);
        alpha.setDuration(duration);
        alpha.setRepeatMode(ObjectAnimator.RESTART);
        alpha.setInterpolator(new AccelerateInterpolator());
        alpha.start();
    }

    public static void main(String[] args) {
        BigDecimal b1 = new BigDecimal("5020000.1");
        BigDecimal b2 = new BigDecimal("50.0");
        System.out.println(b1.subtract(b2).toString());
    }
}
