package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.ForApplication;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

public class AccountsListAdapter extends BaseAdapter {

    @Inject
    @ForApplication
    OBSApplication application;

    private Context mContext;
    private LayoutInflater mInflater;
	private ArrayList<Object> accountsList = new ArrayList<Object>();
    private Object[] accountsArrayList = {};
	private AccountsFragment fragmentInstance;
    private RelativeLayout contentHolder;
    private View currentOpenListItem = null;
    public OnItemListener mCallback;


    private static final int ITEM_VIEW_TYPE_ACCT = 0;
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
    private static final int ITEM_VIEW_TYPE_CHART = 2;
    private static final int ITEM_VIEW_TYPE_COUNT = 3;


    public void setAccountsList(ArrayList<Object> accountsList) {
        this.accountsList = accountsList;
    }

    public AccountsListAdapter(Context context, ArrayList<Object> acctsList, AccountsFragment instance) {
		//Logger.debug("AccountsListAdapter :: CONSTRUCTOR");
        mContext = context;
        mInflater = LayoutInflater.from((context));
		accountsList = acctsList;
        fragmentInstance = instance;
        try {
            mCallback = (OnItemListener) instance;
        } catch (ClassCastException e) {
            throw new ClassCastException(instance.toString()
                    + " must implement OnItemListener");
        }
	}

    // Container Activity must implement this interface
    public interface OnItemListener {
        //public void onItemClicked(View clickedView);
        public void onItemClicked(AccountObj acctObj);
    }

    @Override
    public int getCount() {
		return accountsList.size();
	}

    @Override
    public Object getItem(int position) {
		return accountsList.get(position);
	}

