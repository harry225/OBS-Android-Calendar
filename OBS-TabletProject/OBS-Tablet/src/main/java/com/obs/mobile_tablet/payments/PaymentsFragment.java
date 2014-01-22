package com.obs.mobile_tablet.payments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.components.OBSFragment;
import com.obs.mobile_tablet.facade.PaymentReportingFacade;
import com.obs.mobile_tablet.utils.FontFace;

import javax.inject.Inject;


public class PaymentsFragment extends OBSFragment {

    @Inject
    PaymentReportingFacade paymentReportingFacade;

    ViewPager viewPager;
    PagerAdapter pagerAdapter;


    public static PaymentsFragment newInstance(int num) {
        PaymentsFragment f = new PaymentsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.payments_fragment, container, false);

        viewPager = (ViewPager)rootView.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //SET CUSTOM FONTS:
        TextView viewTitle = (TextView) rootView.findViewById(R.id.view_title);
        viewTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PAGE_TITLE));



        //================ dev stuff: ==================
        Button testDetailButton = (Button) rootView.findViewById(R.id.test_detail_button);
        testDetailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PaymentDetailsFragment f = PaymentDetailsFragment.newInstance(1);
                //((OBSApplication) getActivity().getApplication()).inject(f);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
                ft.commit();
            }
        });
        //================ dev stuff: ===================



        return rootView;
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public float getPageWidth(int position)
        {
            if (position == 0)
            {
                return 0.97f;
            }

            if (position == 1)
            {
                return 0.97f;
            }
            return 1f;
        }

        @Override
        public Fragment getItem(int position) {
            return new FutureFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}