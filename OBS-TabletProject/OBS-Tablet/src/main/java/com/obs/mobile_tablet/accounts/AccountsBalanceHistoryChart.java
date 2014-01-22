package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.BalanceObj;

import com.obs.mobile_tablet.datamodel.reporting.GraphData;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import org.achartengine.GraphicalView;


/**
 * Created by jaimiespetseris on 1/9/14.
 */
public class AccountsBalanceHistoryChart extends LinearLayout {

    private Context myContext;
    private LinearLayout chartHolder;
    private TextView noDataText;
    private GraphicalView mChartView;
    private ArrayList<BalanceObj> balances;
    protected int[] graphColors;

    protected Date[] datesArray;
    protected double[] valuesArray;


    //assumes that the progress indicator should default to 0;
    public AccountsBalanceHistoryChart(Context context) {
        super(context);
        setup(context);
        refreshGraph();
    }

    //assumes that the progress indicator should default to 0;
    public AccountsBalanceHistoryChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    //assumes that the progress indicator should default to 0;
    public AccountsBalanceHistoryChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public void setData(ArrayList<BalanceObj> newBalances) {
        Logger.debug("AccountsBalanceHistoryChart :: setData " + newBalances.size());
        balances = newBalances;
        for (BalanceObj dataPoint : balances) {
            Logger.debug("====="+dataPoint.date+", "+ dataPoint.amount);
        }
        refreshGraph();
    }

    public void repaint() {
        mChartView.repaint();
    }

    private void setup(Context context) {
        LayoutInflater.from(context).inflate(R.layout.accounts_balance_history_chart, this, true);
        myContext = context;
        chartHolder = (LinearLayout) findViewById(R.id.line_chart);
        noDataText = (TextView) findViewById(R.id.no_data_text);

        noDataText.setTypeface(FontFace.GetFace(myContext, FontFace.FONT_PRIMARY_BUTTON));
    }

    protected void refreshGraph() {
        if (balances == null || balances.size() == 0) {
            createNoDataGraph();
        }
        else {
            createNoDataGraph();
        }
    }

    protected void createNoDataGraph() {
        //Logger.debug("AccountOverviewChartBase :: createNoDataGraph");

        BalanceObj noData = new BalanceObj();

        datesArray = new Date[] { new Date(2013, 11,1), new Date(2013, 11, 20), new Date(2013, 11, 30),
                new Date(2014, 0, 10), new Date(2014, 0, 25) };
        valuesArray = new double[] { 20000, 45000, 80000, 65000, 50000};

        //set up colors, etc:
        noDataText.setVisibility(View.INVISIBLE);
        //graphColors = parseColors(myContext.getResources().getStringArray(R.array.nodatachartcolors));

        //draw the graph:
        renderGraph();
    }

    protected void createGraph() {
        //Logger.debug("AccountOverviewChartBase :: createGraph");

        datesArray = new Date[balances.size()];
        valuesArray = new double[balances.size()];
        for (int i=0; i<balances.size(); i++) {
            BalanceObj balance = balances.get(i);
            datesArray[i] = balance.date;
            valuesArray[i] = Double.parseDouble(balance.amount);
        }

        //set up colors, etc:
        noDataText.setVisibility(View.INVISIBLE);
        //graphColors = parseColors(myContext.getResources().getStringArray(R.array.linechartcolors));

        //draw the graph:
        renderGraph();
    }

    private int[] parseColors(String[] colorStrings) {
        int[] colors = new int[colorStrings.length];
        for (int i=0; i<colorStrings.length; i++) {
            colors[i] = Color.parseColor(colorStrings[i]);
        }
        return colors;
    }

    protected void renderGraph() {

        //set up data for graphing:
        String[] titles = new String[] { "" };
        List<Date[]> dates = new ArrayList<Date[]>();
        List<double[]> values = new ArrayList<double[]>();
        dates.add(datesArray);
        values.add(valuesArray);


        PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
        int[] colors = new int[] { getResources().getColor(R.color.obsGreen) };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "", "", "", datesArray[0].getTime(),
                datesArray[datesArray.length - 1].getTime(), 0, 100000, Color.GRAY, Color.LTGRAY);
        renderer.setYLabels(0);
        renderer.setXLabels(0);
        renderer.setShowLegend(false);

        long maxrange = getMaxRange(values.get(0));
        for (int i = 1; i <= 15; i++) {
            double splitnumber = (maxrange/5)*i;
            renderer.addYTextLabel(splitnumber,AccountsTransactionBarChart.formatValue(splitnumber));
        }

        for (int i = 0; i < datesArray.length; ++i) {
            String _newdate =  new SimpleDateFormat("MMM dd").format(datesArray[i]);
            renderer.addXTextLabel (datesArray[i].getTime(), _newdate);
        }

        renderer.setPanLimits(new double[]{datesArray[0].getTime(), datesArray[datesArray.length - 1].getTime(), 0.0, maxrange});
        renderer.setZoomEnabled(true,true);
        renderer.setZoomLimits(new double[]{datesArray[0].getTime(), datesArray[datesArray.length - 1].getTime(), 0.0, maxrange});

        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        XYSeriesRenderer xyRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
        FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
        fill.setColor(getResources().getColor(R.color.obsGreen));
        xyRenderer.addFillOutsideLine(fill);
        fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_BELOW);
        fill.setColor(Color.MAGENTA);
        xyRenderer.addFillOutsideLine(fill);
        fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
        fill.setColor(Color.argb(255, 0, 200, 100));
        fill.setFillRange(new int[] {10, 19});
        xyRenderer.addFillOutsideLine(fill);
        //Not Here

        if (mChartView != null) {
            chartHolder.removeView(mChartView);
        }
        mChartView = ChartFactory.getTimeChartView(myContext, buildDateDataset(titles, dates, values),
                 renderer, "MMM dd");
        chartHolder.addView(mChartView);
        mChartView.repaint();
    }

    protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
                                                       List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            TimeSeries series = new TimeSeries(titles[i]);
            Date[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        renderer.setMarginsColor(Color.WHITE);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);
        setRenderer(renderer, colors, styles);
        return renderer;
    }
    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] {15, 50, 0, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            //r.setColor(colors[i]);
            r.setColor(Color.TRANSPARENT);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAntialiasing(true);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
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
}
