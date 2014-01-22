package com.obs.mobile_tablet.bulletins;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.utils.FontFace;


public class BulletinsFragment extends Fragment {

    public static BulletinsFragment newInstance(int num) {
        BulletinsFragment f = new BulletinsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bulletins_fragment, container, false);

        //SET CUSTOM FONTS:
        TextView viewTitle = (TextView) rootView.findViewById(R.id.view_title);
        viewTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PAGE_TITLE));

        return rootView;
    }
}
