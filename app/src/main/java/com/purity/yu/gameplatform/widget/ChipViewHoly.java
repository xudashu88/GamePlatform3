package com.purity.yu.gameplatform.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.purity.yu.gameplatform.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanghaozhang on 2019/1/4.
 */
public class ChipViewHoly extends RelativeLayout {

    private static final String TAG = "ChipViewHoly";

    private int[] chipCategory = new int[]{10, 20, 50, 100, 200, 500, 1000, 5000, 10000, 50000};

    private int chipNum = 0;

    private List<Integer> chipNumList = new ArrayList<>();
    private int length;

    public ChipViewHoly(Context context) {
        super(context);
    }

    public ChipViewHoly(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChipViewHoly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addChip(Integer i) {
        if (i > 0) {
            chipNum += i;
            calculationAndChange();
        } else {//0 清除数据
            chipNum = 0;
            chipNumList.clear();
            calculationAndChange();
        }
    }

    private void calculationAndChange() {
        chipNumList.clear();
        int chipNumTemp = chipNum;
        for (int i = chipCategory.length - 1; i >= 0; i--) {
            int chipType = chipCategory[i];
            while (chipNumTemp >= chipType) {
                chipNumTemp -= chipType;
                chipNumList.add(chipType);
            }
        }

        length = chipNumList.size();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView child = (ImageView) getChildAt(i);
            if (length <= i) {
                for (int j = i; j < childCount; j++) {
                    ImageView c = (ImageView) getChildAt(i);
                    removeView(c);
                }
                break;
            } else {
                child.setImageResource(getDrawableRes(chipNumList.get(i)));
            }
        }

        for (int i = 0; i < length - childCount; i++) {
            addView(getDrawableRes(chipNumList.get(childCount + i)));
        }
        requestLayout();
    }

    public int getChipNumSize() {
        return length;
    }

    public void addView(@DrawableRes int drawableRes) {
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(drawableRes);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 60);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int dimens = getResources().getDimensionPixelSize(R.dimen.unit2);
        lp.setMargins(0, 0, 0, dimens * getChildCount());
        addView(iv, lp);
    }

    private int getDrawableRes(int i) {
        switch (i) {
            case 10:
                return R.drawable.new_chip_10;
            case 20:
                return R.drawable.new_chip_20;
            case 50:
                return R.drawable.new_chip_50;
            case 100:
                return R.drawable.new_chip_100;
            case 200:
                return R.drawable.new_chip_200;
            case 500:
                return R.drawable.new_chip_500;
            case 1000:
                return R.drawable.new_chip_1000;
            case 5000:
                return R.drawable.new_chip_5000;
            case 10000:
                return R.drawable.new_chip_10000;
            case 50000:
                return R.drawable.new_chip_50000;
            default:
                return R.drawable.new_chip_10;
        }
    }
}
