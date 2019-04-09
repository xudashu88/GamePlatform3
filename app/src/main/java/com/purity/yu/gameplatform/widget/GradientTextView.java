package com.purity.yu.gameplatform.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

//https://blog.csdn.net/u013278099/article/details/50881431 渐变
public class GradientTextView extends AppCompatTextView {
    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
//            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(), StringToColor("#FFFFFF"), StringToColor("#0e9aeb"), Shader.TileMode.CLAMP));
//            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
//                    new int[]{0xFFFFEABA, 0xFFDFBB82, 0xFFBE8B49}, new float[]{0, 0.5f, 1},
//                    Shader.TileMode.CLAMP));
            Shader shader_horizontal = new LinearGradient(getWidth() / 4, 0, getWidth(), 0, StringToColor("#FFFFFF"), Color.BLUE, Shader.TileMode.CLAMP);
            getPaint().setShader(shader_horizontal);

            //4ef1ff A2EFF7
        }
    }

    /**
     * #颜色转16进制颜色
     *
     * @param str {String} 颜色
     * @return
     */
    private int StringToColor(String str) {
        return 0xff000000 | Integer.parseInt(str.substring(2), 16);
    }
}
