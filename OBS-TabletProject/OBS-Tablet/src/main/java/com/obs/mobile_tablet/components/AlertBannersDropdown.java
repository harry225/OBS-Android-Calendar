package com.obs.mobile_tablet.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.accounts.BellItem;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

/**
 * Created by jaimiespetseris on 1/20/14.
 */
public class AlertBannersDropdown extends LinearLayout {

    private Context myContext;
    private LinearLayout alertLayout;
    private ListView alertList;
    private TextView dismiss;

    private Boolean isOpen = false;

    public static int TWEEN_DURATION = 100;

    public AlertBannersDropdown(Context context) {
        super(context);
        setup(context);
    }

    public AlertBannersDropdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public AlertBannersDropdown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
    }

    public void setData(ArrayList<BellItem> items) {
        BulletinAdapter bellAlertAdapter = new BulletinAdapter(myContext, items);
        alertList.setAdapter(bellAlertAdapter);
    }

    public void toggleMe() {
        if (isOpen) {
            hideMe(TWEEN_DURATION);
        }
        else {
            showMe(TWEEN_DURATION);
        }
    }

    public void showMe(int duration) {
        Logger.debug("showMe");
        ScaleAnimation anim = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, (float)0.0, Animation.RELATIVE_TO_SELF, (float)0.0);
        anim.setDuration(duration);
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                alertLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alertLayout.startAnimation(anim);

        isOpen = true;
    }

    public void hideMe(int duration) {
        Logger.debug("hideMe");
        ScaleAnimation anim = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_SELF, (float)0.0, Animation.RELATIVE_TO_SELF, (float)0.0);
        anim.setDuration(duration);
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                alertLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alertLayout.startAnimation(anim);

        isOpen = false;
    }

    private void setup(Context context) {
        LayoutInflater.from(context).inflate(R.layout.alert_banners_dropdown, this, true);
        myContext = context;
        alertLayout = (LinearLayout) findViewById(R.id.alert_holder);
        alertList = (ListView) findViewById(R.id.alerts_list);
        dismiss = (TextView) findViewById(R.id.dismissText);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMe();
            }
        });

        alertLayout.setVisibility(View.INVISIBLE);
        isOpen = false;
    }





    public class BulletinAdapter extends ArrayAdapter {

        private ArrayList<BellItem> bell_items;
        private Context mContext;

        public BulletinAdapter(Context context,ArrayList<BellItem> _bell_items) {
            super(context, R.layout.accounts_fragment, _bell_items);
            mContext = context;
            bell_items = _bell_items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bell_alert_box, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.alert_text);
            textView.setText(bell_items.get(position).alertMessage);

            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.background_alert_box);
            relativeLayout.setBackgroundColor(bell_items.get(position).boxColor);

            return  convertView;
        }
    }

}
