package com.obs.mobile_tablet.registration;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

/**
 * Created by danielstensland on 12/20/13.
 */
public class RegistrationAlertBox extends LinearLayout {


    public static final int TYPE_WARNING = 1;
    public static final int TYPE_INFO = 2;
    public static final int TYPE_SUCCESS = 3;


    private TextView alertText;
    private ImageView alertIcon;
    private LinearLayout alertWrapper;

    public RegistrationAlertBox(Context context) {
        super(context);
        init(context);
    }

    public RegistrationAlertBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegistrationAlertBox (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        View.inflate(context, R.layout.registration_alert_box, this);
        alertText = (TextView) findViewById(R.id.alertText);
        alertIcon = (ImageView) findViewById(R.id.alertIcon);
        alertWrapper = (LinearLayout) findViewById(R.id.alert_box_wrapper);
        ViewGroup.LayoutParams layoutParams = alertWrapper.getLayoutParams();
        layoutParams.height = 0;
        alertWrapper.setLayoutParams(layoutParams);

        //INITIALIZE CUSTOM FONTS:
        alertText.setTypeface(FontFace.GetFace(context, FontFace.FONT_ALERT_BOX));
    }

    public void setText(String newText) {

        alertText.setText(newText);
    }

    public void showAlert(int type, String message) {
        //Logger.debug("showAlert: "+message);
        switch (type) {
            case TYPE_WARNING:
                showWarning(message);
                break;
            case TYPE_INFO:
                showInfo(message);
                break;
            case TYPE_SUCCESS:
                showSuccess(message);
                break;
        }
    }

    public void showWarning(String newText) {

        Logger.debug("showWarning: "+newText);
        alertWrapper.setBackgroundColor(Color.parseColor("#ff8d8d"));
        alertText.setText(newText);
        alertIcon.setImageResource(R.drawable.alert_error);
        expand();
    }

    public void showInfo(String newText) {
        //Logger.debug("showInfo: "+newText);
        alertWrapper.setBackgroundColor(Color.parseColor("#CCCCCC"));
        alertText.setText(newText);
        alertIcon.setImageResource(R.drawable.icon_info);
        expand();
    }

    public void showSuccess (String newText) {
        //Logger.debug("showSuccess: "+newText);
        alertWrapper.setBackgroundColor(Color.parseColor("#87ca7c"));
        alertText.setText(newText);
        alertIcon.setImageResource(R.drawable.icon_success);
        expand();
    }

    public void clearAlert () {
        collapse();
    }

    private void expand() {

        alertWrapper.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        alertWrapper.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, alertWrapper.getMeasuredHeight());
        mAnimator.start();
    }
    private void collapse() {
        int finalHeight = alertWrapper.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.start();
    }
    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = alertWrapper.getLayoutParams();
                layoutParams.height = value;
                alertWrapper.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}