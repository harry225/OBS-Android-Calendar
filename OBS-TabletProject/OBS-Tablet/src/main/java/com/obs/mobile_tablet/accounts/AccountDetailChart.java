package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.GraphData;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by richardvernon on 1/14/14.
 */
public class AccountDetailChart extends LinearLayout {

    private Context myContext;
    private GraphicalView mChartView;
    private GridView legendGridview;
    private LinearLayout chartHolder;
    private TextView noDataText;

    private GraphObj graphObj = new GraphObj();
    private String[] graphColors;

    private int selected_position;


    public AccountDetailChart(Context context) {
        super(context);
        setup(context);
        refreshGraph();
    }

    public AccountDetailChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public AccountDetailChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setup(context);
        refreshGraph();
    }

    public void setData(GraphObj newGraphObj) {
        //Logger.debug("AccountDetailChart :: setData "); // + newGraphObj.displayText + "," + newGraphObj.type);
        graphObj = newGraphObj;
        /*for (GraphData dataPoint : graphObj.dataPoints) {
          Logger.debug(dataPoint.displayText+", "+dataPoint.type + ", " + dataPoint.date + ", "+ dataPoint.value);
        }*/
        refreshGraph();
    }

    public void repaint() {
        mChartView.repaint();
    }

    private void setup(Context context) {
        LayoutInflater.from(context).inflate(R.layout.accounts_details_pie_chart, this, true);
        myContext = context;
        legendGridview = (GridView) findViewById(R.id.legend_gridView);
        chartHolder = (LinearLayout) findViewById(R.id.detail_piechart_container);
        noDataText = (TextView) findViewById(R.id.no_data_text);
        noDataText.setTypeface(FontFace.GetFace(myContext, FontFace.FONT_PRIMARY_BUTTON));
    }

    private void refreshGraph() {
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
        mRenderer.setLabelsColor(Color.WHITE);
        mRenderer.setTextTypeface("sans_serif_bold", Typeface.BOLD);
        mRenderer.setLabelsTextSize(18);
        mRenderer.setScale(1.3f);
        mRenderer.setMargins(new int[]{0,0,0,0});

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
            renderer.setChartValuesFormat(percentFormat);
        }

        mRenderer.setDisplayValues(true);
        selected_position = -1;

        if (mChartView != null) {
            chartHolder.removeView(mChartView);
        }

        mChartView =  ChartFactory.getPieChartView(myContext, mSeries, mRenderer);
        chartHolder.addView(mChartView);
        mChartView.repaint();

        final LegendAdapter legendAdapter;
        if (showLegend) {
            legendAdapter = new LegendAdapter(myContext,categoryNames);
        }
        else { //clear the legend listview:
            legendAdapter = new LegendAdapter(myContext,new String[0]);
        }
        legendGridview.setAdapter(legendAdapter);
        legendGridview.setVerticalSpacing(0);
        legendGridview.setHorizontalSpacing(0);


        mChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {
                    for (int i = 0; i < mSeries.getItemCount(); i++) {
                        if (mRenderer.getSeriesRendererAt(i).isHighlighted()) {
                            mRenderer.getSeriesRendererAt(i).setHighlighted(false);
                            selected_position = -1;
                            legendAdapter.notifyDataSetChanged();
                            legendGridview.invalidateViews();
                            mChartView.repaint();
                            i = mSeries.getItemCount();
                        } else {
                            if (i == seriesSelection.getPointIndex()){
                                mRenderer.getSeriesRendererAt(i).setHighlighted(true);
                                selected_position = i;
                                legendAdapter.notifyDataSetChanged();
                                legendGridview.invalidateViews();
                                mChartView.repaint();
                            }
                        }
                    }
                }
            }
        });


        legendGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int iDeselect = -1;
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                int viewcount = parent.getChildCount();
                for (int i = 0; i < viewcount; i++) {
                    View resetviews = parent.getChildAt(i);
                    resetviews.findViewById(R.id.highlightrow).setBackgroundColor(Color.TRANSPARENT);
                    mRenderer.getSeriesRendererAt(i).setHighlighted(false);
                    mChartView.repaint();
                }
                RelativeLayout hilite = (RelativeLayout) view.findViewById(R.id.highlightrow);
                if (iDeselect != position) {
                    hilite.setBackgroundColor(getResources().getColor(R.color.obsHighlight));
                    mRenderer.getSeriesRendererAt(position).setHighlighted(true);
                    mChartView.repaint();
                    iDeselect = position;
                } else {
                    hilite.setBackgroundColor(Color.TRANSPARENT);
                    iDeselect = -1;
                }
            }

        });
    }



    private class LegendAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;


        public LegendAdapter(Context context, String[] values) {
            super(context, R.layout.account_overview_legend_row_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.account_details_pie_legend_row, parent, false);

            TextView icon = (TextView) rowView.findViewById(R.id.icon);
            TextView itemName = (TextView) rowView.findViewById(R.id.item_name);

            //set custom fonts:
            itemName.setTypeface(FontFace.GetFace(myContext, FontFace.FONT_TABLE_DATA));

            RelativeLayout backrow = (RelativeLayout) rowView.findViewById(R.id.row);
            RelativeLayout row = (RelativeLayout) rowView.findViewById(R.id.highlightrow);
            row.setTag(position);
            itemName.setText(values[position]);
            String[] allColors = context.getResources().getStringArray(R.array.piechartcolors);
            icon.setBackgroundColor(Color.parseColor(allColors[position]));
            if (position == selected_position) {
                row.setBackgroundColor(context.getResources().getColor(R.color.obsHighlight));
            }
            else {
                row.setBackgroundColor(Color.TRANSPARENT);
            }

            /*
            int alternator = position++;
            if (alternator % 4 == 0 || alternator%4 == 3) {
                row.setBackgroundColor(context.getResources().getColor(R.color.obsLightGray));
            }
            else {
                row.setBackgroundColor(Color.WHITE);
            }
            */


            return rowView;
        }
    }
}
