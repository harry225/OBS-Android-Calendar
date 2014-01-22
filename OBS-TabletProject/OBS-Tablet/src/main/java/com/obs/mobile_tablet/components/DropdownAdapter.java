package com.obs.mobile_tablet.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.utils.FontFace;

import java.util.ArrayList;

/**
 * Created by danielstensland on 1/14/14.
 */
public class DropdownAdapter extends ArrayAdapter<String> {

    private ArrayList<String> items;
    private LayoutInflater mInflater;

    public DropdownAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
        super(context, textViewResourceId, items);
        mInflater = LayoutInflater.from((context));
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ItemView itmView = null;

        if (v == null) {
            v = mInflater.inflate(R.layout.widget_spinner_layout, parent, false);
            itmView = new ItemView();
            itmView.spinnerLayout = (RelativeLayout) v.findViewById(R.id.spinner_layout);
            itmView.spinnerText = (TextView) v.findViewById(R.id.spinner_text);
            itmView.spinnerText.setTypeface(FontFace.GetFace( v.getContext(), FontFace.FONT_PRIMARY_FRONT));
            itmView.spinnerText.setTextColor(Color.parseColor("#FFFFFF"));
            itmView.spinnerArrow = (ImageView) v.findViewById(R.id.spinner_arrow);

            int themeColor = v.getResources().getColor(R.color.obsAccent);
            itmView.spinnerArrow.getDrawable().setColorFilter(themeColor, PorterDuff.Mode.MULTIPLY);

            v.setTag(itmView);
        } else {
            itmView = (ItemView) v.getTag();
        }

        String itemText = items.get(position);

        if (itemText != null) {
            itmView.spinnerText.setText(itemText);
        }
        return v;
    }
    private static class ItemView {
        ImageView spinnerArrow;
        TextView spinnerText;
        RelativeLayout spinnerLayout;
    }
}

/*
TextView spinnerText = (TextView) v.findViewById(R.id.spinner_text);
spinnerText.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_PAGE_TITLE));
        spinnerText.setTextColor(Color.parseColor("white"));*/