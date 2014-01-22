package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.GraphData;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.utils.Logger;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import android.graphics.Paint.Align;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jaimiespetseris on 1/9/14.
 */
public class AccountsTransactionBarChart extends LinearLayout {

    private Context myContext;
    private GraphicalView mBarView = null;
    private LinearLayout chartHolder;
    private TextView noDataText;

    private GraphObj graphObj = new GraphObj();
    private String[] graphColors;

    //assumes that the progress indicator should default to 0;
    public AccountsTransactionBarChart(Context context) {
        super(context);
        setup(context);
        refreshGraph();
    }

    //assumes that the progress indicator should default to 0;
    public AccountsTransactionBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    //assumes that the progress indicator should default to 0;
    public AccountsTransactionBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public void setData(GraphObj newGraphObj) {
        //Logger.debug("AccountsTransactionBarChart :: setData " + newGraphObj.dataPoints.size()); // + newGraphObj.displayText + "," + newGraphObj.type);
        graphObj = newGraphObj;

        Logger.debug("graphObj: "+graphObj.type+", "+graphObj.displayText);
        for (GraphData dataPoint : graphObj.dataPoints) {
            Logger.debug("==="+dataPoint.displayText+", "+dataPoint.type + ", " + dataPoint.date + ", "+ dataPoint.value);
        }
        refreshGraph();
    }

    public void repaint() {
        mBarView.repaint();
    }

    private void setup(Context context) {
        LayoutInflater.from(context).inflate(R.layout.accounts_transactions_bar_chart, this, true);
        myContext = context;
        chartHolder = (LinearLayout) findViewById(R.id.bar_chart);
    }

    private void refreshGraph() {
        if (graphObj.dataPoints == null || graphObj.dataPoints.size() == 0) {
            Logger.debug("creating dummy data");
            createNoDataGraph();
        }
        else {
            Logger.debug("using real data");
            createGraph();
        }
    }

    private void createNoDataGraph() {
        //Logger.debug("setting up a dummy graph");
        //set up dummy data:
        graphObj.type = "BAR";
        if (graphObj.displayText == null) {
            graphObj.displayText = "";
        }

        graphObj.dataPoints = new ArrayList<GraphData>();
        GraphData noData = new GraphData();
        noData.type = "";
        noData.value = new BigDecimal(8000);
        graphObj.dataPoints.add(noData);
        GraphData noData2 = new GraphData();
        noData2.type = "";
        noData2.value = new BigDecimal(6000);
        graphObj.dataPoints.add(noData2);

        //set up colors, etc:
        //noDataText.setVisibility(View.VISIBLE);
        graphColors = myContext.getResources().getStringArray(R.array.nodatachartcolors); //initialize for the "NO DATA" graph

        //draw the graph:
        renderGraph();
    }

    private void createGraph() {
        //set up colors, etc:
        //noDataText.setVisibility(View.INVISIBLE);
        graphColors = myContext.getResources().getStringArray(R.array.barchartcolors);

        //draw the graph:
        renderGraph();
    }

    private void renderGraph() {
        //setting possible max range to prevent odd valued max

        //set up data in correct format for graphing:
        String[] titles = new String[] { "Amount"}; //need to send something to the chart engine
        ArrayList<double[]> values = new ArrayList<double[]>(); //must be in this crazy format for chart engine
        ArrayList<GraphData> dataPoints = (ArrayList) graphObj.dataPoints;
        values.add(new double[] { dataPoints.get(0).value.doubleValue(), dataPoints.get(1).value.doubleValue() });

        //find max of range
        long maxrange = getMaxRange(values.get(0));

        int[] colors = new int[] { Color.parseColor(graphColors[0]), Color.parseColor(graphColors[1])};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        renderer.setOrientation(Orientation.HORIZONTAL);
        setChartSettings(renderer, "", "", "", 0.2,
                3, 0, maxrange, Color.BLACK, Color.BLACK);
        renderer.setXLabels(0);
        renderer.setYLabels(0);

        renderer.addXTextLabel(1, dataPoints.get(0).type);
        renderer.addXTextLabel(2, dataPoints.get(1).type);
        //setChartValuesFormat (percentFormat);
        for (int i = 1; i <= 5; i++) {
            double splitnumber = (maxrange/5)*i;
            renderer.addYTextLabel(splitnumber,formatValue(splitnumber));
        }

        if (mBarView != null) {
            chartHolder.removeView(mBarView);
        }
        mBarView = ChartFactory.getBarChartView(myContext,buildBarDataset(titles, values),renderer, BarChart.Type.DEFAULT);
        chartHolder.addView(mBarView);
        mBarView.repaint();
    }

    private long getMaxRange(double[] values) {
        long possibleMax[] = {1000,2500,5000,10000,25000,40000,50000,75000,100000,200000,300000,400000,500000,
                750000,1000000,2000000,3000000,4000000,5000000,6000000,7000000,8000000,9000000,10000000,15000000,25000000,
                50000000,100000000,200000000,500000000,1000000000,2000000000,3000000000L,4000000000L,5000000000L,
                10000000000L,50000000000L,100000000000L,500000000000L,1000000000000L};

        //get max value:
        double maxValue = 0;
        for (int i=0; i<values.length; i++) {
            if (maxValue < values[i]) {
                maxValue = values[i];
            }
        }

        //find max range value:
        long maxRange = 1;
        for (int i=0; i<possibleMax.length; i++) {
            if (maxValue < possibleMax[i]) {
                maxRange = possibleMax[i];
                break;
            }
        }

        return maxRange;
    }

    /*private void refreshGraph() {
        Logger.debug("AccountsTransactionsBarChart :: refreshGraph");

        //put values into a pair of arrays for creating the graph:



        ArrayList<GraphData> dataPoints = (ArrayList) graphObj.dataPoints;
        int numDataPoints = dataPoints.size();
        ArrayList<Bar> points = new ArrayList<Bar>();
        String[] allColors = new String[] {"#CCCCCC"}; //initialize for the "NO DATA" graph
        allColors = myContext.getResources().getStringArray(R.array.piechartcolors);
        for (int i = 0; i < numDataPoints; ++i) {
            Bar bar = new Bar();
            bar.setName(dataPoints.get(i).type);
            bar.setValue(dataPoints.get(i).value.floatValue()/1000);
            bar.setColor(Color.parseColor(allColors[i]));
            Logger.debug("bar: "+dataPoints.get(i).displayText+", "+dataPoints.get(i).value+", "+dataPoints.get(i).type);
            points.add(bar);
        }


    }*/

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setYLabelsPadding(5f);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        //---------
        renderer.setShowLegend(false);
        renderer.setLabelsTextSize(20);
        renderer.setAxisTitleTextSize(20);
        //---------
        renderer.setBarWidth(70);
        renderer.setAntialiasing(true);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setMargins(new int[] { 20, 50, 0, 0 });
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setLabelsTextSize(10);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setPanLimits(new double[]{0.2, 2.0, 0.0, 0.0});
        renderer.setZoomEnabled(false, false);

        XYSeriesRenderer r2 = new XYSeriesRenderer();
        r2.setColors(colors);
        renderer.addSeriesRenderer(r2);
        r2.setChartValuesTextSize(20);
        r2.setAnnotationsTextSize(20);

        return renderer;
    }

    public static String formatValue(double value) {
        int power;
        String suffix = " kmbt";
        String formattedNumber = "";

        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int)StrictMath.log10(value);
        value = value/(Math.pow(10,(power/3)*3));
        formattedNumber=formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power/3);
        return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
    }

}
