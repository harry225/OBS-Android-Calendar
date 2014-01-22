package com.obs.mobile_tablet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.obs.mobile_tablet.components.CustomViewPager;
import com.obs.mobile_tablet.components.OBSProgressBar;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.registration.RegistrationAlertBox;
import com.obs.mobile_tablet.registration.RegistrationFragment;
import com.obs.mobile_tablet.registration.RegistrationFragment1;
import com.obs.mobile_tablet.registration.RegistrationFragment2;
import com.obs.mobile_tablet.registration.RegistrationFragment3;
import com.obs.mobile_tablet.utils.BannersReceivedEvent;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

public class RegistrationActivity extends ActionBarActivity implements RegistrationFragment.OnSubmitListener {

    @Inject
    SetupFacade setupFacade;

    @Inject
    Bus bus;

    public final Boolean SHOW_SKIP_BUTTONS = false;

    private FrameLayout splashHolder;
    private View splashScreen;
    private Button startButton;
    private CustomViewPager pager;
    public RegistrationAlertBox alertBox;
    private FrameLayout successHolder;
    private View successScreen;
    private Button launchAppButton;
    private ProgressDialog activitySpinner;
    private View activityRootView;
    private LinearLayout inputTabber;
    private Button prevButton;
    private Button nextButton;
    private Point p;
    private ImageButton helpButton;
    private String helpString;
    private ArrayList<Fragment> pageList = new ArrayList<Fragment>();

    private OBSProgressBar obsProgress;


    public void onFormSubmitted(int formNum) {
        //Logger.debug("RegistrationActivity :: onFormSubmitted");
        alertBox.clearAlert();
        switch (formNum) {
            case 0:
                showActivityIndicator("Registering Activation Key...");
                break;
            case 1:
                showActivityIndicator("Registering PIN...");
                break;
            case 2:
                showActivityIndicator("Verifying User Data...");
                break;
        }
    }

    @Override
    public void onFormSuccess(int formNum) {

        stopActivityIndicator();
        alertBox.clearAlert();
        switch(formNum) {
            case 0:
                helpString = getString(R.string.registration_help_2);
                goToFragment(1);
                break;
            case 1:
                helpString = getString(R.string.registration_help_3);
                goToFragment(2);
                break;
            case 2:
                showSuccess();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);

    }

    @Subscribe
     public void displayBanners(BannersReceivedEvent event) {
        for (String error : event.getErrorBanners()) {
            onAlert(RegistrationAlertBox.TYPE_WARNING, error);
        }
        for (String info : event.getInfoBanners()){
            onAlert(RegistrationAlertBox.TYPE_INFO, info);
        }
        for (String success : event.getSuccessBanners()){
            onAlert(RegistrationAlertBox.TYPE_SUCCESS, success);
        }
    }

