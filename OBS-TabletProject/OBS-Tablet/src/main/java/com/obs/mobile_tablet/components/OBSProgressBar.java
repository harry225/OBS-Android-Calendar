package com.obs.mobile_tablet.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ProgressAnimation.ProgressAnimation;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

/**
 * Created by jaimiespetseris on 12/12/13.
 */
public class OBSProgressBar extends RelativeLayout {

    private int nextFontColor = 0x00000000;
    private int currentFontColor = 0x00000000;
    private int pastFontColor = 0x00000000;
    private float fontSize;

    private RelativeLayout container;
    private LinearLayout textviewHolder;
    private ProgressBar progressBar;
    private Context myContext;

    private ProgressAnimation anim;

    private int maxWidth;
    private int currentIndex;
    private int totalNumItems = 0;
    private ArrayList<TextView> progressTextItems = new ArrayList<TextView>();
    private ArrayList<Integer> endpoints = new ArrayList<Integer>();

    //assumes that the progress indicator should default to 0;
    public OBSProgressBar(Context context) {
        super(context);
        setup(context);
    }

    //assumes that the progress indicator should default to 0;
    public OBSProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    //assumes that the progress indicator should default to 0;
    public OBSProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
    }

    private void setup(Context context) {

        //set up all view references:
        myContext = context;
        LayoutInflater.from(context).inflate(R.layout.progress_bar, this, true);
        container = (RelativeLayout)findViewById(R.id.obs_progress_container);
        progressBar = (ProgressBar)findViewById(R.id.android_progress_bar);
        textviewHolder = (LinearLayout)findViewById(R.id.textview_holder);

        //set up all colors:
        nextFontColor = getResources().getColor(R.color.obsDarkGray);
        currentFontColor = 0xFF000000;
        pastFontColor = 0xFF000000;
        fontSize = getResources().getDimension(R.dimen.progress_bar_font_size);

        final ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (vto.isAlive()) {
                    vto.removeGlobalOnLayoutListener(this);
                }
                maxWidth  = container.getMeasuredWidth();
                //int height = rl.getMeasuredHeight();
                progressBar.setMax(maxWidth);
                //progressBar.setProgress(0);
                //Logger.debug("maxwidth: "+maxWidth);
            }
        });

        anim = new ProgressAnimation(progressBar, 0, 0); //set it up here to use it later
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(10);
        progressBar.startAnimation(anim);

        currentIndex = 0;
    }

    public void initItems(ArrayList<String> items) {
        clearAllItems();
        totalNumItems = items.size();
        if (totalNumItems > 0) {
            for (int i=0; i<totalNumItems; i++) {
                int startX = i*maxWidth/totalNumItems;
                int endX = (i+1)*maxWidth/totalNumItems;
                addItem(items.get(i), i, startX, endX);
            }

            int progressDest = endpoints.get(0);
            anim.ChangeInfo(0, progressDest);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(500);
            progressBar.startAnimation(anim);
        }
    }

    private void clearAllItems() {
        //Logger.debug("OBSProgressBar :: clearAllItems");
        if(textviewHolder.getChildCount() > 0) {
            textviewHolder.removeAllViews();
        }
        progressTextItems.clear();

        endpoints.clear();

        //anim.ChangeInfo(0, 0); //set it up here to use it later
        //anim.setInterpolator(new LinearInterpolator());
        //anim.setDuration(10);
        //progressBar.startAnimation(anim);
    }

    private int addItem(String itemName, int index, int startX, int endX) {
        //Logger.debug("OBSProgressBar :: addItem "+itemName);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(endX - startX, LayoutParams.MATCH_PARENT, 1f);

        ImageView highlight = new ImageView(myContext);
        //ImageView highlight = new ImageView(new ContextThemeWrapper(myContext, R.style.obsProgressButton_C));
        highlight.setLayoutParams(lParams);
        highlight.setFocusable(false);
        highlight.setClickable(false);
        highlight.setVisibility(View.INVISIBLE);

        TextView label = new TextView(myContext);
        label.setLayoutParams(lParams);
        //label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
        if (index == 0) {
            label.setBackgroundResource(R.drawable.progress_item_bg_first);
        }
        else if (index + 1 == totalNumItems) {
            label.setBackgroundResource(R.drawable.progress_item_bg_last);
        }
        else {
            label.setBackgroundResource(R.drawable.progress_item_bg);
        }
        label.setText(itemName);
        label.setSingleLine(true);
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        label.setGravity(Gravity.CENTER);
        label.setFocusable(false);
        label.setClickable(false);
        label.setId(1);
        /*if (index > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) label.getLayoutParams();
            params.setMargins(-1, 0, 0, 0); //substitute parameters for left, top, right, bottom
            label.setLayoutParams(params);
        }*/

        progressTextItems.add(label);
        textviewHolder.addView(label);

        endpoints.add(endX);

        updateUI();

        return progressTextItems.size();
    }

    //assumes that you only move in increments of 1. does not do any error checking:
    public void update(int position) {
        //Logger.debug("OBSProgressBar :: update = "+position);
        currentIndex = position;
        updateUI();
    }

    public int size() {
        return progressTextItems.size();
    }

    private void updateUI() {
        //Logger.debug("OBSProgressBar :: updateUI");
        int lastIndex = size() - 1;
        for (int i=0; i<=lastIndex; i++) {
            TextView label = progressTextItems.get(i);

            if (i < currentIndex) {
                label.setTextColor(pastFontColor);
            }
            else if (i == currentIndex) {
                int progressDest = endpoints.get(i); //highlight.getRight();
                int progressCurrent = progressBar.getProgress();
                Logger.debug("dest progress: "+progressDest);
                if (progressCurrent < progressDest) {
                    anim.ChangeInfo(progressCurrent, progressDest);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setDuration(500);
                    progressBar.startAnimation(anim);
                }
                label.setTextColor(currentFontColor);
            }
            else {
                label.setTextColor(nextFontColor);
            }
        }
    }
}
