package com.obs.mobile_tablet.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

/**
 * Created by jaimiespetseris on 1/2/14.
 */
public class MainNavigation extends Fragment {

    public OnNavListener mCallback;

    TranslateAnimation sliderAnimation;

    private Button accountsButton;
    private Button paymentsButton;
    private Button messagesButton;
    private RelativeLayout accountsWrapper;
    private RelativeLayout paymentsWrapper;
    private RelativeLayout messagesWrapper;
    private ImageView navHighlighter;
    private int currentLocation = 0;

    // Container Activity must implement this interface
    public interface OnNavListener {
        public void onNavClicked(int navIndex);
    }

    public static MainNavigation newInstance(int num) {
        MainNavigation f = new MainNavigation();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNavListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.main_navigation, container, false);

        accountsButton = (Button) v.findViewById(R.id.accounts_button);
        paymentsButton = (Button) v.findViewById(R.id.payments_button);
        messagesButton = (Button) v.findViewById(R.id.messages_button);
        accountsWrapper = (RelativeLayout) v.findViewById(R.id.accounts_button_wrapper);
        paymentsWrapper = (RelativeLayout) v.findViewById(R.id.payments_button_wrapper);
        messagesWrapper = (RelativeLayout) v.findViewById(R.id.messages_button_wrapper);

        navHighlighter = (ImageView)v.findViewById(R.id.nav_highlighter);

        accountsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onNavClicked(NavigationData.NAV_ACCOUNTS);
            }
        });

        paymentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onNavClicked(NavigationData.NAV_PAYMENTS);
            }
        });

        messagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onNavClicked(NavigationData.NAV_MESSAGES);
            }
        });

        return v;
    }

    //always called from parent activity!!!
    public void setCurrent(int navIndex) {
        int newPosition = 0;
        switch (navIndex) {
            case NavigationData.NAV_ACCOUNTS:
                newPosition = accountsWrapper.getTop();
                break;
            case NavigationData.NAV_PAYMENTS:
                newPosition = paymentsWrapper.getTop();
                break;
            case NavigationData.NAV_MESSAGES:
                newPosition = messagesWrapper.getTop();
                break;
        }
        sliderAnimation = new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,currentLocation,
                Animation.ABSOLUTE,newPosition);
        sliderAnimation.setDuration(250);
        sliderAnimation.setFillAfter(true);
        sliderAnimation.setFillEnabled(true);
        navHighlighter.startAnimation(sliderAnimation);
        currentLocation = newPosition;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Logger.debug("MainNavigation :: onCreate");
        super.onCreate(savedInstanceState);
    }

}