    @Override
    public void onAlert(int type, String message) {
        stopActivityIndicator();

        final int finalType = type;
        final String finalMessage = message;

        this.runOnUiThread(new Runnable() {
            public void run() {

                alertBox.showAlert(finalType, finalMessage);
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        ImageButton button = (ImageButton) findViewById(R.id.infoButton);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Logger.debug("RegistrationActivity :: onCreate");
        super.onCreate(savedInstanceState);
        ((OBSApplication) getApplication()).inject(this);
        setContentView(R.layout.registration_activity);

        if (setupFacade.getIsSetup()) {
            goToLoginActivity();
        }
            int themeColor = getResources().getColor(R.color.obsPrimary);
            ImageView bgPattern = (ImageView) findViewById(R.id.registration_bg_imageview);
            bgPattern.getDrawable().setColorFilter(themeColor, PorterDuff.Mode.MULTIPLY);

            //splash screen initialization:
            splashHolder = (FrameLayout)this.findViewById(R.id.registration_splash_frame);
            splashScreen = LayoutInflater.from(this).inflate(R.layout.registration_splash, null);
            splashHolder.addView(splashScreen);
            startButton = (Button) findViewById(R.id.get_started_button);
            TextView tvWelcomeTxt = (TextView) findViewById(R.id.welcome_text_area);
            tvWelcomeTxt.setTextColor(getResources().getColor(R.color.obsMediumGray));
            tvWelcomeTxt.setText(getResources().getString(R.string.welcome_splash_text));

            //success panel initialization:
            successHolder = (FrameLayout)this.findViewById(R.id.registration_success_frame);
            successScreen = LayoutInflater.from(this).inflate(R.layout.registration_success, null);
            successHolder.addView(successScreen);
            launchAppButton = (Button) findViewById(R.id.launch_app_button);

            alertBox = (RegistrationAlertBox) findViewById(R.id.registration_alert_box);
            pager = (CustomViewPager) findViewById(R.id.registration_pager);

            activitySpinner = new ProgressDialog(this);
            //help button:
            helpButton = (ImageButton) findViewById(R.id.infoButton);
            helpButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //showDialog();
                    if (p != null) ;
                    showPopup(RegistrationActivity.this, p);
                }
            });
            helpString = getString(R.string.registration_help_1);

            //prev/next buttons for keyboard:
            inputTabber = (LinearLayout) findViewById(R.id.input_tabber);
            prevButton = (Button) findViewById(R.id.button_prev);
            nextButton = (Button) findViewById(R.id.button_next);

            //progress bar stuff:
            obsProgress = (OBSProgressBar)findViewById(R.id.obs_progress_bar);


            //INITIALIZE ALL CUSTOM FONTS:
            TextView welcomeMessage = (TextView) findViewById(R.id.welcome_message);
            welcomeMessage.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_HEADER_1));
            startButton.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_PRIMARY_BUTTON));
            launchAppButton.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_PRIMARY_BUTTON));

            prevButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Logger.debug("prev");
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Logger.debug("next");
                }
            });

        /*
        final CustomViewPager cvp = (CustomViewPager) findViewById(R.id.registration_pager);
        int[] screen = new int[2];
        cvp.getLocationOnScreen(screen);
        int[] window = new int[2];
        cvp.getLocationInWindow(window);
        //Logger.debug("ONE: "+window[0]+"|"+window[1]+", "+screen[0]+"|"+screen[1]);
        activityRootView = findViewById(R.id.container);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = activityRootView.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom);
                //Logger.debug("height diff: "+heightDiff+ ", "+screenHeight+", "+r.bottom);

                if (heightDiff > 100) { //the keyboard is showing:
                    //get screen offset from adjustPan:
                    int[] screen = new int[2];
                    cvp.getLocationOnScreen(screen);
                    if (startPos == 0) {
                        startPos = screen[1];
                    }
                    else {
                        offset = startPos - screen[1];
                    }

                    //TODO: see if this can be adjusted to place the view above the keyboard. The issue seems to be the actionbar height
                    //set position of inputTabber:
                    int yPosition = offset;
                    //THIS SHOULD PLACE IT JUST ABOVE THE KEYBOARD, BUT IT DOESN'T:
                    //int yPosition = offset + r.bottom - inputTabber.getHeight() - getActionBarHeight();

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = yPosition;
                    inputTabber.setLayoutParams(params);
                    if (inputTabber.getVisibility() == View.INVISIBLE) { //don't forget to show it:
                        inputTabber.setVisibility(View.VISIBLE);
                    }
                }
                else if (heightDiff < 100 && inputTabber.getVisibility() == View.VISIBLE) { //the keyboard is NOT showing:
                    inputTabber.setVisibility(View.INVISIBLE); //hide the inputTabber
                }
            }
        });
        */
        //Intent openIntent = new Intent(getApplicationContext(),MainActivity.class);
        //startActivity(openIntent);
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    protected void onStart() {
        //Logger.debug("RegistrationActivity :: onStart");
        super.onStart();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hide keyboard!
        splashHolder.setVisibility(View.VISIBLE);
        successHolder.setVisibility(View.INVISIBLE);
        enableStartButton();
    }

    private void enableStartButton() {
        //Logger.debug("RegistrationActivity :: enableStartButton");
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startButton.setOnClickListener(null); //take only one click
                //goToLoginActivity();
                hideSplash();
            }
        });
    }

    private void initRegistrationPager() {
        alertBox = (RegistrationAlertBox) findViewById(R.id.registration_alert_box);
        alertBox.clearAlert();
        pager = (CustomViewPager) findViewById(R.id.registration_pager);

        //set up the fragments for the pager:
        RegistrationFragment1 regFrag1 = RegistrationFragment1.newInstance(1);
        RegistrationFragment2 regFrag2 = RegistrationFragment2.newInstance(2);
        RegistrationFragment3 regFrag3 = RegistrationFragment3.newInstance(3);
        ArrayList<Fragment> pageList = new ArrayList<Fragment>();
        pageList.add(regFrag1);
        pageList.add(regFrag2);
        pageList.add(regFrag3);
        ((OBSApplication) getApplication()).inject(regFrag1);
        ((OBSApplication) getApplication()).inject(regFrag2);
        ((OBSApplication) getApplication()).inject(regFrag3);

        PagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), pageList);
        pager.setAdapter(adapter);

        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(adapter.getCount());

        //A little space between pages
        pager.setPageMargin(15);

        //If hardware acceleration is enabled, you should also remove
        //clipping on the pager for its children.
        pager.setClipChildren(false);

        pager.setPagingEnabled(false); //turn off the swipe functionality

        ArrayList<String> progressItemList = new ArrayList<String>();
        progressItemList.add("ACTIVATION KEY");
        progressItemList.add("CREATE A PIN");
        progressItemList.add("REGISTRATION QUESTIONS");

        //activation_btn.setTextColor(Color.BLACK);
        obsProgress.initItems(progressItemList);
    }

    private void hideSplash() {
        //goToLoginActivity();
        initRegistrationPager();
        obsProgress.update(0); //highlight the first item in the progress bar
        Animation a = AnimationUtils.loadAnimation(this, R.anim.view_fade_out);
        if (a != null) {
            a.setAnimationListener(new AnimationListener() {

                public void onAnimationStart(Animation animation) {
                    splashHolder.setVisibility(View.VISIBLE);
                }

                public void onAnimationEnd(Animation animation) {
                    splashHolder.setVisibility(View.INVISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                    // Do what ever you need, if not remove it.
                }

            });
        }
        splashHolder.startAnimation(a);
    }

    //assumes that all data validation is already done and it is safe to proceed:
    public void goToFragment(int index) {
        pager.setCurrentItem(index, true);
        obsProgress.update(index);
    }

    public void showSuccess() {
        //Logger.debug("RegistrationActivity :: showSuccess");

        //LinearLayout successPanel = (LinearLayout) findViewById(R.id.registration_success);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.view_fade_in);
        if (a != null) {
            a.setAnimationListener(new AnimationListener() {

                public void onAnimationStart(Animation animation) {
                    successHolder.setVisibility(View.VISIBLE);
                }

                public void onAnimationEnd(Animation animation) {
                    launchAppButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            launchAppButton.setOnClickListener(null); //take only one click
                            goToLoginActivity();
                        }
                    });
                }

                public void onAnimationRepeat(Animation animation) {
                    // Do what ever you need, if not remove it.
                }

            });
        }
        successHolder.startAnimation(a);

    }

    //assumes that all data validation is already done and it is safe to proceed:
    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        //prep some data here if necessary
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //only allow one activity to open
        startActivity(intent);
    }

    private void showActivityIndicator(String loadString) {

        activitySpinner.setMessage(loadString);
        activitySpinner.setCancelable(false);
        activitySpinner.show();
    }

    private void stopActivityIndicator() {
        //Logger.debug("HIDING ACTIVITY INDICATOR");

        this.runOnUiThread(new Runnable() {
            public void run() {

                if (activitySpinner != null) {
                    activitySpinner.hide();
                    activitySpinner.dismiss();
                }
            }
        });
    }

    private void showPopup(final Activity context, Point p) {

        int popupWidth = 500;
        LinearLayout fader = (LinearLayout) context.findViewById(R.id.fade_frame);
        fader.setBackgroundColor(Color.parseColor("#88000000"));

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        TextView helpMessage = (TextView) layout.findViewById(R.id.info_button_text);
        helpMessage.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_LABEL_FIELD));
        helpMessage.setTextSize(18f);
        helpMessage.setText(helpString);

        // Creating the PopupWindow

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(popupWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        helpMessage.measure(widthMeasureSpec, heightMeasureSpec);

        popup.setWidth(popupWidth);
        popup.setHeight(helpMessage.getMeasuredHeight() + 60);
        popup.setFocusable(true);


        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -popupWidth - 10;
        int OFFSET_Y = 0;

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {

                LinearLayout fader = (LinearLayout) context.findViewById(R.id.fade_frame);
                fader.setBackgroundColor(Color.parseColor("#00000000"));
            }
        });

    }

    public void showIndicator(String message) {

        showActivityIndicator(message);
    }

    public void stopIndicator() {
        stopActivityIndicator();
    }

    // for registration fragment viewpager
    private class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> pageList;


        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
            super(fm);
            pageList = pages;
        }

        @Override
        public int getCount() {
            //return pageList.size();
            return pageList.size();
        }

        @Override
        public Fragment getItem(int position) {
            //Logger.debug("FragmentPagerAdapter :: getItem - " + position);
            return pageList.get(position);
        }
    }

}
