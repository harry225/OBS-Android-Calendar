package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.GraphData;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.utils.Logger;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by jaimiespetseris on 1/14/14.
 */
public class AccountsPercentTransactionsChart extends LinearLayout {

    private Context myContext;
    private FrameLayout chartHolder;
    private TextView noDataText;
    private GraphicalView mChartView;

    private GraphObj graphObj = new GraphObj();
    private String[] graphColors;


    //assumes that the progress indicator should default to 0;
    public AccountsPercentTransactionsChart(Context context) {
        super(context);
        setup(context);
        refreshGraph();
    }

    //assumes that the progress indicator should default to 0;
    public AccountsPercentTransactionsChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    //assumes that the progress indicator should default to 0;
    public AccountsPercentTransactionsChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public void setData(GraphObj newGraphObj) {
        //Logger.debug("AccountsPercentTransactionsChart :: setData " + newGraphObj.dataPoints.size()); // + newGraphObj.displayText + "," + newGraphObj.type);
        graphObj = newGraphObj;
        refreshGraph();
        //for (GraphData dataPoint : graphObj.dataPoints) {
        //  Logger.debug(dataPoint.displayText+", "+dataPoint.type + ", " + dataPoint.date + ", "+ dataPoint.value);
        //}
    }

    public void repaint() {
        mChartView.repaint();
    }

    private void setup(Context context) {
        Logger.debug("AccountsPercentTransactionsChart :: setup");
        LayoutInflater.from(context).inflate(R.layout.accounts_percent_transactions_chart, this, true);
        myContext = context;

        chartHolder = (FrameLayout) findViewById(R.id.content_holder_pie);
        noDataText = (TextView) findViewById(R.id.no_data_text);
        noDataText.setVisibility(View.INVISIBLE);
    }

    private void refreshGraph() {
        Logger.debug("AccountsPercentTransactionsChart :: refreshGraph");
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
        //noDataText.setVisibility(View.VISIBLE);
        graphColors = myContext.getResources().getStringArray(R.array.nodatachartcolors);

        //draw the graph:
        renderGraph(false);
    }

    protected void createGraph() {
        //Logger.debug("AccountOverviewChartBase :: createGraph");

        //set up colors, etc:
        //noDataText.setVisibility(View.INVISIBLE);
        graphColors = myContext.getResources().getStringArray(R.array.piechartcolors);

        //draw the graph:
        renderGraph(true);
    }

    private void renderGraph(Boolean showLegend) {

        //put values into a pair of arrays for creating the graph:
        ArrayList<GraphData> dataPoints = (ArrayList) graphObj.dataPoints;
        int numDataPoints = dataPoints.size();
        double[] values = new double[numDataPoints];
        String[] categoryNames = new String[numDataPoints];
        for (int i = 0; i < numDataPoints; ++i) {
            values[i] = dataPoints.get(i).value.doubleValue()/100;
            categoryNames[i] = dataPoints.get(i).displayText;
        }

        final DefaultRenderer mRenderer = new DefaultRenderer();
        final CategorySeries mSeries = new CategorySeries("");
        mRenderer.setStartAngle(180);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setZoomEnabled(false);
        mRenderer.setPanEnabled(false);
        mRenderer.setShowLabels(false);
        mRenderer.setShowLegend(false);
        mRenderer.setClickEnabled(true);
        mRenderer.setAntialiasing(true);
        mRenderer.setScale(1.3f);
        mRenderer.setLabelsColor(Color.WHITE);
        mRenderer.setLabelsTextSize(18);
        mRenderer.setScale(1.2f);
        mRenderer.setMargins(new int[] {0,0,0,0});
        mRenderer.setTextTypeface("sans_serif_bold", Typeface.BOLD);
        mRenderer.setLabelsColor(Color.parseColor("#FFFFFF"));


        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(0);


        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < dataPoints.size(); ++i) {
            String text = categoryNames[i] + " " + values[i];
            list.add(text);
            mSeries.add(categoryNames[i], values[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(Color.parseColor(graphColors[i]));
            mRenderer.addSeriesRenderer(renderer);
            //renderer.setDisplayChartValues(false);
            renderer.setChartValuesFormat(percentFormat);
        }

        mRenderer.setDisplayValues(true);

        if (mChartView != null) {
            chartHolder.removeView(mChartView);
        }
        mChartView =  ChartFactory.getPieChartView(myContext, mSeries, mRenderer);
        chartHolder.addView(mChartView);
        mChartView.repaint();

        mChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.debug("pie chart click");
            }
        });

    }
}
