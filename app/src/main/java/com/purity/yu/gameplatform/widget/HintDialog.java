package com.purity.yu.gameplatform.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.purity.yu.gameplatform.R;

/**
 * 提示对话框
 */
public class HintDialog extends Dialog {
    private TextView title;
    private TextView sure;
    private Context context;

    public HintDialog(Context context) {
        super(context, R.style.hintDialog);
        this.context=context;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_hint, null);  //通过LayoutInflater获取布局
        title = (TextView) view.findViewById(R.id.tv_title);
        sure = (TextView) view.findViewById(R.id.tv_sure);
        setContentView(view);  //设置view
    }

    /**
     * show之后设置属性
     * */
    public void setAttributes(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();//对话框属性
        lp.dimAmount = 0.6f;//所有弹窗的背景透明度都为0.6
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        lp.width = (int) (d.getWidth() * 0.4); // 宽度设置为屏幕的0.8倍
        lp.height = (int) (d.getHeight() * 0.4);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//一定加上这句才起效果
        setCanceledOnTouchOutside(true);//false 对话框外部失去焦点
    }

    public void setAttributes(float width,float height){
        WindowManager.LayoutParams lp = getWindow().getAttributes();//对话框属性
        lp.dimAmount = 0.6f;//所有弹窗的背景透明度都为0.6
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        lp.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.8倍
        lp.height = (int) (d.getHeight() * height);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//一定加上这句才起效果
        setCanceledOnTouchOutside(true);//false 对话框外部失去焦点
    }

    //设置内容  
    public void setContent(String content) {
        title.setText(content);
    }

    public void setOnPositiveListener(View.OnClickListener listener) {
        sure.setOnClickListener(listener);
    }

}