    @Override
    public long getItemId(int position) {
		return position;
	}

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int type = ITEM_VIEW_TYPE_ACCT; //default is account data
        if (accountsList.get(position) instanceof ListItemHeader) {
            //Logger.debug("I should be a header");
            type = ITEM_VIEW_TYPE_SEPARATOR;
        }
        else if(accountsList.get(position) instanceof ListItemGraphs) {
            //Logger.debug("I should be a chart");
            type = ITEM_VIEW_TYPE_CHART;
        }
        return type;
    }

    @Override
    public boolean isEnabled(int position) {
        // none of the listview items should be clickable! account items contain a button!
        return false; //getItemViewType(position) != ITEM_VIEW_TYPE_SEPARATOR;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final int type = getItemViewType(position); //figure out if this is a separator or an account

        if (type == ITEM_VIEW_TYPE_ACCT) {
            final ListItemAcct data = (ListItemAcct)getItem(position);
            ItemView itmView = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.accounts_list_item, parent, false);

                itmView = new ItemView();
                itmView.title = (TextView)convertView.findViewById(R.id.title);
                itmView.val1 = (TextView)convertView.findViewById(R.id.val1);
                itmView.val2 = (TextView)convertView.findViewById(R.id.val2);
                itmView.val3 = (TextView)convertView.findViewById(R.id.val3);
                itmView.val4 = (TextView)convertView.findViewById(R.id.val4);
                itmView.val5 = (TextView)convertView.findViewById(R.id.val5);
                itmView.noData = (TextView)convertView.findViewById(R.id.no_data);
                itmView.arrow = (ImageView)convertView.findViewById(R.id.arrow);
                itmView.contentHolder = (RelativeLayout) convertView.findViewById(R.id.listitem_container);
                itmView.viewAllTransactionsButton = (Button) convertView.findViewById(R.id.view_all_transactions_button);
                convertView.setTag(itmView);
            }
            else {
                itmView = (ItemView)convertView.getTag();
            }


            itmView.title.setText(data.name.toUpperCase());
            if (!data.dataReported) {
                itmView.noData.setVisibility(View.VISIBLE);
            }
            else {
                itmView.noData.setVisibility(View.INVISIBLE);
            }

            //int themeColor = convertView.getResources().getColor(R.color.obsAccent);
            //itmView.arrow.getDrawable().setColorFilter(themeColor, PorterDuff.Mode.MULTIPLY);

            //always need to repopulate these because of how the views recycle:
            itmView.val1.setText(data.balances.get(0));
            itmView.val2.setText(data.balances.get(1));
            itmView.val3.setText(data.balances.get(2));
            itmView.val4.setText(data.balances.get(3));
            itmView.val5.setText(data.balances.get(4));

            //set all custom fonts:
            itmView.title.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_PRIMARY_BUTTON));
            itmView.noData.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_PRIMARY_BUTTON));
            itmView.val1.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_DATA));
            itmView.val2.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_DATA));
            itmView.val3.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_DATA));
            itmView.val4.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_DATA));
            itmView.val5.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_DATA));

            //expand button:
            final ListView parentListView = (ListView) parent;
            final RelativeLayout myContainer = itmView.contentHolder; //need this in the onClick...
            itmView.contentHolder.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Logger.debug("clicked item " + position);
                    mCallback.onItemClicked(data.accountObj);
                }
            });
        }
        else if (type == ITEM_VIEW_TYPE_SEPARATOR) {
            ListItemHeader data = (ListItemHeader)getItem(position);
            HeaderView hdrView = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.accounts_list_separator, parent, false);

                hdrView = new HeaderView();
                hdrView.title = (TextView)convertView.findViewById(R.id.title);
                hdrView.val1 = (TextView)convertView.findViewById(R.id.val1);
                hdrView.val2 = (TextView)convertView.findViewById(R.id.val2);
                hdrView.val3 = (TextView)convertView.findViewById(R.id.val3);
                hdrView.val4 = (TextView)convertView.findViewById(R.id.val4);
                hdrView.val5 = (TextView)convertView.findViewById(R.id.val5);
                convertView.setTag(hdrView);
            }
            else {
                hdrView = (HeaderView)convertView.getTag();
            }

            hdrView.title.setText(data.name);

            hdrView.val1.setText(data.balanceTypes.get(0).toUpperCase());
            hdrView.val2.setText(data.balanceTypes.get(1).toUpperCase());
            hdrView.val3.setText(data.balanceTypes.get(2).toUpperCase());
            hdrView.val4.setText(data.balanceTypes.get(3).toUpperCase());
            hdrView.val5.setText(data.balanceTypes.get(4).toUpperCase());

            //set custom fonts:
            hdrView.title.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_HEADER));
            hdrView.val1.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_HEADER));
            hdrView.val2.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_HEADER));
            hdrView.val3.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_HEADER));
            hdrView.val4.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_HEADER));
            hdrView.val5.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_TABLE_HEADER));
        }
        else { //if (type == ITEM_VIEW_TYPE_CHART) {
            ListItemGraphs data = (ListItemGraphs)getItem(position);
            GraphView grphView = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.accounts_list_chart_item, parent, false);

                grphView = new GraphView();
                grphView.title = (TextView)convertView.findViewById(R.id.title);
                grphView.chartHolder = (LinearLayout)convertView.findViewById(R.id.chart_holder);
                convertView.setTag(grphView);
            }
            else {
                grphView = (GraphView)convertView.getTag();
            }

            grphView.title.setText(data.name);

            //set custom fonts:
            grphView.title.setTypeface(FontFace.GetFace(mContext, FontFace.FONT_PRIMARY_BUTTON));

            //need this to hold the charts so we can set their widths:
            final ArrayList<AccountOverviewChartBase> chartViews = new ArrayList<AccountOverviewChartBase>();

            //set up the graphs with the correct layout and add them:
            if (grphView.chartHolder.getChildCount() <= 0) { //if there are no charts, add them:

                final int numGraphs = data.graphs.size();

                Logger.debug("number of graphs: " + numGraphs);
                for (GraphObj graphData : data.graphs) {
                    AccountOverviewChartBase newGraph;
                    if (numGraphs >= 2) { //use regular layout
                        Logger.debug("reg chart");
                        newGraph = new AccountOverviewChart(mContext);
                    }
                    else { //use alternate layout
                        Logger.debug("alt chart");
                        newGraph = new AccountOverviewChartAlternate(mContext);
                    }
                    chartViews.add(newGraph);
                    newGraph.setData(graphData);
                    grphView.chartHolder.addView(newGraph, new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
            }

            //set the width of the graph blocks:
            final HorizontalScrollView contentWrapper = (HorizontalScrollView) convertView.findViewById(R.id.scrollview);
            contentWrapper.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //Logger.debug("the chart wrapper width is: " + contentWrapper.getWidth());
                    int visiblePaneWidth = contentWrapper.getWidth();
                    final int numGraphs = chartViews.size();
                    int dividend = numGraphs;
                    if (numGraphs > 3) { dividend = 3; }
                    if (numGraphs < 1) { dividend = 1; }
                    //dividend = 1; //set manually for testing layout
                    int chartWidth = visiblePaneWidth/dividend;
                    for (int i = 0; i < numGraphs; i++) {
                        chartViews.get(i).setWidth(chartWidth);
                    }
                    ViewTreeObserver obs = contentWrapper.getViewTreeObserver();
                    try {
                        obs.removeOnGlobalLayoutListener(this);
                    } catch (NoSuchMethodError e) {
                        //this is the deprecated method - used in < API 16:
                        obs.removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }

        return convertView;
	}

    private static class ItemView {
        TextView title;
        TextView val1;
        TextView val2;
        TextView val3;
        TextView val4;
        TextView val5;
        TextView noData;
        ImageView arrow;
        RelativeLayout contentHolder;
        Button viewAllTransactionsButton;
    }

    private static class HeaderView {
        TextView title;
        TextView val1;
        TextView val2;
        TextView val3;
        TextView val4;
        TextView val5;
    }

    private static class GraphView {
        TextView title;
        LinearLayout chartHolder;
    }

}
