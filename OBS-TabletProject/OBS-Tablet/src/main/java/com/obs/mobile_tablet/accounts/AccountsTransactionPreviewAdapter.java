package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

/**
 * Created by danielstensland on 1/13/14.
 */

public class AccountsTransactionPreviewAdapter extends ArrayAdapter<TransactionObj> {

    private ArrayList<TransactionObj> transactionItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public OnItemListener mCallback;


    public AccountsTransactionPreviewAdapter(Context context, int textViewResourceId, ArrayList<TransactionObj> items, AccountSummaryFragment instance) {
        super(context, textViewResourceId, items);
        mContext = context;
        mInflater = LayoutInflater.from((context));
        transactionItems = items;
        try {
            mCallback = (OnItemListener) instance;
        } catch (ClassCastException e) {
            throw new ClassCastException(instance.toString()
                    + " must implement OnItemListener");
        }
    }

    // Container Activity must implement this interface
    public interface OnItemListener {
        public void onCheckClicked(String checkUrl);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ItemView itemView = null;

        if (v == null) {
            v = mInflater.inflate(R.layout.accounts_transactions_preview_cell, parent, false);
            itemView = new ItemView();
            itemView.viewCell = (LinearLayout) v.findViewById(R.id.view_cell);
            itemView.dateLabel = (TextView) v.findViewById(R.id.dateLabel);
            itemView.checkLabel = (Button) v.findViewById(R.id.checkLabel);
            itemView.descriptionLabel = (TextView) v.findViewById(R.id.descriptionLabel);
            itemView.creditLabel = (TextView) v.findViewById(R.id.creditLabel);
            itemView.debitLabel = (TextView) v.findViewById(R.id.debitLabel);
            v.setTag(itemView);
        }
        else {
            itemView = (ItemView)convertView.getTag();
        }

        final TransactionObj transaction = getItem(position);

        if (transaction != null) {

            itemView.dateLabel.setText(transaction.asOfDate);
            itemView.checkLabel.setText(transaction.displayCheckNumber);
            if (transaction.getHasCheckImage() && transaction.getCheckImageUrl() != null) {
                itemView.checkLabel.setTextColor(mContext.getResources().getColor(R.color.obsAccentButton));
                itemView.checkLabel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Logger.debug("check image url is: " + transaction.getCheckImageUrl());
                        mCallback.onCheckClicked("http://www.onlinebankingsolutions.com/news/docs/SunTrust_CaseStudy_V54_20W.pdf");
                    }
                });
                itemView.checkLabel.setEnabled(true); //don't forget to re-enable the button. The views are recycled, dummy.
            }
            else {
                itemView.checkLabel.setTextColor(Color.parseColor("#000000"));
                itemView.checkLabel.setOnClickListener(null);
                itemView.checkLabel.setEnabled(false);
            }
            itemView.descriptionLabel.setText(transaction.displayText);
            itemView.creditLabel.setText(transaction.creditAmount);
            itemView.debitLabel.setText(transaction.debitAmount);

            //Logger.debug("========"+transaction.getCheckImageUrl());

            itemView.debitLabel.setTextColor(v.getContext().getResources().getColor(R.color.obsDebitTextColor));

            //SET CUSTOM FONTS:
            itemView.dateLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itemView.checkLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itemView.descriptionLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itemView.creditLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itemView.debitLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));

            int val = position % 2;
            if(val == 0) {
                itemView.viewCell.setBackgroundColor(v.getResources().getColor(R.color.obsLightGray));
            }
            else {
                itemView.viewCell.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        return v;
    }

    private static class ItemView {
        TextView dateLabel;
        Button checkLabel;
        TextView descriptionLabel;
        TextView creditLabel;
        TextView debitLabel;
        LinearLayout viewCell;
    }
}