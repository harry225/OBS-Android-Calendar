package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.GraphData;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by jaimiespetseris on 1/9/14.
 */
public class AccountOverviewChartBase extends LinearLayout {

    protected Context myContext;
    protected LinearLayout container;
    protected GraphicalView mChartView;
    protected TextView noDataText;
    protected ListView legendListView;
    protected FrameLayout chartHolder;
    protected TextView title;
    protected TextView legendTitle;

    protected GraphObj graphObj = new GraphObj();
    protected String[] graphColors;


    public AccountOverviewChartBase(Context context) {
        super(context);
        setup(context);
        refreshGraph();
    }

    public AccountOverviewChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public AccountOverviewChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public void setWidth(int width) {
        //Logger.debug("chart width is: "+width);
        container = (LinearLayout) findViewById(R.id.container);
        LayoutParams lp = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(lp);
    }

    public void setData(GraphObj newGraphObj) {
        //Logger.debug("AccountOverviewChartBase :: setData "); // + newGraphObj.displayText + "," + newGraphObj.type);
        graphObj = newGraphObj;
        /*for (GraphData dataPoint : graphObj.dataPoints) {
            Logger.debug("====="+dataPoint.displayText+", "+dataPoint.type + ", " + dataPoint.date + ", "+ dataPoint.value);
        }*/
        refreshGraph();
    }

    public void repaint() {
        mChartView.repaint();
    }

    protected void setup(Context context) {
        myContext = context;

        chartHolder = (FrameLayout) findViewById(R.id.content_holder_pie);
        title = (TextView) findViewById(R.id.title);
        legendTitle = (TextView) findViewById(R.id.legend_title);
        legendListView = (ListView) findViewById(R.id.vertical_legend);
        noDataText = (TextView) findViewById(R.id.no_data_text);

        //font stuff:
        title.setTypeface(FontFace.GetFace(myContext, FontFace.FONT_PRIMARY_BUTTON));
        legendTitle.setTypeface(FontFace.GetFace(myContext, FontFace.FONT_TABLE_DATA));
        noDataText.setTypeface(FontFace.GetFace(myContext, FontFace.FONT_PRIMARY_BUTTON));
    }

    protected void refreshGraph() {
        if (graphObj.dataPoints == null || graphObj.dataPoints.size() == 0) {
            createNoDataGraph();
        }
        else {
            createGraph();
        }
    }

    protected void createNoDataGraph() {
        //Logger.debug("AccountOverviewChartBase :: createNoDataGraph");

        //set up dummy data:
        graphObj.type = "PIE";
        if (graphObj.displayText == null) {
            graphObj.displayText = "";
        }

        GraphData noData = new GraphData();
        noData.displayText = "";
        noData.value = new BigDecimal(100);
        graphObj.dataPoints = new ArrayList<GraphData>();
        graphObj.dataPoints.add(noData);

        //set up colors, etc:
        noDataText.setVisibility(View.VISIBLE);
        graphColors = myContext.getResources().getStringArray(R.array.nodatachartcolors);

        //draw the graph:
        renderGraph(false);
    }

    protected void createGraph() {
        //Logger.debug("AccountOverviewChartBase :: createGraph");

        //set up colors, etc:
        noDataText.setVisibility(View.INVISIBLE);
        graphColors = myContext.getResources().getStringArray(R.array.piechartcolors);

        //draw the graph:
        renderGraph(true);
    }

