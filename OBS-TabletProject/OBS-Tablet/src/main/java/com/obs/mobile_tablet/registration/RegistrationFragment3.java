package com.obs.mobile_tablet.registration;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.RegistrationActivity;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

public class RegistrationFragment3 extends RegistrationFragment {
    private EditText question1;
    private EditText question2;

    public static RegistrationFragment3 newInstance(int num) {
        RegistrationFragment3 f = new RegistrationFragment3();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    protected Boolean validateInput(Editable editable) {
        //Logger.debug("RegistrationFragment :: validateInputs");
        int length = editable.toString().length();

        Boolean valid = true;
        if(question1.getText().length()==0) {
            question1.setActivated(false);
            valid = false;
        } else {
            question1.setActivated(true);
        }
        if(question2.getText().length()==0) {
            question2.setActivated(false);
            valid = false;
        } else {
            question2.setActivated(true);
        }

        submitButton.setEnabled(valid);

        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Logger.debug("RegistrationFragment3 :: onCreate");
        super.onCreate(savedInstanceState);
        currentIndex = 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_fragment_3, container, false);

        //skip button for testing. can be turned on in RegistrationActivity:
        skipButton = (Button) v.findViewById(R.id.skip_button);
        RegistrationActivity parent = (RegistrationActivity) getActivity();
        if (parent.SHOW_SKIP_BUTTONS) {
            skipButton.setVisibility(View.VISIBLE);
            enableSkipClick();
        }

        question1 = (EditText) v.findViewById(R.id.question1Field);
        question2 = (EditText) v.findViewById(R.id.question2Field);
        submitButton = (Button) v.findViewById(R.id.submit_button);
        restartButton = (Button) v.findViewById(R.id.restart_button);
        attachRestartListener();

        //add them to the array for the tab buttons:
        tabbableInputs.add(question1);
        tabbableInputs.add(question2);
        //tabbableInputs.add(submitButton); //no worky

        enableSubmitClick();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                validateInput(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };

        question1.addTextChangedListener(textWatcher);
        question2.addTextChangedListener(textWatcher);

        //INITIALIZE ALL CUSTOM FONTS:
        question1.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        question2.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        submitButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        restartButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));

        TextView question1Label = (TextView) v.findViewById(R.id.question1Label);
        TextView question2Label = (TextView) v.findViewById(R.id.question2Label);

        question1Label.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));
        question2Label.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //clear all fields:
        question1.setText("");
        question2.setText("");
    }

    @Override
    public void onSubmitClicked() {

        mCallback.onAlert(RegistrationAlertBox.TYPE_INFO, getResources().getString(R.string.validating_user));
//        mCallback.showIndicator("Validating User Info...");
        setupFacade.registerTokenAndObs(getQuestion1String(), getQuestion2String(), new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
//                mCallback.stopIndicator();
                mCallback.onFormSuccess(currentIndex);
            }

            @Override
            public void onTaskFailure(String reason) {
//                mCallback.stopIndicator();
                mCallback.onAlert(RegistrationAlertBox.TYPE_WARNING, reason);
            }
        });
    }

    public String getQuestion1String() {
        String question = question1.getText().toString();
        return question;
    }
     public String getQuestion2String() {

        String question = question2.getText().toString();
        return question;
    }
}