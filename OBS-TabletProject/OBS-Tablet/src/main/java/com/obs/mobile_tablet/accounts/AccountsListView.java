package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.obs.mobile_tablet.components.PullToRefreshListView;

/**
 * Created by jaimiespetseris on 1/13/14.
 */
public class AccountsListView extends PullToRefreshListView {

    public Boolean disableScroll = false;

    public AccountsListView(Context context) {
        //If built programmatically
        super(context);
    }

    public AccountsListView(Context context, AttributeSet attrs) {
        //This example uses this method since being built from XML
        super(context, attrs);
    }

    public AccountsListView(Context context, AttributeSet attrs, int defStyle) {
        //Build from XML layout
        super(context, attrs, defStyle);
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(ev.getAction()==MotionEvent.ACTION_MOVE)
            return true;
        return super.dispatchTouchEvent(ev);
    }
    */


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(disableScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
