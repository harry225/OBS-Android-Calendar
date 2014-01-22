package com.obs.mobile_tablet.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by danielstensland on 1/6/14.
 */
public class OBSListView extends ListView {

    //If built programmatically
    public OBSListView(Context context)
    {
        super(context);
        init();
    }
    //This example uses this method since being built from XML
    public OBSListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    //Build from XML layout
    public OBSListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public void init()
    {

    }

}

