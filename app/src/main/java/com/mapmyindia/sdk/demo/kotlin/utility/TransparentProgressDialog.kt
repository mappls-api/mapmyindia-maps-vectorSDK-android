package com.mapmyindia.sdk.demo.kotlin.kotlin.utility

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mapmyindia.sdk.demo.R

/**
 * Created by CEINFO on 26-02-2019.
 */

class TransparentProgressDialog(context: Context, resourceIdOfImage: Int, message: String) : Dialog(context, R.style.TransparentProgressDialog) {

    private val iv: ImageView
    private val tv: TextView


    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        //setTitle(message);
        setCancelable(false)
        setOnCancelListener(null)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        iv = ImageView(context)
        tv = TextView(context)
        tv.setPadding(0, 20, 0, 0)
        tv.textSize = 25f
        iv.setImageResource(resourceIdOfImage)
        tv.text = message

        layout.addView(iv, params)
        layout.addView(tv, params)
        addContentView(layout, params)
    }

    override fun show() {
        super.show()
        val anim = RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        anim.duration = 3000
        iv.animation = anim
        iv.startAnimation(anim)
    }
}