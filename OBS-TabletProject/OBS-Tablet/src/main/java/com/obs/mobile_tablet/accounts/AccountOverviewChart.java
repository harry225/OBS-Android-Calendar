package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.utils.Logger;

/**
 * Created by jaimiespetseris on 1/15/14.
 */
public class AccountOverviewChart extends AccountOverviewChartBase {


    //assumes that the progress indicator should default to 0;
    public AccountOverviewChart(Context context) {
        super(context);
    }

    //assumes that the progress indicator should default to 0;
    public AccountOverviewChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //assumes that the progress indicator should default to 0;
    public AccountOverviewChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    protected void setup(Context context) {
        LayoutInflater.from(context).inflate(R.layout.accounts_overview_chart, this, true);
        super.setup(context);
    }
}
