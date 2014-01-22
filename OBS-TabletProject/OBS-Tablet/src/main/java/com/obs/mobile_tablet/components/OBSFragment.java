package com.obs.mobile_tablet.components;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.obs.mobile_tablet.accounts.BellItem;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

import com.obs.mobile_tablet.R;

/**
 * Created by jaimiespetseris on 1/20/14.
 */
public class OBSFragment extends Fragment {

    protected ProgressDialog activitySpinner = null;
    protected AlertBannersDropdown alertBannersPanel = null;
    protected ImageView alertBannersButton = null;


    protected void setupBanners(ArrayList<BellItem> banners) {
        //setup the alert banners:
        if (alertBannersButton != null && alertBannersPanel != null) {
            alertBannersPanel.setData(banners);
            alertBannersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertBannersPanel.toggleMe();

                }
            });
        }
    }

    protected void showActivityIndicator(String loadString) {
        //Logger.debug("OBSFragment :: showActivityIndicator");
        activitySpinner = new ProgressDialog(getActivity());
        activitySpinner.setMessage(loadString);
        activitySpinner.setCancelable(false);
        activitySpinner.show();
    }

    protected void stopActivityIndicator() {
        //Logger.debug("OBSFragment :: stopActivityIndicator");
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (activitySpinner != null) {
                    activitySpinner.hide();
                    activitySpinner.dismiss();
                }
            }
        });
    }

    protected void onFragmentAnimationStart() {
        //this just provides a hook for triggering actions. override in your subclass.
        //THIS ONLY FIRE IF YOU USE AN ANIMATED TRANSITION TO add OR replace THE FRAGMENT
    }

    protected void onFragmentAnimationRepeat() {
        //this just provides a hook for triggering actions. override in your subclass.
        //THIS ONLY FIRE IF YOU USE AN ANIMATED TRANSITION TO add OR replace THE FRAGMENT
    }

    protected void onFragmentAnimationEnd() {
        //this just provides a hook for triggering actions. override in your subclass.
        //THIS ONLY FIRE IF YOU USE AN ANIMATED TRANSITION TO add OR replace THE FRAGMENT
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        // HW layer support only exists on API 11+
        if (Build.VERSION.SDK_INT >= 11) {
            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            }

            if (animation != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        //Logger.debug("Animation started.");
                        onFragmentAnimationStart();
                    }

                    public void onAnimationRepeat(Animation animation) {
                        //Logger.debug("Animation repeating.");
                        onFragmentAnimationRepeat();
                    }

                    public void onAnimationEnd(Animation animation) {
                        //Logger.debug("Animation ended.");
                        onFragmentAnimationEnd();
                    }
                });
            }
        }

        return animation;
    }
}
