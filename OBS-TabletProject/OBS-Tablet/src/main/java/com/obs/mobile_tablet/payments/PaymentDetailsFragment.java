package com.obs.mobile_tablet.payments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.accounts.AccountsFragment;
import com.obs.mobile_tablet.components.OBSFragment;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

/**
 * Created by jaimiespetseris on 1/21/14.
 */
public class PaymentDetailsFragment extends OBSFragment{


    Button backButton;
    TextView viewTitle;


    public static PaymentDetailsFragment newInstance(int num) {
        PaymentDetailsFragment f = new PaymentDetailsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    public void setData() {
        Logger.debug("PaymentDetailsFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TestFlight.passCheckpoint("Account Details");

        View rootView = inflater.inflate(R.layout.payment_details_fragment, container, false);

        viewTitle = (TextView) rootView.findViewById(R.id.view_title);

        backButton = (Button) rootView.findViewById(R.id.back_button);
        backButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_BLACK_BUTTON));
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PaymentsFragment f = PaymentsFragment.newInstance(1);
                ((OBSApplication) getActivity().getApplication()).inject(f);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
                ft.commit();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showActivityIndicator("Fetching Payment...");
    }

    protected void onFragmentAnimationEnd() {
        refreshData();
    }

    private void refreshData() {
        stopActivityIndicator();

    }

    //do anything that is dependent on the data:
    private void activateMe() {

    }

}
