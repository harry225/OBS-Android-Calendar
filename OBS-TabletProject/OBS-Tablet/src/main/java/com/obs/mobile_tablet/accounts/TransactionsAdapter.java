package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

/**
 * Created by danielstensland on 1/12/14.
 */
public class TransactionsAdapter extends ArrayAdapter<TransactionObj> {

    private ArrayList<TransactionObj> items;
    private LayoutInflater mInflater;

    public OnItemListener mCallback;

    public boolean hidingAll;

    // Container Activity must implement this interface
    public interface OnItemListener {
        public void onCheckClicked(String checkUrl);
    }

    public TransactionsAdapter(Context context, int textViewResourceId, ArrayList<TransactionObj> items, Fragment instance) {
        super(context, textViewResourceId, items);
        mInflater = LayoutInflater.from((context));
        this.items = items;

        try {
            mCallback = (OnItemListener) instance;
        } catch (ClassCastException e) {
            throw new ClassCastException(instance.toString()
                    + " must implement OnItemListener");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ItemView itmView = null;

        if (v == null) {
            v = mInflater.inflate(R.layout.accounts_all_transactions_cell, parent, false);
            itmView = new ItemView();
            itmView.viewCell = (RelativeLayout) v.findViewById(R.id.view_cell);
            itmView.dateLabel = (TextView) v.findViewById(R.id.dateLabel);
            itmView.checkLabel = (Button) v.findViewById(R.id.checkLabel);
            itmView.descriptionLabel = (TextView) v.findViewById(R.id.descriptionLabel);
            itmView.extendedDescriptionLabel = (TextView) v.findViewById(R.id.extendedDescriptionLabel);
            itmView.creditLabel = (TextView) v.findViewById(R.id.creditLabel);
            itmView.debitLabel = (TextView) v.findViewById(R.id.debitLabel);
            itmView.balanceLabel = (TextView) v.findViewById(R.id.balanceLabel);
            itmView.expandImage = (ImageView) v.findViewById(R.id.expandImage);
            v.setTag(itmView);
        }
        else {

            itmView = (ItemView)convertView.getTag();
        }

        final TransactionObj transaction = getItem(position);

        if (transaction != null) {
            itmView.dateLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.dateLabel.setText(transaction.asOfDate);

            itmView.checkLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.checkLabel.setTextColor(v.getContext().getResources().getColor(R.color.obsAccent));
            itmView.checkLabel.setText(transaction.displayCheckNumber);

            if (transaction.getHasCheckImage() && transaction.getCheckImageUrl() != null) {
                itmView.checkLabel.setTextColor(v.getContext().getResources().getColor(R.color.obsAccentButton));
                itmView.checkLabel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Logger.debug("check image url is: " + transaction.getCheckImageUrl());
                        mCallback.onCheckClicked("http://www.onlinebankingsolutions.com/news/docs/SunTrust_CaseStudy_V54_20W.pdf");
                    }
                });
                itmView.checkLabel.setEnabled(true); //don't forget to re-enable the button. The views are recycled, dummy.

            } else {
                itmView.checkLabel.setTextColor(Color.parseColor("#000000"));
                itmView.checkLabel.setOnClickListener(null);
                itmView.checkLabel.setEnabled(false);
            }

            itmView.extendedDescriptionLabel.setVisibility(View.GONE);
            itmView.expandImage.setVisibility(View.GONE);

            ArrayList<String> additionalDescription = new ArrayList<String>(transaction.getAdditionalText());

            if(transaction.getAdditionalText() != null && transaction.getAdditionalText().size()>0) {

                itmView.expandImage.setVisibility(View.VISIBLE);
                itmView.extendedDescriptionLabel.setVisibility(View.VISIBLE);

                int themeColor = v.getResources().getColor(R.color.obsAccent);
                itmView.expandImage.getDrawable().setColorFilter(themeColor, PorterDuff.Mode.MULTIPLY);
                itmView.expandImage.setImageResource(R.drawable.expand_cell_icon_down);
                itmView.expandImage.getDrawable().setColorFilter(themeColor, PorterDuff.Mode.MULTIPLY);

                String descriptionText = "";
                for( String description : additionalDescription) {
                    descriptionText += description + "\n";
                }
                itmView.extendedDescriptionLabel.setText(descriptionText);
                itmView.showingExtendedDescription = true;

                itmView.viewCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ItemView itemView = (ItemView) view.getTag();

                        if(itemView.showingExtendedDescription) {

                            if(itemView.extendedDescriptionLabel.getVisibility()==View.VISIBLE) {

                                itemView.expandImage.setImageResource(R.drawable.expand_cell_icon_up);
                                itemView.extendedDescriptionLabel.setVisibility(View.GONE);
                            } else {

                                itemView.expandImage.setImageResource(R.drawable.expand_cell_icon_down);
                                itemView.extendedDescriptionLabel.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

                if(hidingAll) {
                    itmView.expandImage.setImageResource(R.drawable.expand_cell_icon_up);
                    itmView.extendedDescriptionLabel.setVisibility(View.GONE);
                    itmView.showingExtendedDescription = false;
                }

            } else {
                itmView.extendedDescriptionLabel.setVisibility(View.GONE);
                itmView.expandImage.setVisibility(View.GONE);
                itmView.showingExtendedDescription = false;
            }

            itmView.descriptionLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.descriptionLabel.setText(transaction.displayText);

            itmView.extendedDescriptionLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));

            itmView.creditLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.creditLabel.setText(transaction.creditAmount);

            itmView.debitLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.debitLabel.setTextColor(v.getContext().getResources().getColor(R.color.obsDebitTextColor));
            itmView.debitLabel.setText(transaction.debitAmount);

            itmView.balanceLabel.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
            itmView.balanceLabel.setText(transaction.balanceAmount);

            int val = position % 2;

            if(val == 1) {
                itmView.viewCell.setBackgroundColor(Color.parseColor("#FFFFFF"));

            } else {
                itmView.viewCell.setBackgroundColor( v.getResources().getColor(R.color.obsLightGray));

            }
        }

        return v;
    }

    public void closeAll () {

    }
/*
    private void toggleExtension(ItemView itemView) {

         if(itemView.showingExtendedDescription) {

             if(itemView.extendedDescriptionLabel.getVisibility()==View.VISIBLE) {

                 rotate( 0,  itemView.expandImage);
                 itemView.extendedDescriptionLabel.setVisibility(View.GONE);
             } else {

                 rotate( 180,  itemView.expandImage);
                 itemView.extendedDescriptionLabel.setVisibility(View.VISIBLE);
             }
         }
    }

    private void rotate(float degree, ImageView imageView) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        imageView.startAnimation(rotateAnim);
    }
*/
    private static class ItemView {
        TextView dateLabel;
        Button checkLabel;
        TextView descriptionLabel;
        TextView extendedDescriptionLabel;
        TextView creditLabel;
        TextView debitLabel;
        TextView balanceLabel;
        ImageView expandImage;
        RelativeLayout viewCell;
        boolean showingExtendedDescription;
    }

    private static android.graphics.BitmapFactory.Options getSize(Context c, int resId){
        android.graphics.BitmapFactory.Options o = new android.graphics.BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(c.getResources(), resId, o);
        return o;
    }
}