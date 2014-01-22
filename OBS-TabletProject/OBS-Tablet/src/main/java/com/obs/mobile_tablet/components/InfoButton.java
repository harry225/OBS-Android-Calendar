package com.obs.mobile_tablet.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.obs.mobile_tablet.R;

/**
 * Created by danielstensland on 12/23/13.
 */
public class InfoButton extends ImageButton {

    public String infoText;


    //assumes that the progress indicator should default to 0;
    public InfoButton(Context context) {
        super(context);
        init();
    }

    //assumes that the progress indicator should default to 0;
    public InfoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //assumes that the progress indicator should default to 0;
    public InfoButton (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
    }

    private void init () {
        this.setBackgroundColor(Color.parseColor("#CC0000"));
        this.setImageResource(R.drawable.info_button);
        this.setScaleType(ScaleType.FIT_START);
    }

}