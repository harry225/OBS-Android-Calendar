package com.obs.mobile_tablet.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.components.OBSFragment;
import com.obs.mobile_tablet.utils.FontFace;


public class MessagesFragment extends OBSFragment {

    public static MessagesFragment newInstance(int num) {
        MessagesFragment f = new MessagesFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messages_fragment, container, false);

        //SET CUSTOM FONTS:
        TextView viewTitle = (TextView) rootView.findViewById(R.id.view_title);
        viewTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PAGE_TITLE));

        return rootView;
    }
}