    protected void renderGraph(Boolean showLegend) {
        //put values into arrays for creating the graph:
        ArrayList<GraphData> dataPoints = (ArrayList) graphObj.dataPoints;
        int numDataPoints = dataPoints.size();
        double[] values = new double[numDataPoints];
        String[] categoryNames = new String[numDataPoints];
        String[] percentValues = new String[numDataPoints];
        for (int i = 0; i < numDataPoints; ++i) {
            values[i] = dataPoints.get(i).value.doubleValue()/100;
            categoryNames[i] = dataPoints.get(i).displayText;
            percentValues[i] = dataPoints.get(i).value.intValue() + "%";
        }


        title.setText(graphObj.displayText.toUpperCase());
        legendTitle.setText("Total Accounts: "+numDataPoints);


        final DefaultRenderer mRenderer = new DefaultRenderer();
        final CategorySeries mSeries = new CategorySeries("");
        mRenderer.setStartAngle(180);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setZoomEnabled(false);
        mRenderer.setPanEnabled(false);
        mRenderer.setAntialiasing(true);
        mRenderer.setShowLabels(false);
        mRenderer.setShowLegend(false);
        mRenderer.setClickEnabled(true);


        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(0);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < dataPoints.size(); ++i) {
            list.add(categoryNames[i] + " " + values[i]);

            mSeries.add(categoryNames[i], values[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(Color.parseColor(graphColors[i]));
            mRenderer.addSeriesRenderer(renderer);
            //renderer.setDisplayChartValues(false);
            //renderer.setChartValuesTextSize(24);
            renderer.setChartValuesFormat(percentFormat);
        }

        mRenderer.setDisplayValues(true);

        if (mChartView != null) {
            chartHolder.removeView(mChartView);
        }
        mChartView =  ChartFactory.getPieChartView(myContext, mSeries, mRenderer);
        chartHolder.addView(mChartView);
        mChartView.repaint();

        final LegendAdapter adapter;
        if (showLegend) {
            adapter = new LegendAdapter(myContext, categoryNames, percentValues);
        }
        else { //clear the legend listview:
            adapter = new LegendAdapter(myContext, new String[0], new String[0]);
        }
        legendListView.setAdapter(adapter);

        mChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {
                    for (int i = 0; i < mSeries.getItemCount(); i++) {
                        legendListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        if (mRenderer.getSeriesRendererAt(i).isHighlighted()) {
                            mRenderer.getSeriesRendererAt(i).setHighlighted(false);
                            mChartView.repaint();
                            i = mSeries.getItemCount();
                        } else {
                            if (i == seriesSelection.getPointIndex()){
                                mRenderer.getSeriesRendererAt(i).setHighlighted(true);
                                legendListView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.obsHighlight));
                                mChartView.repaint();
                            }
                        }
                    }
                }
            }
        });


        legendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int iDeselect = -1;
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                int viewcount = parent.getChildCount();
                for (int i = 0; i < viewcount; i++) {
                    View resetviews = parent.getChildAt(i);
                    resetviews.setBackgroundColor(Color.TRANSPARENT);
                    mRenderer.getSeriesRendererAt(i).setHighlighted(false);
                    mChartView.repaint();

                }
                if (iDeselect != position){
                    view.setBackgroundColor(getResources().getColor(R.color.obsHighlight));
                    mRenderer.getSeriesRendererAt(position).setHighlighted(true);
                    mChartView.repaint();
                    iDeselect = position;
                }else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                    iDeselect = -1;
                }
            }

        });
    }



    protected class LegendAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] names;
        private final String[] values;


        public LegendAdapter(Context context, String[] names, String[] values) {
            super(context, R.layout.account_overview_legend_row_item, names);
            this.context = context;
            this.names = names;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.account_overview_legend_row_item, parent, false);
            TextView Icon = (TextView) rowView.findViewById(R.id.icon);
            TextView AccountInfo = (TextView) rowView.findViewById(R.id.item_name);
            TextView AccountValue = (TextView) rowView.findViewById(R.id.item_value);
            RelativeLayout row = (RelativeLayout) rowView.findViewById(R.id.highlightrow);
            AccountInfo.setText(names[position]);
            AccountValue.setText(values[position]);
            String[] graphColors = context.getResources().getStringArray(R.array.piechartcolors);
            Icon.setBackgroundColor(Color.parseColor(graphColors[position]));
            int alternator = position++;
            if (alternator % 2 == 0){
                row.setBackgroundColor(Color.WHITE);
            }
            else {
                row.setBackgroundColor(context.getResources().getColor(R.color.obsLightGray));
            }
            // Change the icon for Windows and iPhone

            //set custom fonts:
            AccountInfo.setTypeface(FontFace.GetFace(context, FontFace.FONT_TABLE_DATA));
            AccountValue.setTypeface(FontFace.GetFace(context, FontFace.FONT_TABLE_DATA));

            return rowView;
        }
    }
}
