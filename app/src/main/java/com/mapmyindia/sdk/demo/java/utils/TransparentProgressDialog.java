package com.mapmyindia.sdk.demo.java.utils;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapmyindia.sdk.demo.R;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class TransparentProgressDialog extends Dialog {

    private ImageView iv;
    private TextView tv;


    public TransparentProgressDialog(Context context, int resourceIdOfImage, String message) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        //setTitle(message);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        iv = new ImageView(context);
        tv = new TextView(context);
        tv.setPadding(0, 20, 0, 0);
        tv.setTextSize(25);
        iv.setImageResource(resourceIdOfImage);
        tv.setText(message);

        layout.addView(iv, params);
        layout.addView(tv, params);
        addContentView(layout, params);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);
        iv.setAnimation(anim);
        iv.startAnimation(anim);
    }
}