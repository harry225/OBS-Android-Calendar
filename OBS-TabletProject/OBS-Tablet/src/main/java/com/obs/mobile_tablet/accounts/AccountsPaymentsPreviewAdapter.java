package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.PaymentThumbnailObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

/**
 * Created by danielstensland on 1/13/14.
 */

public class AccountsPaymentsPreviewAdapter extends ArrayAdapter<PaymentThumbnailObj> {

    private ArrayList<PaymentThumbnailObj> paymentItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public AccountsPaymentsPreviewAdapter(Context context, int textViewResourceId, ArrayList<PaymentThumbnailObj> items) {
        super(context, textViewResourceId, items);
        mContext = context;
        mInflater = LayoutInflater.from((context));
        paymentItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ItemView itmView = null;

        if (v == null) {
            v = mInflater.inflate(R.layout.accounts_payments_preview_cell, parent, false);
            itmView = new ItemView();
            itmView.typeIcon = (ImageView) v.findViewById(R.id.payment_type_icon);
            itmView.amountLabel = (TextView) v.findViewById(R.id.payment_amount_label);
            itmView.dateLabel = (TextView) v.findViewById(R.id.payment_date_label);
            v.setTag(itmView);
        }
        else {
            itmView = (ItemView)convertView.getTag();
        }
        PaymentThumbnailObj payment = getItem(position);

        if (payment != null) {

            //TODO: Add remaining types to if/else statement:
            //Logger.debug("pay type: "+payment.type+", "+payment.statusCategory);
            if (payment.type.contentEquals("ACH")) {
                itmView.typeIcon.setImageResource(R.drawable.type_ach);
            }
            else if (payment.type.contentEquals("ACH_TAXES")) {
                itmView.typeIcon.setImageResource(R.drawable.type_tax);
            }
            else if (payment.type.contentEquals("ACCOUNT_TRANSFER")) {
                itmView.typeIcon.setImageResource(R.drawable.type_transfer);
            }
            else if (payment.type.contentEquals("WIRE")) {
                itmView.typeIcon.setImageResource(R.drawable.type_wire);
            }
            else if (payment.type.contentEquals("INT_WIRE")) {
                itmView.typeIcon.setImageResource(R.drawable.type_internationalwire);
            }
            else { //should never get this one, but just in case:
                itmView.typeIcon.setImageResource(R.drawable.type_payee);
            }

            itmView.amountLabel.setText(payment.amount);
            itmView.dateLabel.setText(payment.paymentDate.toString());

            //SET CUSTOM FONTS:
            itmView.amountLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.dateLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
        }
        return v;
    }

    private static class ItemView {
        ImageView typeIcon;
        TextView amountLabel;
        TextView dateLabel;
    }
}
