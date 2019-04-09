package com.purity.yu.gameplatform.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.utils.MyYAnimation;


/**
 * 列表单选
 */

public class ChoiceItemLayout2 extends LinearLayout implements Checkable {

    private boolean mChecked;

    public ChoiceItemLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundResource(checked ? R.drawable.shape_border_golden_bg_gray2 : android.R.color.transparent);
    }
    public void anim(boolean mChecked,ImageView imageView) {
        final MyYAnimation animation = new MyYAnimation();
        animation.setRepeatCount(Animation.INFINITE);
        if(mChecked){
            imageView.startAnimation(animation);
        }else {
            imageView.clearAnimation();
        }
    }

    @Override
    public boolean isChecked() {
        return true;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
