package com.purity.yu.gameplatform.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.baccarat.BeadPlateNode;
import com.purity.yu.gameplatform.baccarat.FourCircleNode;
import com.purity.yu.gameplatform.baccarat.FourLineNode;
import com.purity.yu.gameplatform.baccarat.FourRingsNode;
import com.purity.yu.gameplatform.baccarat.NodeImp;
import com.purity.yu.gameplatform.baccarat.SingleRingNode;
import com.purity.yu.gameplatform.baccarat.YHZGridView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 功能：百家乐算法规则
 * 描述：
 * Create by purity on 2018/7/10.
 */
public class AlgorithmMacau {
    private static AlgorithmMacau instance = null;

    //0庄 1闲
    List<Integer> bigEyeRoadList_color1 = new ArrayList<>();// 大路原始数据 红红蓝蓝按顺序存 0红圈 1蓝圈 转成二维便于其他3路分析-
    List<Integer> bigEyeRoad = new ArrayList<>();// 大眼路原始数据(转成二维就可以直接画出来了)

    //0庄 1闲
    List<Integer> smallRoad = new ArrayList<>();// 小路原始数据(转成二维就可以直接画出来了)

    //0庄 1闲
    List<Integer> cockroachRoad = new ArrayList<>();// 小强路原始数据(转成二维就可以直接画出来了)

    private AlgorithmMacau() {
    }

    public static AlgorithmMacau getInstance() {
        if (null == instance) {
            synchronized (AlgorithmMacau.class) {
                if (null == instance) {
                    instance = new AlgorithmMacau();
                }
            }
        }
        return instance;
    }

    /**
     * 计算珠盘路列数 最多13列
     */
    public void calBeadRoadLine(List<Integer> boardMessageList, List<Integer> beadRoadList) {
        //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
        beadRoadList.addAll(boardMessageList);
    }

