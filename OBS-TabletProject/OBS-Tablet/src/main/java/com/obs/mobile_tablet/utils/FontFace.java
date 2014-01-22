package com.obs.mobile_tablet.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontFace {

    //Font Categories:
    public static final String FONT_BASE = "fonts/InterstateRegular.ttf";
    public static final String FONT_INPUT_FIELD = "fonts/InterstateRegular.ttf";
    public static final String FONT_ALERT_BOX = "fonts/InterstateBold.ttf";
    public static final String FONT_HEADER_1 = "fonts/InterstateBlack.ttf";
    public static final String FONT_HEADER_2 = "fonts/InterstateRegular.ttf";
    public static final String FONT_HEADER_3 = "fonts/Interstate Bold Cond.ttf";
    public static final String FONT_LABEL = "fonts/InterstateRegular.ttf";
    public static final String FONT_TABLE_HEADER = "fonts/Interstate-RegularCondensed.ttf";
    public static final String FONT_TABLE_DATA = "fonts/Interstate-RegularCondensed.ttf";
    public static final String FONT_PRIMARY_FRONT = "fonts/InterstateBold.ttf";
    public static final String FONT_SECONDARY_FRONT = "fonts/InterstateBold.ttf";
    public static final String FONT_TERTIARY_FRONT = "fonts/InterstateRegular.ttf";
    public static final String FONT_PRIMARY_BUTTON = "fonts/InterstateBlack.ttf";
    public static final String FONT_SECONDARY_BUTTON = "fonts/InterstateBold.ttf";
    public static final String FONT_TERTIARY_BUTTON = "fonts/InterstateBold.ttf";
    public static final String FONT_DISABLED_BUTTON = "fonts/InterstateBold.ttf";
    public static final String FONT_BLACK_BUTTON = "fonts/InterstateBold.ttf";
    public static final String FONT_DROPDOWN = "fonts/Interstate-RegularCondensed.ttf";
    public static final String FONT_LABEL_TITLE = "fonts/Interstate Bold Cond.ttf";
    public static final String FONT_LABEL_FIELD = "fonts/Interstate-RegularCondensed.ttf";
    public static final String FONT_PAGE_TITLE = "fonts/InterstateBold.ttf";
    public static final String FONT_PROGRESS_BAR = "fonts/InterstateRegular.ttf";
    public static final String FONT_TABLE_TITLE = "fonts/InterstateRegular.ttf";


    public static Typeface GetFace(Context context, String value) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),value);
        return typeface;
    }
}
