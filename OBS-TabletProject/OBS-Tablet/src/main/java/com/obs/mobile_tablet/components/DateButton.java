package com.obs.mobile_tablet.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by danielstensland on 1/15/14.
 */
public class DateButton extends RelativeLayout {

    private TextView monthLabel;
    private TextView dayLabel;
    private TextView yearLabel;

    public Date date;

    //assumes that the progress indicator should default to 0;
    public DateButton(Context context) {
        super(context);
        init();
    }

    //assumes that the progress indicator should default to 0;
    public DateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //assumes that the progress indicator should default to 0;
    public DateButton (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(this.getContext()).inflate(R.layout.widget_date_button, this);

        monthLabel = (TextView)findViewById(R.id.month_label);
        monthLabel.setTypeface(FontFace.GetFace(this.getContext(), FontFace.FONT_TERTIARY_BUTTON));
        dayLabel = (TextView)findViewById(R.id.day_label);
        dayLabel.setTypeface(FontFace.GetFace(this.getContext(), FontFace.FONT_TABLE_DATA));
        yearLabel = (TextView)findViewById(R.id.year_label);
        yearLabel.setTypeface(FontFace.GetFace(this.getContext(), FontFace.FONT_TERTIARY_BUTTON));

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date d1 = c.getTime(); //the midnight, that's the first second of the day.
        setDate(d1);
    }

    public void setDate (Date newDate) {

        date = newDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        monthLabel.setText(new SimpleDateFormat("MMM").format(date));
        dayLabel.setText(new SimpleDateFormat("dd").format(date));
        yearLabel.setText(new SimpleDateFormat("yyyy").format(date));
    }
}