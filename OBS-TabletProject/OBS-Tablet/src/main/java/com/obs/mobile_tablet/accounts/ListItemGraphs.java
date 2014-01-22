package com.obs.mobile_tablet.accounts;

import com.obs.mobile_tablet.datamodel.reporting.GraphObj;

import java.util.ArrayList;

/**
 * Created by jaimiespetseris on 1/9/14.
 */
public class ListItemGraphs {

    public String name;
    public ArrayList<GraphObj> graphs = new ArrayList<GraphObj>();

    public ListItemGraphs(String newName) {
        name = newName;
        //myChart = new AccountOverviewChartBase();
    }
}