    /**
     * 绘制珠盘路
     */
    private void drawBeadRoad(List<Integer> beadRoadList, YHZGridView gv_left, Context mContext, int gameType, int bankColor, int playColor, int tieColor) {
        String _banker = "", _player = "";
        if (gameType == 0) {
            _banker = mContext.getResources().getString(R.string.banker);
            _player = mContext.getResources().getString(R.string.player);
        } else if (gameType == 1) {
            _banker = mContext.getResources().getString(R.string.dragon);
            _player = mContext.getResources().getString(R.string.tiger);
        }
        String _tie = mContext.getResources().getString(R.string.tie);
        List<NodeImp> list1 = new ArrayList<>();
        list1.clear();
        for (int i = 0; i < beadRoadList.size(); i++) {
            if (i < 6) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 0, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 12 && i > 5) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 1, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 18 && i > 11) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 2, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 24 && i > 17) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 3, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 30 && i > 23) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 4, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 36 && i > 29) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 5, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 42 && i > 35) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 6, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 48 && i > 41) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 7, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 54 && i > 47) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 8, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 60 && i > 53) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 9, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 66 && i > 59) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 10, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 72 && i > 63) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 11, gameType, bankColor, playColor, tieColor, mContext);
            }
            if (i < 78 && i > 69) {
                i = initFontNode(beadRoadList, list1, _banker, _player, _tie, i, 12, gameType, bankColor, playColor, tieColor, mContext);
            }
        }
        gv_left.setNodeList(list1);
        gv_left.postInvalidate();
    }

    private int initFontNode(List<Integer> boardMessageList, List<NodeImp> list, String _banker, String _player, String _tie, int i, int i2, int gameType, int bankColor, int playColor, int tieColor, Context mContext) {
        for (int j = 0; j < 6; j++, i++) {
            if (i < boardMessageList.size()) {
                if (gameType == 0) {
                    if (boardMessageList.get(i) == 0) {
                        list.add(new BeadPlateNode(i2, j, _banker, bankColor, false, false, mContext));
                    } else if (boardMessageList.get(i) == 1) {
                        list.add(new BeadPlateNode(i2, j, _player, playColor, false, false, mContext));
                    } else if (boardMessageList.get(i) == 2) {
                        list.add(new BeadPlateNode(i2, j, _tie, tieColor, false, false, mContext));
                    } else if (boardMessageList.get(i) == 3) {
                        list.add(new BeadPlateNode(i2, j, _banker, bankColor, true, false, mContext));
                    } else if (boardMessageList.get(i) == 4) {
                        list.add(new BeadPlateNode(i2, j, _banker, bankColor, false, true, mContext));
                    } else if (boardMessageList.get(i) == 5) {
                        list.add(new BeadPlateNode(i2, j, _banker, bankColor, true, true, mContext));
                    } else if (boardMessageList.get(i) == 6) {
                        list.add(new BeadPlateNode(i2, j, _player, playColor, true, false, mContext));
                    } else if (boardMessageList.get(i) == 7) {
                        list.add(new BeadPlateNode(i2, j, _player, playColor, false, true, mContext));
                    } else if (boardMessageList.get(i) == 8) {
                        list.add(new BeadPlateNode(i2, j, _player, playColor, true, true, mContext));
                    } else if (boardMessageList.get(i) == 9) {
                        list.add(new BeadPlateNode(i2, j, _tie, tieColor, true, false, mContext));
                    } else if (boardMessageList.get(i) == 10) {
                        list.add(new BeadPlateNode(i2, j, _tie, tieColor, false, true, mContext));
                    } else if (boardMessageList.get(i) == 11) {
                        list.add(new BeadPlateNode(i2, j, _tie, tieColor, true, true, mContext));
                    }
                } else if (gameType == 1) {
                    if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {
                        list.add(new BeadPlateNode(i2, j, _banker, Color.RED, false, false));
                    } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {
                        list.add(new BeadPlateNode(i2, j, _player, Color.BLUE, false, false));
                    } else if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {
                        list.add(new BeadPlateNode(i2, j, _tie, Color.GREEN, false, false));
                    }
                }
            }
        }
        return i;
    }

    public void remove(List<Integer> list, int target) {
        Iterator<Integer> iter = list.iterator();
        while (iter.hasNext()) {
            int item = iter.next();
            if (item == target) {
                iter.remove();
            }
        }
    }

    /**
     * 根据所有珠盘路数据生成对应大路所需数据 在根据大路数据生成展开的大路和缩小的大路
     * <p>
     * 思路：初始化大路的同时初始化大眼路,小路和小强路
     *
     * @param boardMessageList
     */
    public List<List<Integer>> initBigRoad(List<Integer> boardMessageList, List<Integer> beadRoadList, List<Integer> beadRoadListShort,
                                           List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadList, List<List<Integer>> bigRoadListShort,
                                           List<List<Integer>> bigEyeRoadListAll, List<List<Integer>> bigEyeRoadList, List<List<Integer>> bigEyeRoadListShort,
                                           List<List<Integer>> smallRoadListAll, List<List<Integer>> smallRoadList, List<List<Integer>> smallRoadListShort,
                                           List<List<Integer>> cockroachRoadListAll, List<List<Integer>> cockroachRoadList, List<List<Integer>> cockroachRoadListShort,
                                           List<Integer> maxScoreListAll, List<Integer> maxScoreList, Context mContext, int type, boolean isShowAsk,
                                           ImageView iv_ask_bank_1, ImageView iv_ask_bank_2, ImageView iv_ask_bank_3, ImageView iv_ask_play_1, ImageView iv_ask_play_2, ImageView iv_ask_play_3,
                                           int bankColor, int playColor, int tieColor) {
        //0庄 1闲 2和 3庄和 4庄和2  5闲和 6闲和2 7庄和3 8闲和3 9和2 10和3 11和4
        int y = 0;//大路坐标
        int currentLine = 0;//++x 当前列
        int r = 0, l = 0;//将一维转成二维需要的行和列 r=x l=y
        beadRoadList.clear();
        beadRoadListShort.clear();
        bigRoadListAll.clear();
        bigRoadList.clear();
        bigRoadListShort.clear();
        bigEyeRoadListAll.clear();
        bigEyeRoadList.clear();
        bigEyeRoadListShort.clear();
        smallRoadListAll.clear();
        smallRoadList.clear();
        smallRoadListShort.clear();
        cockroachRoadListAll.clear();
        cockroachRoadList.clear();
        cockroachRoadListShort.clear();

        if (bigEyeRoadList_color1.size() > 0) {
            bigEyeRoadList_color1.clear();
        }
        if (bigEyeRoad.size() > 0) {
            bigEyeRoad.clear();
        }
        if (smallRoad.size() > 0) {
            smallRoad.clear();
        }
        if (cockroachRoad.size() > 0) {
            cockroachRoad.clear();
        }
        SharedPreUtil.getInstance(mContext).saveParam("tie0", 0);//对第0,0位置为和的情况进行区分
        for (int i = 0; i < boardMessageList.size(); i++) {
            List<Integer> _temp = new ArrayList<>();
            _temp.clear();
            if (i == 0) {//第0,0位置
                if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//0,0 庄
                    _temp.add(0);
                    bigEyeRoadList_color1.add(0);
                } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//0,0 闲
                    _temp.add(1);
                    bigEyeRoadList_color1.add(1);
                } else if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {//0,0 和
                    _temp.add(2);
                    bigEyeRoadList_color1.add(2);
                }
                bigRoadListAll.add(_temp);
            } else if (i > 0) {//前1个与当前
                int tie0 = SharedPreUtil.getInstance(mContext).getInt("tie0");
                if ((boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5)
                        && (boardMessageList.get(0) == 2 || boardMessageList.get(0) == 9 || boardMessageList.get(0) == 10 || boardMessageList.get(0) == 11) && tie0 == 0) {//和庄
                    y = 0;
                    currentLine++;
                    List<Integer> __temp = new ArrayList<>();
                    bigRoadListAll.add(new ArrayList<Integer>());
                    bigRoadListAll.get(currentLine).add(0);
                    bigEyeRoadList_color1.add(0);
                    SharedPreUtil.getInstance(mContext).saveParam("tie0", 1);
                } else if ((boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8)
                        && (boardMessageList.get(0) == 2 || boardMessageList.get(0) == 9 || boardMessageList.get(0) == 10 || boardMessageList.get(0) == 11) && tie0 == 0) {//和闲
                    y = 0;
                    currentLine++;
                    List<Integer> __temp = new ArrayList<>();
                    bigRoadListAll.add(__temp);
                    bigRoadListAll.get(currentLine).add(1);
                    bigEyeRoadList_color1.add(1);
                    SharedPreUtil.getInstance(mContext).saveParam("tie0", 1);
                } else if ((boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11)
                        && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//0,0 和和
                    if (i > 1) {
                        if ((boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11)
                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)
                                && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)) {
                            ++y;//用来获取当前bigRoadListAll的值
                            bigRoadListAll.get(currentLine).add(11);
                        } else if ((boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11)
                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)
                                && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)) {
                            ++y;//用来获取当前bigRoadListAll的值
                            bigRoadListAll.get(currentLine).add(10);
                        } else if ((boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11)
                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)
                                && (boardMessageList.get(i - 2) != 2 || boardMessageList.get(i - 2) != 9 || boardMessageList.get(i - 2) != 10 || boardMessageList.get(i - 2) != 11)) {
                            ++y;//用来获取当前bigRoadListAll的值
                            bigRoadListAll.get(currentLine).add(9);//前2个为和第3个不为和
                        }
                    } else {
                        ++y;//用来获取当前bigRoadListAll的值
                        bigRoadListAll.get(currentLine).add(9);
                    }
                } else if ((boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5)
                        && (boardMessageList.get(i - 1) == 0 || boardMessageList.get(i - 1) == 3 || boardMessageList.get(i - 1) == 4 || boardMessageList.get(i - 1) == 5)) {//0,1 庄庄
                    ++y;
                    bigRoadListAll.get(currentLine).add(0);
                    bigEyeRoadList_color1.add(0);
                } else if ((boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5)
                        && (boardMessageList.get(i - 1) == 1 || boardMessageList.get(i - 1) == 6 || boardMessageList.get(i - 1) == 7 || boardMessageList.get(i - 1) == 8)) {//1,0 闲 庄
                    y = 0;
                    currentLine++;
                    List<Integer> __temp = new ArrayList<>();
                    bigRoadListAll.add(__temp);
                    bigRoadListAll.get(currentLine).add(0);
                    bigEyeRoadList_color1.add(0);
                } else if ((boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8)
                        && (boardMessageList.get(i - 1) == 0 || boardMessageList.get(i - 1) == 3 || boardMessageList.get(i - 1) == 4 || boardMessageList.get(i - 1) == 5)) {//1,0 庄 闲
                    y = 0;
                    currentLine++;
                    List<Integer> __temp = new ArrayList<>();
                    bigRoadListAll.add(__temp);
                    bigRoadListAll.get(currentLine).add(1);
                    bigEyeRoadList_color1.add(1);
                } else if ((boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8)
                        && (boardMessageList.get(i - 1) == 1 || boardMessageList.get(i - 1) == 6 || boardMessageList.get(i - 1) == 7 || boardMessageList.get(i - 1) == 8)) {//0,1 闲 闲
                    ++y;
                    bigRoadListAll.get(currentLine).add(1);
                    bigEyeRoadList_color1.add(1);
                } else if ((boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11)
                        && (boardMessageList.get(i - 1) == 0 || boardMessageList.get(i - 1) == 3 || boardMessageList.get(i - 1) == 4 || boardMessageList.get(i - 1) == 5)) {//0,0 庄和
                    ++y;//用来获取当前bigRoadListAll的值
                    bigRoadListAll.get(currentLine).add(3);
                } else if ((boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11)
                        && (boardMessageList.get(i - 1) == 1 || boardMessageList.get(i - 1) == 6 || boardMessageList.get(i - 1) == 7 || boardMessageList.get(i - 1) == 8)) {//0,0 闲和
                    ++y;//用来获取当前bigRoadListAll的值
                    bigRoadListAll.get(currentLine).add(5);
                } else if (i > 1) {//前2个与当前
                    if ((boardMessageList.get(i - 2) == 0 || boardMessageList.get(i - 2) == 3 || boardMessageList.get(i - 2) == 4 || boardMessageList.get(i - 2) == 5)
                            && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//前2个为 庄和
                        if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第3个为庄
                            ++y;
                            bigRoadListAll.get(currentLine).add(0);
                            bigEyeRoadList_color1.add(0);
                        } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第3个为闲
                            y = 0;
                            currentLine++;
                            List<Integer> __temp = new ArrayList<>();
                            bigRoadListAll.add(__temp);
                            bigRoadListAll.get(currentLine).add(1);
                            bigEyeRoadList_color1.add(1);
                        } else if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {//第3个为和
                            ++y;//用来获取当前bigRoadListAll的值
                            bigRoadListAll.get(currentLine).add(4);//永远没有？？？
                        }
                    } else if ((boardMessageList.get(i - 2) == 1 || boardMessageList.get(i - 2) == 6 || boardMessageList.get(i - 2) == 7 || boardMessageList.get(i - 2) == 8)
                            && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//前2个为 闲和
                        if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第3个为闲
                            ++y;
                            bigRoadListAll.get(currentLine).add(1);
                            bigEyeRoadList_color1.add(1);
                        } else if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第3个为庄
                            y = 0;
                            currentLine++;
                            List<Integer> __temp = new ArrayList<>();
                            bigRoadListAll.add(__temp);
                            bigRoadListAll.get(currentLine).add(0);
                            bigEyeRoadList_color1.add(0);
                        } else if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {//第3个为和
                            ++y;//用来获取当前bigRoadListAll的值
                            bigRoadListAll.get(currentLine).add(6);//永远没有？？？
                        }
                    } else if (boardMessageList.size() == 3 && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                            && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//前2个为 和和
                        if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第3个为庄
                            y = 0;
                            currentLine++;
                            List<Integer> __temp = new ArrayList<>();
                            bigRoadListAll.add(__temp);
                            bigRoadListAll.get(currentLine).add(0);
                            bigEyeRoadList_color1.add(0);
                        } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第3个为闲
                            y = 0;
                            currentLine++;
                            List<Integer> __temp = new ArrayList<>();
                            bigRoadListAll.add(__temp);
                            bigRoadListAll.get(currentLine).add(1);
                            bigEyeRoadList_color1.add(1);
                        } else if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {//第3个为和
                            ++y;//用来获取当前bigRoadListAll的值
                            bigRoadListAll.get(currentLine).add(10);
                        }
                    } else if (i > 2) {//前3个与当前
                        if ((boardMessageList.get(i - 3) == 0 || boardMessageList.get(i - 3) == 3 || boardMessageList.get(i - 3) == 4 || boardMessageList.get(i - 3) == 5)
                                && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//庄和和
                            if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {//第4个为和
                                ++y;//用来获取当前bigRoadListAll的值
                                bigRoadListAll.get(currentLine).add(7);
                            } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第4个为闲
                                y = 0;
                                currentLine++;
                                List<Integer> __temp = new ArrayList<>();
                                bigRoadListAll.add(__temp);
                                bigRoadListAll.get(currentLine).add(1);
                                bigEyeRoadList_color1.add(1);
                            } else if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第4个为庄
                                ++y;
                                bigRoadListAll.get(currentLine).add(0);
                                bigEyeRoadList_color1.add(0);
                            }
                        } else if ((boardMessageList.get(i - 3) == 1 || boardMessageList.get(i - 3) == 6 || boardMessageList.get(i - 3) == 7 || boardMessageList.get(i - 3) == 8)
                                && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//闲和和
                            if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {//第4个为和
                                ++y;//用来获取当前bigRoadListAll的值
                                bigRoadListAll.get(currentLine).add(8);
                            } else if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第4个为庄
                                y = 0;
                                currentLine++;
                                List<Integer> __temp = new ArrayList<>();
                                bigRoadListAll.add(__temp);
                                bigRoadListAll.get(currentLine).add(0);
                                bigEyeRoadList_color1.add(0);
                            } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第4个为闲
                                ++y;
                                bigRoadListAll.get(currentLine).add(1);
                                bigEyeRoadList_color1.add(1);
                            }
                        } else if (i > 3) {
                            if ((boardMessageList.get(i - 4) == 0 || boardMessageList.get(i - 4) == 3 || boardMessageList.get(i - 4) == 4 || boardMessageList.get(i - 4) == 5)
                                    && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                    && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                    && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//庄和和和
                                if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第5个为庄
                                    ++y;
                                    bigRoadListAll.get(currentLine).add(0);
                                    bigEyeRoadList_color1.add(0);
                                } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第5个为闲
                                    y = 0;
                                    currentLine++;
                                    List<Integer> __temp = new ArrayList<>();
                                    bigRoadListAll.add(__temp);
                                    bigRoadListAll.get(currentLine).add(1);
                                    bigEyeRoadList_color1.add(1);
                                }
                            } else if ((boardMessageList.get(i - 4) == 1 || boardMessageList.get(i - 4) == 6 || boardMessageList.get(i - 4) == 7 || boardMessageList.get(i - 4) == 8)
                                    && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                    && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                    && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//闲和和和
                                if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第5个为庄
                                    y = 0;
                                    currentLine++;
                                    List<Integer> __temp = new ArrayList<>();
                                    bigRoadListAll.add(__temp);
                                    bigRoadListAll.get(currentLine).add(0);
                                    bigEyeRoadList_color1.add(0);
                                } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第5个为闲
                                    ++y;
                                    bigRoadListAll.get(currentLine).add(1);
                                    bigEyeRoadList_color1.add(1);
                                }
                            } else if (i > 4) {
                                if ((boardMessageList.get(i - 5) == 0 || boardMessageList.get(i - 5) == 3 || boardMessageList.get(i - 5) == 4 || boardMessageList.get(i - 5) == 5)
                                        && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                        && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                        && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                        && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//庄和和和和
                                    if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第6个为庄
                                        ++y;
                                        bigRoadListAll.get(currentLine).add(0);
                                        bigEyeRoadList_color1.add(0);
                                    } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第6个为闲
                                        y = 0;
                                        currentLine++;
                                        List<Integer> __temp = new ArrayList<>();
                                        bigRoadListAll.add(__temp);
                                        bigRoadListAll.get(currentLine).add(1);
                                        bigEyeRoadList_color1.add(1);
                                    }
                                } else if ((boardMessageList.get(i - 5) == 1 || boardMessageList.get(i - 5) == 6 || boardMessageList.get(i - 5) == 7 || boardMessageList.get(i - 5) == 8)
                                        && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                        && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                        && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                        && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//闲和和和和
                                    if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第6个为庄
                                        y = 0;
                                        currentLine++;
                                        List<Integer> __temp = new ArrayList<>();
                                        bigRoadListAll.add(__temp);
                                        bigRoadListAll.get(currentLine).add(0);
                                        bigEyeRoadList_color1.add(0);
                                    } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第6个为闲
                                        ++y;
                                        bigRoadListAll.get(currentLine).add(1);
                                        bigEyeRoadList_color1.add(1);
                                    }
                                } else if (i > 5) {
                                    if ((boardMessageList.get(i - 6) == 0 || boardMessageList.get(i - 6) == 3 || boardMessageList.get(i - 6) == 4 || boardMessageList.get(i - 6) == 5)
                                            && (boardMessageList.get(i - 5) == 2 || boardMessageList.get(i - 5) == 9 || boardMessageList.get(i - 5) == 10 || boardMessageList.get(i - 5) == 11)
                                            && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                            && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                            && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                            && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//庄和和和和和
                                        if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第7个为庄
                                            ++y;
                                            bigRoadListAll.get(currentLine).add(0);
                                            bigEyeRoadList_color1.add(0);
                                        } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第7个为闲
                                            y = 0;
                                            currentLine++;
                                            List<Integer> __temp = new ArrayList<>();
                                            bigRoadListAll.add(__temp);
                                            bigRoadListAll.get(currentLine).add(1);
                                            bigEyeRoadList_color1.add(1);
                                        }
                                    } else if ((boardMessageList.get(i - 6) == 1 || boardMessageList.get(i - 6) == 6 || boardMessageList.get(i - 6) == 7 || boardMessageList.get(i - 6) == 8)
                                            && (boardMessageList.get(i - 5) == 2 || boardMessageList.get(i - 5) == 9 || boardMessageList.get(i - 5) == 10 || boardMessageList.get(i - 5) == 11)
                                            && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                            && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                            && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                            && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//闲和和和和和
                                        if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第7个为庄
                                            y = 0;
                                            currentLine++;
                                            List<Integer> __temp = new ArrayList<>();
                                            bigRoadListAll.add(__temp);
                                            bigRoadListAll.get(currentLine).add(0);
                                            bigEyeRoadList_color1.add(0);
                                        } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第7个为闲
                                            ++y;
                                            bigRoadListAll.get(currentLine).add(1);
                                            bigEyeRoadList_color1.add(1);
                                        }
                                    } else if (i > 6) {
                                        if ((boardMessageList.get(i - 7) == 0 || boardMessageList.get(i - 7) == 3 || boardMessageList.get(i - 7) == 4 || boardMessageList.get(i - 7) == 5)
                                                && (boardMessageList.get(i - 6) == 2 || boardMessageList.get(i - 6) == 9 || boardMessageList.get(i - 6) == 10 || boardMessageList.get(i - 6) == 11)
                                                && (boardMessageList.get(i - 5) == 2 || boardMessageList.get(i - 5) == 9 || boardMessageList.get(i - 5) == 10 || boardMessageList.get(i - 5) == 11)
                                                && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                                && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                                && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//庄和和和和和
                                            if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第7个为庄
                                                ++y;
                                                bigRoadListAll.get(currentLine).add(0);
                                                bigEyeRoadList_color1.add(0);
                                            } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第7个为闲
                                                y = 0;
                                                currentLine++;
                                                List<Integer> __temp = new ArrayList<>();
                                                bigRoadListAll.add(__temp);
                                                bigRoadListAll.get(currentLine).add(1);
                                                bigEyeRoadList_color1.add(1);
                                            }
                                        } else if ((boardMessageList.get(i - 7) == 1 || boardMessageList.get(i - 7) == 6 || boardMessageList.get(i - 7) == 7 || boardMessageList.get(i - 7) == 8)
                                                && (boardMessageList.get(i - 6) == 2 || boardMessageList.get(i - 6) == 9 || boardMessageList.get(i - 6) == 10 || boardMessageList.get(i - 6) == 11)
                                                && (boardMessageList.get(i - 5) == 2 || boardMessageList.get(i - 5) == 9 || boardMessageList.get(i - 5) == 10 || boardMessageList.get(i - 5) == 11)
                                                && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                                && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                                && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                                && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//闲和和和和和
                                            if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第7个为庄
                                                y = 0;
                                                currentLine++;
                                                List<Integer> __temp = new ArrayList<>();
                                                bigRoadListAll.add(__temp);
                                                bigRoadListAll.get(currentLine).add(0);
                                                bigEyeRoadList_color1.add(0);
                                            } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第7个为闲
                                                ++y;
                                                bigRoadListAll.get(currentLine).add(1);
                                                bigEyeRoadList_color1.add(1);
                                            }
                                        } else if (i > 7) {
                                            if ((boardMessageList.get(i - 8) == 0 || boardMessageList.get(i - 8) == 3 || boardMessageList.get(i - 8) == 4 || boardMessageList.get(i - 8) == 5)
                                                    && (boardMessageList.get(i - 7) == 2 || boardMessageList.get(i - 7) == 9 || boardMessageList.get(i - 7) == 10 || boardMessageList.get(i - 7) == 11)
                                                    && (boardMessageList.get(i - 6) == 2 || boardMessageList.get(i - 6) == 9 || boardMessageList.get(i - 6) == 10 || boardMessageList.get(i - 6) == 11)
                                                    && (boardMessageList.get(i - 5) == 2 || boardMessageList.get(i - 5) == 9 || boardMessageList.get(i - 5) == 10 || boardMessageList.get(i - 5) == 11)
                                                    && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                                    && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                                    && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                                    && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//庄和和和和和
                                                if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第7个为庄
                                                    ++y;
                                                    bigRoadListAll.get(currentLine).add(0);
                                                    bigEyeRoadList_color1.add(0);
                                                } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第7个为闲
                                                    y = 0;
                                                    currentLine++;
                                                    List<Integer> __temp = new ArrayList<>();
                                                    bigRoadListAll.add(__temp);
                                                    bigRoadListAll.get(currentLine).add(1);
                                                    bigEyeRoadList_color1.add(1);
                                                }
                                            } else if ((boardMessageList.get(i - 8) == 1 || boardMessageList.get(i - 8) == 6 || boardMessageList.get(i - 8) == 7 || boardMessageList.get(i - 8) == 8)
                                                    && (boardMessageList.get(i - 7) == 2 || boardMessageList.get(i - 7) == 9 || boardMessageList.get(i - 7) == 10 || boardMessageList.get(i - 7) == 11)
                                                    && (boardMessageList.get(i - 6) == 2 || boardMessageList.get(i - 6) == 9 || boardMessageList.get(i - 6) == 10 || boardMessageList.get(i - 6) == 11)
                                                    && (boardMessageList.get(i - 5) == 2 || boardMessageList.get(i - 5) == 9 || boardMessageList.get(i - 5) == 10 || boardMessageList.get(i - 5) == 11)
                                                    && (boardMessageList.get(i - 4) == 2 || boardMessageList.get(i - 4) == 9 || boardMessageList.get(i - 4) == 10 || boardMessageList.get(i - 4) == 11)
                                                    && (boardMessageList.get(i - 3) == 2 || boardMessageList.get(i - 3) == 9 || boardMessageList.get(i - 3) == 10 || boardMessageList.get(i - 3) == 11)
                                                    && (boardMessageList.get(i - 2) == 2 || boardMessageList.get(i - 2) == 9 || boardMessageList.get(i - 2) == 10 || boardMessageList.get(i - 2) == 11)
                                                    && (boardMessageList.get(i - 1) == 2 || boardMessageList.get(i - 1) == 9 || boardMessageList.get(i - 1) == 10 || boardMessageList.get(i - 1) == 11)) {//闲和和和和和
                                                if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {//第7个为庄
                                                    y = 0;
                                                    currentLine++;
                                                    List<Integer> __temp = new ArrayList<>();
                                                    bigRoadListAll.add(__temp);
                                                    bigRoadListAll.get(currentLine).add(0);
                                                    bigEyeRoadList_color1.add(0);
                                                } else if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {//第7个为闲
                                                    ++y;
                                                    bigRoadListAll.get(currentLine).add(1);
                                                    bigEyeRoadList_color1.add(1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //将01一维转成二维01各一列
            if (bigEyeRoadList_color1.size() > 0) {
                if (bigEyeRoadListAll.size() == 0) {
                    bigEyeRoadListAll.add(new ArrayList<Integer>());
                    bigEyeRoadListAll.get(r).add(bigEyeRoadList_color1.get(l++));
                } else if (l < bigEyeRoadList_color1.size()) {
                    if (!bigEyeRoadList_color1.get(l).equals(bigEyeRoadList_color1.get(l - 1))) {
                        bigEyeRoadListAll.add(new ArrayList<Integer>());
                        bigEyeRoadListAll.get(++r).add(bigEyeRoadList_color1.get(l));
                    } else {
                        bigEyeRoadListAll.get(r).add(bigEyeRoadList_color1.get(l));
                    }
                    l++;
                }
            }

            //----------------------------------------------------------------------小强路分析 start-------------------------------------------------------------------------------------------------
            if (bigEyeRoadListAll.size() > 4 && bigEyeRoadListAll.get(3).size() == 1 && (bigRoadListAll.get(currentLine).get(y) == 0 || bigRoadListAll.get(currentLine).get(y) == 1)) {//第5列第1行
                if (currentLine == 4 && y == 0) {//有自己的y
                    if ((bigEyeRoadListAll.get(0).get(0) == 2 || bigEyeRoadListAll.get(0).size() == 1) && bigEyeRoadListAll.get(3).size() == 1
                            || (bigEyeRoadListAll.get(0).get(0) != 2 && bigEyeRoadListAll.get(0).size() == bigEyeRoadListAll.get(3).size())) {//第一个红色 第1列和第4列齐整
                        cockroachRoad.add(0);
                    } else if ((bigEyeRoadListAll.get(0).size() != 1 || bigEyeRoadListAll.get(0).get(0) != 2)) {//第一个蓝色 第1列和第4列不齐整
                        cockroachRoad.add(1);
                    }
                } else {
                    analyzeSmallCockroach(bigEyeRoadListAll, cockroachRoad, y, currentLine, 1, 3, 4);
                }
            } else if (bigEyeRoadListAll.size() > 3 && bigEyeRoadListAll.get(3).size() > 1 && (bigRoadListAll.get(currentLine).get(y) == 0 || bigRoadListAll.get(currentLine).get(y) == 1)) {//第4列第2行
                if (currentLine == 3 && y == 1) {
                    if (bigEyeRoadListAll.get(0).get(0) == 2 || bigEyeRoadListAll.get(0).size() == 1) {//第一个蓝色
                        cockroachRoad.add(1);
                    } else if (bigEyeRoadListAll.get(0).get(0) != 2 && bigEyeRoadListAll.get(0).size() > 1) {//第一个红色
                        cockroachRoad.add(0);
                    }
                } else {
                    analyzeSmallCockroach(bigEyeRoadListAll, cockroachRoad, y, currentLine, 1, 3, 4);
                }
            }
            //----------------------------------------------------------------------小强路分析 end---------------------------------------------------------------------------------------------------

            //----------------------------------------------------------------------小路分析 start-------------------------------------------------------------------------------------------------
            if (bigEyeRoadListAll.size() > 3 && bigEyeRoadListAll.get(2).size() == 1 && (bigRoadListAll.get(currentLine).get(y) == 0 || bigRoadListAll.get(currentLine).get(y) == 1)) {//第4列第1行
                if (currentLine == 3 && y == 0) {//有自己的y
                    if ((bigEyeRoadListAll.get(0).get(0) == 2 || bigEyeRoadListAll.get(0).size() == 1) && bigEyeRoadListAll.get(2).size() == 1
                            || (bigEyeRoadListAll.get(0).get(0) != 2 && bigEyeRoadListAll.get(0).size() == bigEyeRoadListAll.get(2).size())) {//第一个红色 第1列和第3列齐整
                        smallRoad.add(0);
                    } else if ((bigEyeRoadListAll.get(0).size() != 1 || bigEyeRoadListAll.get(0).get(0) != 2)) {//第一个蓝色 第1列和第3列不齐整
                        smallRoad.add(1);
                    }
                } else {
                    analyzeSmallCockroach(bigEyeRoadListAll, smallRoad, y, currentLine, 1, 2, 3);
                }
            } else if (bigEyeRoadListAll.size() > 2 && bigEyeRoadListAll.get(2).size() > 1 && (bigRoadListAll.get(currentLine).get(y) == 0 || bigRoadListAll.get(currentLine).get(y) == 1)) {//第3列第2行
                if (currentLine == 2 && y == 1) {
                    if (bigEyeRoadListAll.get(0).get(0) == 2 || bigEyeRoadListAll.get(0).size() == 1) {//第一个蓝色
                        smallRoad.add(1);
                    } else if (bigEyeRoadListAll.get(0).get(0) != 2 && bigEyeRoadListAll.get(0).size() > 1) {//第一个红色
                        smallRoad.add(0);
                    }
                } else {
                    analyzeSmallCockroach(bigEyeRoadListAll, smallRoad, y, currentLine, 1, 2, 3);
                }
            }
            //----------------------------------------------------------------------小路分析 end---------------------------------------------------------------------------------------------------

            //----------------------------------------------------------------------大眼路分析 start-------------------------------------------------------------------------------------------------
            //根据长度将值保存在一维中（结果就是要画出来的）
            if (bigEyeRoadListAll.size() > 2 && bigEyeRoadListAll.get(1).size() == 1 && (bigRoadListAll.get(currentLine).get(y) == 0 || bigRoadListAll.get(currentLine).get(y) == 1)) {//第3列第1行
                if (currentLine == 2 && y == 0) {//有自己的y
                    if ((bigEyeRoadListAll.get(0).get(0) == 2 || bigEyeRoadListAll.get(0).size() == 1)) {//第一个红色 前2列齐整
                        bigEyeRoad.add(0);
                    } else if ((bigEyeRoadListAll.get(0).size() != 1 || bigEyeRoadListAll.get(0).get(0) != 2)) {//第一个蓝色 前2列不齐整
                        bigEyeRoad.add(1);
                    }
                } else {
                    analyzeBigEye(bigEyeRoadListAll, y, currentLine, 1, 2);
                }
            } else if (bigEyeRoadListAll.size() > 1 && bigEyeRoadListAll.get(1).size() > 1 && (bigRoadListAll.get(currentLine).get(y) == 0 || bigRoadListAll.get(currentLine).get(y) == 1)) {//第2列第2行
                if (currentLine == 1 && y == 1) {
                    if (bigEyeRoadListAll.get(0).get(0) == 2 || bigEyeRoadListAll.get(0).size() == 1) {//第一个蓝色
                        bigEyeRoad.add(1);
                    } else if (bigEyeRoadListAll.get(0).get(0) != 2 && bigEyeRoadListAll.get(0).size() > 1) {//第一个红色
                        bigEyeRoad.add(0);
                    }
                } else if ((currentLine == 1 && y != 0) || (currentLine > 1)) {
                    analyzeBigEye(bigEyeRoadListAll, y, currentLine, 1, 2);
                }
            }
            //----------------------------------------------------------------------大眼路分析 end------------------------------------------------------------------------------------------------------
        }
        //计算列数  36 68 将最终的一维结果又转回二维结果中，将这个二维结果进行列位移，画在界面上
        bigEyeRoadListAll.clear();
        initListAll(bigEyeRoadListAll, bigEyeRoad);
        initListAll(smallRoadListAll, smallRoad);
        initListAll(cockroachRoadListAll, cockroachRoad);
        if (type == 1) {
            calCockroachRoad(bigRoadListAll, bigRoadList, bigRoadListShort);
            calBigEyeLine(bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort);
            calCockroachRoad(smallRoadListAll, smallRoadList, smallRoadListShort);
            calCockroachRoad(cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort);
            //路单 大路40 大眼路20 小路20 小强
        } else {
            calBeadRoadLine(boardMessageList, beadRoadList);//both 1视频 2路单
            calBigSmallCockroachRoad(bigRoadListAll, bigRoadList);//30 新澳门5路 大路小路小强路
            calBigEyeLine(bigEyeRoadListAll, bigEyeRoadList);//60
            calBigSmallCockroachRoad(smallRoadListAll, smallRoadList);//30
            calBigSmallCockroachRoad(cockroachRoadListAll, cockroachRoadList);//30
            calMaxScore(bigRoadListAll, bigRoadList, maxScoreListAll, maxScoreList);
        }
        if (isShowAsk) {
            int lastBigEyeRoad = -1, lastSmallRoad = -1, lastCockroachRoad = -1;
            if (bigEyeRoadListAll.size() > 0) {
                lastBigEyeRoad = bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).get(bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() - 1);
            }
            if (smallRoadListAll.size() > 0) {
                lastSmallRoad = smallRoadListAll.get(smallRoadListAll.size() - 1).get(smallRoadListAll.get(smallRoadListAll.size() - 1).size() - 1);
            }
            if (cockroachRoadListAll.size() > 0) {
                lastCockroachRoad = cockroachRoadListAll.get(cockroachRoadListAll.size() - 1).get(cockroachRoadListAll.get(cockroachRoadListAll.size() - 1).size() - 1);
            }

            if (lastBigEyeRoad != -1) {
                GradientDrawable drawableDragon1 = setGradientDrawable(GradientDrawable.OVAL, bankColor, mContext);
                GradientDrawable drawableDragon2 = setGradientDrawable(GradientDrawable.OVAL, playColor, mContext);
                if (lastBigEyeRoad == 0) {
                    iv_ask_bank_1.setBackgroundDrawable(drawableDragon1);
                    iv_ask_play_1.setBackgroundDrawable(drawableDragon2);
                } else {
                    iv_ask_bank_1.setBackgroundDrawable(drawableDragon2);
                    iv_ask_play_1.setBackgroundDrawable(drawableDragon1);
                }
            }
            if (lastSmallRoad != -1) {
                GradientDrawable drawableDragon1 = setGradientDrawable2(GradientDrawable.OVAL, bankColor, mContext);
                GradientDrawable drawableDragon2 = setGradientDrawable2(GradientDrawable.OVAL, playColor, mContext);
                if (lastSmallRoad == 0) {
                    iv_ask_bank_2.setBackgroundDrawable(drawableDragon1);
                    iv_ask_play_2.setBackgroundDrawable(drawableDragon2);
                } else {
                    iv_ask_bank_2.setBackgroundDrawable(drawableDragon2);
                    iv_ask_play_2.setBackgroundDrawable(drawableDragon1);
                }
            }
            if (lastCockroachRoad != -1) {
                GradientDrawable drawableDragon1 = setGradientDrawable(GradientDrawable.LINE, bankColor, mContext);
                GradientDrawable drawableDragon2 = setGradientDrawable(GradientDrawable.LINE, playColor, mContext);
                if (lastCockroachRoad == 0) {
                    iv_ask_bank_3.setBackgroundDrawable(drawableDragon1);
                    iv_ask_play_3.setBackgroundDrawable(drawableDragon2);
                } else {
                    iv_ask_bank_3.setBackgroundDrawable(drawableDragon2);
                    iv_ask_play_3.setBackgroundDrawable(drawableDragon1);
                }
            }
        }
        return bigRoadListAll;
    }

    private GradientDrawable setGradientDrawable(int shape, int color, Context mContext) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(shape);
        gradientDrawable.setUseLevel(false);
        gradientDrawable.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), color);
        gradientDrawable.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit30), mContext.getResources().getDimensionPixelOffset(R.dimen.unit30));
        return gradientDrawable;
    }

    private GradientDrawable setGradientDrawable2(int shape, int color, Context mContext) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(shape);
        gradientDrawable.setUseLevel(false);
        gradientDrawable.setColor(color);
        gradientDrawable.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit30), mContext.getResources().getDimensionPixelOffset(R.dimen.unit30));
        return gradientDrawable;
    }

    private void calMaxScore(List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadList, List<Integer> maxScoreListAll, List<Integer> maxScoreList) {
        int removeMaxScoreCount = 0;
        if (bigRoadListAll.size() - bigRoadList.size() > 0) {
            int line = bigRoadListAll.size() - bigRoadList.size();
            for (int i = 0; i < line; i++) {
                for (int j = 0; j < bigRoadListAll.get(i).size(); j++) {
                    removeMaxScoreCount++;
                }
            }
            for (int i = removeMaxScoreCount; i < maxScoreListAll.size(); i++) {
                maxScoreList.add(maxScoreListAll.get(i));
            }
        } else {
            maxScoreList.addAll(maxScoreListAll);
        }
    }

    private void analyzeSmallCockroach(List<List<Integer>> bigEyeRoadListAll, List<Integer> smallRoad, int y, int currentLine, int p1, int p2, int p3) {
        if (bigEyeRoadListAll.get(currentLine).size() - bigEyeRoadListAll.get(currentLine - p2).size() == 1) {//蓝圈 当前列比前前一列大一个
            smallRoad.add(1);
        } else if (bigEyeRoadListAll.get(currentLine).size() - bigEyeRoadListAll.get(currentLine - p2).size() > 1) {//红圈 当前列比前前一列大2个
            smallRoad.add(0);
        } else if (y == 0 && bigEyeRoadListAll.get(currentLine - p3).size() != bigEyeRoadListAll.get(currentLine - p1).size()) {//蓝圈 不齐整
            smallRoad.add(1);
        } else if ((currentLine > p2 && bigEyeRoadListAll.get(currentLine - p3).size() == bigEyeRoadListAll.get(currentLine - p1).size())
                || bigEyeRoadListAll.get(currentLine).size() < bigEyeRoadListAll.get(currentLine - p2).size()
                || bigEyeRoadListAll.get(currentLine).size() == bigEyeRoadListAll.get(currentLine - p2).size()) {//红圈 齐整
            smallRoad.add(0);
        }
    }

    private void analyzeBigEye(List<List<Integer>> RoadListAll, int y, int currentLine, int p1, int p2) {
        if (RoadListAll.get(currentLine).size() - RoadListAll.get(currentLine - p1).size() == 1) {//蓝圈 当前列比前一列大一个
            bigEyeRoad.add(1);
        } else if (RoadListAll.get(currentLine).size() - RoadListAll.get(currentLine - p1).size() > 1) {//红圈 当前列比前一列大2个
            bigEyeRoad.add(0);
        } else if (y == 0 && RoadListAll.get(currentLine - p2).size() != RoadListAll.get(currentLine - p1).size()) {//蓝圈 不齐整
            bigEyeRoad.add(1);
        } else if ((currentLine > p1 && RoadListAll.get(currentLine - p2).size() == RoadListAll.get(currentLine - p1).size())
                || RoadListAll.get(currentLine).size() < RoadListAll.get(currentLine - p1).size()
                || RoadListAll.get(currentLine).size() == RoadListAll.get(currentLine - p1).size()) {//红圈 齐整或者当前列<=前一列
            bigEyeRoad.add(0);
        }
    }

    /**
     * 初始化大路列数
     *
     * @param init             起始列
     * @param bigRoadListAll   原始数据
     * @param bigRoadListShort 返回结果
     */
    private void initBigRoadLine(int init, List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadListShort) {
        bigRoadListShort.clear();
        int n = 0;
        for (int i = init; i < bigRoadListAll.size(); i++) {
            bigRoadListShort.add(new ArrayList<Integer>());
            for (int j = 0; j < bigRoadListAll.get(i).size(); j++) {
                bigRoadListShort.get(n).add(bigRoadListAll.get(i).get(j));
            }
            n++;
        }
    }

    private void initListAll(List<List<Integer>> bigEyeRoadListAll, List<Integer> bigEyeRoadList_color2) {
        if (bigEyeRoadList_color2.size() > 0) {
            int m = 0;
            for (int n = 0; n < bigEyeRoadList_color2.size(); n++) {//将一维01转成二维0一行1一行
                if (n < bigEyeRoadList_color2.size() - 1) {
                    if (bigEyeRoadList_color2.get(n) != bigEyeRoadList_color2.get(n + 1)) {
                        if (bigEyeRoadListAll.size() > m) {
                            bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                            m++;
                        } else {
                            bigEyeRoadListAll.add(new ArrayList<Integer>());
                            bigEyeRoadListAll.get(m++).add(bigEyeRoadList_color2.get(n));
                        }
                    } else {
                        if (bigEyeRoadListAll.size() == 0) {
                            bigEyeRoadListAll.add(new ArrayList<Integer>());
                            bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                        } else {
                            if (bigEyeRoadListAll.size() == m) {
                                bigEyeRoadListAll.add(new ArrayList<Integer>());
                                bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                            } else
                                bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                        }
                    }
                } else if (n == bigEyeRoadList_color2.size() - 1) {
                    if (n == 0) {
                        bigEyeRoadListAll.add(new ArrayList<Integer>());
                        bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                    } else if (bigEyeRoadList_color2.get(n) != bigEyeRoadList_color2.get(n - 1)) {
                        if (bigEyeRoadListAll.size() == m) {
                            bigEyeRoadListAll.add(new ArrayList<Integer>());
                            bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                        } else {
                            bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                        }
                    } else {
                        bigEyeRoadListAll.get(m).add(bigEyeRoadList_color2.get(n));
                    }
                }
            }
        }
    }

    /**
     * 计算大路,小路和小强路列数 最多9列
     */
    private void calCockroachRoad(List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadList, List<List<Integer>> bigRoadListShort) {
        /**
         * 根据原始数据列数判断
         * 短：1.<18 (17,16,15)  2.=18 =19...=45 倒叙(先大后小)判断
         * 长：1.<34 (33,32,31)  2.=34 =35...=45 同上
         */
        if (bigRoadListAll.size() < 18) {
            if (bigRoadListAll.size() == 17) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadListShort);
                } else
                    bigRoadListShort.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 16) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadListShort);
                } else
                    bigRoadListShort.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 15) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadListShort);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadListShort);
                } else
                    bigRoadListShort.addAll(bigRoadListAll);
            } else
                bigRoadListShort.addAll(bigRoadListAll);
        } else if (bigRoadListAll.size() == 45) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(36, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(35, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(34, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 44) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(35, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(34, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 43) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(34, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 42) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 41) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 40) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(35, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(34, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 39) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(34, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 38) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(33, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 37) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(32, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 36) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(31, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 35) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(30, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 34) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(29, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 33) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(28, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 32) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(27, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 31) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(26, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 30) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(25, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 29) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(24, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 28) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(23, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 27) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(22, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 26) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(21, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 25) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(20, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 24) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(19, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 23) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 22) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 21 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 22) {
                initBigRoadLine(18, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(17, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 21) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(16, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 20) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(15, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(3, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 19) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(14, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(3, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(2, bigRoadListAll, bigRoadListShort);
        } else if (bigRoadListAll.size() == 18) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(13, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(12, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(11, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(10, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(9, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(8, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(7, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(6, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(5, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(4, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(3, bigRoadListAll, bigRoadListShort);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(2, bigRoadListAll, bigRoadListShort);
            } else
                initBigRoadLine(1, bigRoadListAll, bigRoadListShort);
        }

        if (bigRoadListAll.size() < 34) {
            if (bigRoadListAll.size() == 33) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 32) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 31) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else
                bigRoadList.addAll(bigRoadListAll);
        } else if (bigRoadListAll.size() == 45) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 44) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 43) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 42) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 41) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 40) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 39) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 38) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 37) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 36) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 35) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 34) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(1, bigRoadListAll, bigRoadList);
        }
    }

    /**
     * 计算大路,小路和小强路列数 最多22列
     * smallRoadListAll smallRoadList
     */
    private void calCockroachRoad(List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadList) {
        /**
         * 根据原始数据列数判断
         * 短：1.<18 (17,16,15)  2.=18 =19...=45 倒叙(先大后小)判断
         * 长：1.<34 (33,32,31)  2.=34 =35...=45 同上
         */
        if (bigRoadListAll.size() < 44) {
            if (bigRoadListAll.size() == 43) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 42) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 41) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else
                bigRoadList.addAll(bigRoadListAll);
        } else if (bigRoadListAll.size() == 45) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 44) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(1, bigRoadListAll, bigRoadList);
        }
    }

    /**
     * 小路最多15列
     */
    private void calSmallRoad(List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadList) {
        /**
         * 根据原始数据列数判断
         * 短：1.<18 (17,16,15)  2.=18 =19...=45 倒叙(先大后小)判断
         * 长：1.<34 (33,32,31)  2.=34 =35...=45 同上
         */
        if (bigRoadListAll.size() < 30) {
            if (bigRoadListAll.size() == 29) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 28) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 27) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else
                bigRoadList.addAll(bigRoadListAll);
        } else if (bigRoadListAll.size() == 32) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 31) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 30) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(1, bigRoadListAll, bigRoadList);
        }
    }

    /**
     * 计算大路 最多30列
     * smallRoadListAll smallRoadList
     */
    private void calBigSmallCockroachRoad(List<List<Integer>> bigRoadListAll, List<List<Integer>> bigRoadList) {
        /**
         * 根据原始数据列数判断
         * 短：1.<18 (17,16,15)  2.=18 =19...=45 倒叙(先大后小)判断
         * 长：1.<34 (33,32,31)  2.=34 =35...=45 同上
         */
        if (bigRoadListAll.size() < 30) {
            if (bigRoadListAll.size() == 29) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 28) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else if (bigRoadListAll.size() == 27) {
                if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                    initBigRoadLine(8, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(7, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(6, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(5, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(4, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(3, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(2, bigRoadListAll, bigRoadList);
                } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(1, bigRoadListAll, bigRoadList);
                } else
                    bigRoadList.addAll(bigRoadListAll);
            } else
                bigRoadList.addAll(bigRoadListAll);
        } else if (bigRoadListAll.size() == 50) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(33, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(32, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(31, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(30, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(29, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(28, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 49) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(32, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(31, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(30, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(29, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(28, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 48) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(31, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(30, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(29, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(28, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 47) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(30, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(29, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(28, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 46) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(29, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(28, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 45) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(28, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 44) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(27, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 43) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(26, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 42) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(25, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 41) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(24, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 40) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(23, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 39) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(22, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 38) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(21, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 37) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(20, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 36) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(19, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 35) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 34) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 21 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 22) {
                initBigRoadLine(18, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(17, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 33) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(16, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 32) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(15, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 31) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(14, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
        } else if (bigRoadListAll.size() == 30) {
            if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 20 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 21) {
                initBigRoadLine(13, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 19 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 20) {
                initBigRoadLine(12, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 18 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 19) {
                initBigRoadLine(11, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 17 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 18) {
                initBigRoadLine(10, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 16 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(9, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 15 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(8, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 14 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(7, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 13 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(6, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 12 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(5, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 11 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(4, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 10 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(3, bigRoadListAll, bigRoadList);
            } else if (bigRoadListAll.get(bigRoadListAll.size() - 1).size() == 7 || bigRoadListAll.get(bigRoadListAll.size() - 2).size() == 8 || bigRoadListAll.get(bigRoadListAll.size() - 3).size() == 9 || bigRoadListAll.get(bigRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(2, bigRoadListAll, bigRoadList);
            } else
                initBigRoadLine(1, bigRoadListAll, bigRoadList);
        }
    }

    /**
     * 计算大眼列数 最多20列
     */
    private void calBigEyeLine(List<List<Integer>> bigEyeRoadListAll, List<List<Integer>> bigEyeRoadList, List<List<Integer>> bigEyeRoadListShort) {
        /**
         * 根据原始数据列数判断
         * 短：1.<36 (35,34,33)  2.=36 =37...=40 倒叙(先大后小)判断
         * 长：1.<68                             同上 不用转足够了
         */
        if (bigEyeRoadListAll.size() < 40) {
            if (bigEyeRoadListAll.size() == 39) {
                if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(3, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(2, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                    initBigRoadLine(1, bigEyeRoadListAll, bigEyeRoadListShort);
                } else
                    bigEyeRoadListShort.addAll(bigEyeRoadListAll);
            } else if (bigEyeRoadListAll.size() == 38) {
                if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 17 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(3, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(2, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                    initBigRoadLine(1, bigEyeRoadListAll, bigEyeRoadListShort);
                } else
                    bigEyeRoadListShort.addAll(bigEyeRoadListAll);
            } else if (bigEyeRoadListAll.size() == 37) {
                if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 17 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 18 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 19) {
                    initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 17 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 18) {
                    initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                    initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                    initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                    initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                    initBigRoadLine(3, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                    initBigRoadLine(2, bigEyeRoadListAll, bigEyeRoadListShort);
                } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                    initBigRoadLine(1, bigEyeRoadListAll, bigEyeRoadListShort);
                } else
                    bigEyeRoadListShort.addAll(bigEyeRoadListAll);
            } else
                bigEyeRoadListShort.addAll(bigEyeRoadListAll);
        } else if (bigEyeRoadListAll.size() == 49) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(18, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(17, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(16, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(15, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(14, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(13, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 48) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(17, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(16, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(15, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(14, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(13, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 47) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(16, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(15, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(14, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(13, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 46) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(15, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(14, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(13, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 45) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(14, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(13, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 44) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(13, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 43) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(12, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 42) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(11, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(3, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 41) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(10, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(3, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(2, bigEyeRoadListAll, bigEyeRoadListShort);
        } else if (bigEyeRoadListAll.size() == 40) {
            if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 16 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 17) {
                initBigRoadLine(9, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 15 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 16) {
                initBigRoadLine(8, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 14 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 15) {
                initBigRoadLine(7, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 13 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 14) {
                initBigRoadLine(6, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 12 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 13) {
                initBigRoadLine(5, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 11 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 12) {
                initBigRoadLine(4, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 10 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 11) {
                initBigRoadLine(3, bigEyeRoadListAll, bigEyeRoadListShort);
            } else if (bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 1).size() == 7 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 2).size() == 8 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 3).size() == 9 || bigEyeRoadListAll.get(bigEyeRoadListAll.size() - 4).size() == 10) {
                initBigRoadLine(2, bigEyeRoadListAll, bigEyeRoadListShort);
            } else
                initBigRoadLine(1, bigEyeRoadListAll, bigEyeRoadListShort);
        }

        if (bigEyeRoadListAll.size() < 68) {
            bigEyeRoadList.addAll(bigEyeRoadListAll);
        }
    }

    /**
     * 计算大眼列数 最多60列
     */
    private void calBigEyeLine(List<List<Integer>> bigEyeRoadListAll, List<List<Integer>> bigEyeRoadList) {
        bigEyeRoadList.addAll(bigEyeRoadListAll);
    }

    private void drawBigRoad(List<Integer> maxScoreList, List<List<Integer>> bigRoadList, YHZGridView gv_right_top, int bankColor, int playColor, int tieColor) {
        List<NodeImp> list2 = new ArrayList<>();
        list2.clear();
        //多个和的问题
        int tie_1 = 2;
        int tie_2 = 2;
        int tie_3 = 2;
        int tie_4 = 2;
        int tie_0 = 0;
        int b = -1;
        StringBuffer turnSb = new StringBuffer(); //保存 所有拐弯数据
        int m = -1;
        //0庄 1闲(只存庄和闲)  用于大眼路，小路和小强路分析       2和 3庄和 4庄和2  5闲和 6闲和2 7庄和3 8闲和3 9和2 10和3
        for (int i = 0; i < bigRoadList.size(); i++) {
            m++;
            int n = 0;//y轴
            int _m = m;//x轴
            int k = 1;//k++ k<7 添加前判断下一个有没有数据，有就从当前行直接拐弯，没有下一行
            for (int j = 0; j < bigRoadList.get(i).size(); j++) {//一个二维数组，有多少输出多少，跟n无关。
                if (i == 0 && bigRoadList.get(0).get(0) == 2) {
                    tie_0++;
                    if (j > 1 && bigRoadList.get(0).get(0) == 2 && bigRoadList.get(0).get(1) == 9 && bigRoadList.get(0).get(j) == 11) {
                        removeLateEle(list2);
                        list2.add(new SingleRingNode(0, 0, Color.TRANSPARENT, true, "" + tie_0));
                    } else if (j == 1 && bigRoadList.get(0).get(0) == 2 && bigRoadList.get(0).get(1) == 9) {
                        list2.add(new SingleRingNode(0, 0, Color.TRANSPARENT, true, "2"));
                    } else if (j == 0 && bigRoadList.get(0).get(0) == 2) {
                        list2.add(new SingleRingNode(0, 0, Color.TRANSPARENT, true, ""));
                    }
                } else if (k < 7) {
                    tie_0 = 0;
                    if (!turnSb.toString().contains(+m + "" + k)) {
                        if (n == 5) {
                            if (bigRoadList.get(i).get(j) == 0) {
                                list2.add(new SingleRingNode(m, n, Color.RED, false, ""));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            } else if (bigRoadList.get(i).get(j) == 1) {
                                list2.add(new SingleRingNode(m, n, Color.BLUE, false, ""));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            } else if (bigRoadList.get(i).get(j) == 3 || bigRoadList.get(i).get(j) == 5) {
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, ""));
                            } else if (bigRoadList.get(i).get(j) == 9) {
                                removeLateEle(list2);
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, "2"));
                            } else if (bigRoadList.get(i).get(j) == 10) {
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, "3"));
                            } else if (bigRoadList.get(i).get(j) == 11) {
                                removeLateEle(list2);
                                ++tie_1;
                                LogUtil.i("tie_1=" + tie_1);
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, String.valueOf(tie_1)));
                            }
                        } else if (n > 5) {
                            if (bigRoadList.get(i).get(j) == 0) {
                                ++_m;
                                list2.add(new SingleRingNode(_m, (k - 1), Color.RED, false, ""));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            } else if (bigRoadList.get(i).get(j) == 1) {
                                ++_m;
                                list2.add(new SingleRingNode(_m, (k - 1), Color.BLUE, false, ""));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            } else if (bigRoadList.get(i).get(j) == 3 || bigRoadList.get(i).get(j) == 5) {
                                list2.add(new SingleRingNode(_m, (k - 1), Color.TRANSPARENT, true, ""));
                            } else if (bigRoadList.get(i).get(j) == 9) {
                                removeLateEle(list2);
                                list2.add(new SingleRingNode(_m, (k - 1), Color.TRANSPARENT, true, "2"));
                            } else if (bigRoadList.get(i).get(j) == 10) {
                                list2.add(new SingleRingNode(_m, (k - 1), Color.TRANSPARENT, true, "3"));
                            } else if (bigRoadList.get(i).get(j) == 11) {
                                removeLateEle(list2);
                                ++tie_2;
                                LogUtil.i("tie_2=" + tie_2);
                                list2.add(new SingleRingNode(_m, (k - 1), Color.TRANSPARENT, true, String.valueOf(tie_2)));
                            }
                        } else {//n=0...4 k=1...5
                            if (bigRoadList.get(i).get(j) == 0) {
                                list2.add(new SingleRingNode(m, n, Color.RED, false, ""));
                                n++;
                                k++;
                            } else if (bigRoadList.get(i).get(j) == 1) {
                                list2.add(new SingleRingNode(m, n, Color.BLUE, false, ""));
                                n++;
                                k++;
                            } else if (bigRoadList.get(i).get(j) == 3 || bigRoadList.get(i).get(j) == 5) {
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, ""));
                            } else if (bigRoadList.get(i).get(j) == 9) {
                                removeLateEle(list2);
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, "2"));
                            } else if (bigRoadList.get(i).get(j) == 10) {
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, "3"));
                            } else if (bigRoadList.get(i).get(j) == 11) {
                                removeLateEle(list2);
                                ++tie_3;
                                LogUtil.i("tie_3=" + tie_3);
                                list2.add(new SingleRingNode(m, (n - 1), Color.TRANSPARENT, true, String.valueOf(tie_3)/*"4"*/));
                            }
                        }
                    } else {
                        if (bigRoadList.get(i).get(j) == 0) {
                            list2.add(new SingleRingNode(_m, (k - 1), Color.RED, false, ""));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        } else if (bigRoadList.get(i).get(j) == 1) {
                            list2.add(new SingleRingNode(_m, (k - 1), Color.BLUE, false, ""));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        } else if (bigRoadList.get(i).get(j) == 3 || bigRoadList.get(i).get(j) == 5) {
                            list2.add(new SingleRingNode(_m - 1, (k - 1), Color.TRANSPARENT, true, ""));
                        } else if (bigRoadList.get(i).get(j) == 9) {
                            removeLateEle(list2);
                            list2.add(new SingleRingNode(_m - 1, (k - 1), Color.TRANSPARENT, true, "2"));
                        } else if (bigRoadList.get(i).get(j) == 10) {
                            list2.add(new SingleRingNode(_m - 1, (k - 1), Color.TRANSPARENT, true, "3"));
                        } else if (bigRoadList.get(i).get(j) == 11) {
                            removeLateEle(list2);
                            ++tie_4;
                            LogUtil.i("tie_4=" + tie_4);
                            list2.add(new SingleRingNode(_m - 1, (k - 1), Color.TRANSPARENT, true, String.valueOf(tie_4)));
                        }
                    }
                }
            }
        }
        //0庄 1闲 2和 3庄和 4庄和2  5闲和 6闲和2 7庄和3 8闲和3 9和2 10和3
        gv_right_top.setNodeList(list2);
        gv_right_top.postInvalidate();
    }

    private void removeLateEle(List<NodeImp> list) {
        list.remove(list.size() - 1);
    }

    private void drawBigEye(List<List<Integer>> bigEyeRoadList, YHZGridView gv_right_middle, Context mContext, int bankColor, int playColor, int tieColor) {
        StringBuffer turnSb = new StringBuffer(); //保存 所有拐弯数据
        List<NodeImp> list3 = new ArrayList<>();
        list3.clear();
        int m = -1;
        //0庄 1闲(只存庄和闲)  用于大眼路，小路和小强路分析       2和 3庄和 4庄和2  5闲和 6闲和2 7庄和3 8闲和3 9和2 10和3
        for (int i = 0; i < bigEyeRoadList.size(); i++) {
            m++;
            int n = 0;//y轴
            int _m = m;//x轴
            int k = 1;//k++ k<7 添加前判断下一个有没有数据，有就从当前行直接拐弯，没有下一行
            for (int j = 0; j < bigEyeRoadList.get(i).size(); j++) {//一个二维数组，有多少输出多少，跟n无关。
                if (k < 7) {
                    if (!turnSb.toString().contains(+m + "" + k)) {
                        if (n == 5) {
                            if (bigEyeRoadList.get(i).get(j) == 0) {
                                list3.add(new FourRingsNode(m, n, Color.RED));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            } else if (bigEyeRoadList.get(i).get(j) == 1) {
                                list3.add(new FourRingsNode(m, n, Color.BLUE));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            }
                        } else if (n > 5) {
                            if (bigEyeRoadList.get(i).get(j) == 0) {
                                ++_m;
                                list3.add(new FourRingsNode(_m, (k - 1), Color.RED));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            } else if (bigEyeRoadList.get(i).get(j) == 1) {
                                ++_m;
                                list3.add(new FourRingsNode(_m, (k - 1), Color.BLUE));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            }
                        } else {
                            if (bigEyeRoadList.get(i).get(j) == 0) {
                                list3.add(new FourRingsNode(m, n, Color.RED));
                                n++;
                                k++;
                            } else if (bigEyeRoadList.get(i).get(j) == 1) {
                                list3.add(new FourRingsNode(m, n, Color.BLUE));
                                n++;
                                k++;
                            }
                        }
                    } else {
                        if (bigEyeRoadList.get(i).get(j) == 0) {
                            list3.add(new FourRingsNode(_m, (k - 1), Color.RED));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        } else if (bigEyeRoadList.get(i).get(j) == 1) {
                            list3.add(new FourRingsNode(_m, (k - 1), Color.BLUE));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        }
                    }
                }
            }
        }
        gv_right_middle.setNodeList(list3);
        gv_right_middle.postInvalidate();
    }

    private void drawSmallRoad(List<List<Integer>> smallRoadList, YHZGridView gv_right_bottom_1, Context mContext, int bankColor, int playColor, int tieColor) {
        StringBuffer turnSb = new StringBuffer(); //保存 所有拐弯数据
        List<NodeImp> list4 = new ArrayList<>();
        list4.clear();
        int m = -1;
        for (int i = 0; i < smallRoadList.size(); i++) {
            m++;
            int n = 0;//y轴
            int _m = m;//x轴
            int k = 1;//k++ k<7 添加前判断下一个有没有数据，有就从当前行直接拐弯，没有下一行
            for (int j = 0; j < smallRoadList.get(i).size(); j++) {//一个二维数组，有多少输出多少，跟n无关。
                if (k < 7) {
                    if (!turnSb.toString().contains(+m + "" + k)) {
                        if (n == 5) {
                            if (smallRoadList.get(i).get(j) == 0) {
                                list4.add(new FourCircleNode(m, n, Color.RED));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            } else if (smallRoadList.get(i).get(j) == 1) {
                                list4.add(new FourCircleNode(m, n, Color.BLUE));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            }
                        } else if (n > 5) {
                            if (smallRoadList.get(i).get(j) == 0) {
                                ++_m;
                                list4.add(new FourCircleNode(_m, (k - 1), Color.RED));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            } else if (smallRoadList.get(i).get(j) == 1) {
                                ++_m;
                                list4.add(new FourCircleNode(_m, (k - 1), Color.BLUE));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            }
                        } else {
                            if (smallRoadList.get(i).get(j) == 0) {
                                list4.add(new FourCircleNode(m, n, Color.RED));
                                n++;
                                k++;
                            } else if (smallRoadList.get(i).get(j) == 1) {
                                list4.add(new FourCircleNode(m, n, Color.BLUE));
                                n++;
                                k++;
                            }
                        }
                    } else {
                        if (smallRoadList.get(i).get(j) == 0) {
                            list4.add(new FourCircleNode(_m, (k - 1), Color.RED));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        } else if (smallRoadList.get(i).get(j) == 1) {
                            list4.add(new FourCircleNode(_m, (k - 1), Color.BLUE));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        }
                    }
                }
            }
        }
        gv_right_bottom_1.setNodeList(list4);
        gv_right_bottom_1.postInvalidate();
    }

    private void drawCockroachRoad(List<List<Integer>> cockroachRoadList, YHZGridView gv_right_bottom_2, Context mContext, int bankColor, int playColor, int tieColor) {
        StringBuffer turnSb = new StringBuffer(); //保存 所有拐弯数据
        List<NodeImp> list5 = new ArrayList<>();
        list5.clear();
        int m = -1;
        for (int i = 0; i < cockroachRoadList.size(); i++) {
            m++;
            int n = 0;//y轴
            int _m = m;//x轴
            int k = 1;//k++ k<7 添加前判断下一个有没有数据，有就从当前行直接拐弯，没有下一行
            for (int j = 0; j < cockroachRoadList.get(i).size(); j++) {//一个二维数组，有多少输出多少，跟n无关。
                if (k < 7) {
                    if (!turnSb.toString().contains(+m + "" + k)) {
                        if (n == 5) {
                            if (cockroachRoadList.get(i).get(j) == 0) {
                                list5.add(new FourLineNode(m, n, Color.RED));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            } else if (cockroachRoadList.get(i).get(j) == 1) {
                                list5.add(new FourLineNode(m, n, Color.BLUE));
                                n++;
                                turnSb.append(m + "" + (k - 1) + ",");
                            }
                        } else if (n > 5) {
                            if (cockroachRoadList.get(i).get(j) == 0) {
                                ++_m;
                                list5.add(new FourLineNode(_m, (k - 1), Color.RED));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            } else if (cockroachRoadList.get(i).get(j) == 1) {
                                ++_m;
                                list5.add(new FourLineNode(_m, (k - 1), Color.BLUE));
                                n++;
                                turnSb.append(_m + "" + (k - 1) + ",");
                            }
                        } else {
                            if (cockroachRoadList.get(i).get(j) == 0) {
                                list5.add(new FourLineNode(m, n, Color.RED));
                                n++;
                                k++;
                            } else if (cockroachRoadList.get(i).get(j) == 1) {
                                list5.add(new FourLineNode(m, n, Color.BLUE));
                                n++;
                                k++;
                            }
                        }
                    } else {
                        if (cockroachRoadList.get(i).get(j) == 0) {
                            list5.add(new FourLineNode(_m, (k - 1), Color.RED));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        } else if (cockroachRoadList.get(i).get(j) == 1) {
                            list5.add(new FourLineNode(_m, (k - 1), Color.BLUE));
                            n++;
                            turnSb.append(_m + "" + (k - 1) + ",");
                            _m++;
                        }
                    }
                }
            }
        }
        gv_right_bottom_2.setNodeList(list5);
        gv_right_bottom_2.postInvalidate();
    }

    /**
     * @param beadRoadList
     * @param bigRoadList
     * @param bigEyeRoadList
     * @param smallRoadList
     * @param cockroachRoadList
     * @param gv_left
     * @param gv_right_top
     * @param gv_right_middle
     * @param gv_right_bottom_1
     * @param gv_right_bottom_2
     * @param context
     * @param gameType          0百家乐 1龙虎
     */
    public void drawRoad(List<Integer> maxScoreList, List<Integer> beadRoadList, List<List<Integer>> bigRoadList, List<List<Integer>> bigEyeRoadList, List<List<Integer>> smallRoadList, List<List<Integer>> cockroachRoadList,
                         YHZGridView gv_left, YHZGridView gv_right_top, YHZGridView gv_right_middle, YHZGridView gv_right_bottom_1, YHZGridView gv_right_bottom_2, Context context, int gameType,
                         int bankColor, int playColor, int tieColor) {
        drawBeadRoad(beadRoadList, gv_left, context, gameType, bankColor, playColor, tieColor);
        drawBigRoad(maxScoreList, bigRoadList, gv_right_top, bankColor, playColor, tieColor);
        //drawBigRoad(List<Integer> maxScoreList, List<List<Integer>> bigRoadList, YHZGridView gv_right_top,  int bankColor, int playColor, int tieColor)
        drawBigEye(bigEyeRoadList, gv_right_middle, context, bankColor, playColor, tieColor);
        drawSmallRoad(smallRoadList, gv_right_bottom_1, context, bankColor, playColor, tieColor);
        drawCockroachRoad(cockroachRoadList, gv_right_bottom_2, context, bankColor, playColor, tieColor);
    }
}
