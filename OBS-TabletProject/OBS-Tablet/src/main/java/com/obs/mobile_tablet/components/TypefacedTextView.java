package com.obs.mobile_tablet.components;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import android.graphics.Typeface;

import com.obs.mobile_tablet.R;


public class TypefacedTextView extends TextView
{
    public TypefacedTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // Typeface.createFromAsset doesn't work in the layout editor. Skipping ...
        if (isInEditMode())
        {
            return;
        }



        //TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        //String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
        //styledAttrs.recycle();

       // if (fontName != null)
       // {
         //   Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
          //  setTypeface(typeface);
       // }
    }
}