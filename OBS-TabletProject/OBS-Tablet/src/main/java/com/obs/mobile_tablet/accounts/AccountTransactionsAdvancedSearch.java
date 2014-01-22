package com.obs.mobile_tablet.accounts;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.components.DateButton;
import com.obs.mobile_tablet.components.TimePickerFragment;
import com.obs.mobile_tablet.datamodel.reporting.TransactionGroup;
import com.obs.mobile_tablet.datamodel.reporting.TransactionSearch;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by danielstensland on 1/15/14.
 */
public class AccountTransactionsAdvancedSearch extends RelativeLayout {

    private TextView titleText;
    public Button closeButton;
    public Button submitButton;
    public Spinner groupSpinner;
    private RelativeLayout advancedSearchWrapper;
    public DateButton startDateButton;
    public DateButton endDateButton;
    public ArrayList<TransactionGroup> transactionGroups;

    private EditText startCheckField;
    private EditText endCheckField;
    private EditText startAmountField;
    private EditText endAmountField;

    public AccountTransactionsAdvancedSearch(Context context) {
        super(context);
        init(context);
    }

    public AccountTransactionsAdvancedSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AccountTransactionsAdvancedSearch (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        View.inflate(context, R.layout.accounts_transactions_advanced_search_box, this);

        advancedSearchWrapper = (RelativeLayout) findViewById(R.id.advanced_search_wrapper);
        titleText = (TextView) findViewById(R.id.advanced_search_title);
        titleText.setTypeface(FontFace.GetFace(context, FontFace.FONT_ALERT_BOX));
        closeButton = (Button) findViewById(R.id.advanced_search_close_button);
        submitButton = (Button) findViewById(R.id.advanced_search_submit_button);
        submitButton.setTypeface(FontFace.GetFace(context, FontFace.FONT_PRIMARY_BUTTON));
        groupSpinner = (Spinner) findViewById(R.id.advanced_search_group_spinner);

        startCheckField = (EditText) findViewById(R.id.check_field_start);
        endCheckField = (EditText) findViewById(R.id.check_field_end);
        startAmountField = (EditText) findViewById(R.id.enter_amount_start);
        endAmountField = (EditText) findViewById(R.id.enter_amount_end);

        startDateButton = (DateButton) findViewById(R.id.start_date_field);
        endDateButton = (DateButton) findViewById(R.id.end_date_field);

        TextView checkPrompt = (TextView) findViewById(R.id.enter_check_label);
        checkPrompt.setTypeface(FontFace.GetFace(context, FontFace.FONT_TABLE_HEADER));
        TextView checkToLabel = (TextView) findViewById(R.id.check_to_label);
        checkToLabel.setTypeface(FontFace.GetFace(context, FontFace.FONT_PRIMARY_BUTTON));
        checkToLabel.setTextSize(12);

        TextView amountPrompt = (TextView) findViewById(R.id.amount_range_label);
        amountPrompt.setTypeface(FontFace.GetFace(context, FontFace.FONT_TABLE_HEADER));
        TextView amountToLabel = (TextView) findViewById(R.id.amount_to_label);
        amountToLabel.setTypeface(FontFace.GetFace(context, FontFace.FONT_PRIMARY_BUTTON));
        amountToLabel.setTextSize(12);

        TextView datePrompt = (TextView) findViewById(R.id.date_range_label);
        datePrompt.setTypeface(FontFace.GetFace(context, FontFace.FONT_TABLE_HEADER));
        TextView dateToLabel = (TextView) findViewById(R.id.date_to_label);
        dateToLabel.setTypeface(FontFace.GetFace(context, FontFace.FONT_PRIMARY_BUTTON));
        dateToLabel.setTextSize(12);

    }

    public void expand() {

        advancedSearchWrapper.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        advancedSearchWrapper.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, advancedSearchWrapper.getMeasuredHeight());
        mAnimator.start();
    }

    public void collapse() {
        int finalHeight = advancedSearchWrapper.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = advancedSearchWrapper.getLayoutParams();
                layoutParams.height = value;
                advancedSearchWrapper.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public TransactionSearch getTransactionSearch() {

        TransactionSearch newSearch = new TransactionSearch();

        newSearch.allTransactionGroups = groupSpinner.getSelectedItemPosition() == 0;

        if(!newSearch.allTransactionGroups) {
            newSearch.transactionGroups = new ArrayList<TransactionGroup> (){{ add((TransactionGroup) transactionGroups.get(groupSpinner.getSelectedItemPosition() - 1)); }};
        }

        if(startCheckField.getText().length() > 0) {
            newSearch.minCheckNumber = startCheckField.getText().toString();
        }

        if(endCheckField.getText().length() > 0) {
            newSearch.maxCheckNumber = endCheckField.getText().toString();
        }

        if(startAmountField.getText().length() > 0) {
            newSearch.minAmount = new BigDecimal(startAmountField.getText().toString());
        }

        if(endAmountField.getText().length() > 0) {
            newSearch.maxAmount = new BigDecimal(endAmountField.getText().toString());
        }

        newSearch.fromDate = startDateButton.date;
        newSearch.toDate = endDateButton.date;

        return newSearch;
    }
}