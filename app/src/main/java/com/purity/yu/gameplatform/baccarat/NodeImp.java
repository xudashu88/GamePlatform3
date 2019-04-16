package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by yanghaozhang on 2018/7/3.
 */
public interface NodeImp {

    void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom);

    int getX();

    int getY();

    int getColor();

    boolean isSpecialNode(List<NodeImp> nodeList);

    void drawSpecial(Canvas canvas, Paint paint, float left, float top, float right, float bottom);
}
