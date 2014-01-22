package com.obs.mobile_tablet.registration;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.RegistrationActivity;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.inject.Inject;

public class RegistrationFragment1 extends RegistrationFragment {

    private EditText companyID;
    private EditText userID;
    private EditText actKey1;
    private EditText actKey2;
    private EditText actKey3;
    private EditText actKey4;
    private EditText actKey5;

    private int currentKeyboardMode = InputType.TYPE_CLASS_TEXT;

    public static RegistrationFragment1 newInstance(int num) {
        RegistrationFragment1 f = new RegistrationFragment1();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onSubmitClicked() {

        mCallback.onAlert(RegistrationAlertBox.TYPE_INFO, getResources().getString(R.string.registering_account));
//        mCallback.showIndicator("");
        setupFacade.authenticate(getCompanyId(), getUserName(), getActivationKey(), new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
                RegistrationFragment2.updatePinDescription(setupFacade.getPinDescription());
                RegistrationFragment2.updatePinPattern(setupFacade.getPinRegex());
                //mCallback.stopIndicator();
                mCallback.onFormSuccess(currentIndex);
//                proceed();
            }

            @Override
            public void onTaskFailure(String reason) {
                //mCallback.stopIndicator();
                mCallback.onAlert(RegistrationAlertBox.TYPE_WARNING, reason);
            }
        });
    }

    @Override
    protected Boolean validateInput(Editable editable) {
        //Logger.debug("RegistrationFragment :: validateInputs");
        int length = editable.toString().length();
        if(length == 0 && submitButton.isEnabled()) {
            submitButton.setEnabled(false);
        }

        Boolean valid = true;
        if(companyID.getText().length()==0) {
            companyID.setActivated(false);
            valid = false;
        } else {
            companyID.setActivated(true);
        }

        if(userID.getText().length()==0) {
            valid = false;
            userID.setActivated(false);
        } else {
            userID.setActivated(true);
        }

        if(actKey1.getText().length()<5) {
            valid = false;
            actKey1.setActivated(false);
        } else {
            actKey1.setActivated(true);
        }

        if(actKey2.getText().length()<5) {
            valid = false;
            actKey2.setActivated(false);
        } else {
            actKey2.setActivated(true);
        }

        if(actKey3.getText().length()<5) {
            valid = false;
            actKey3.setActivated(false);
        } else {
            actKey3.setActivated(true);
        }

        if(actKey4.getText().length()<5) {
            valid = false;
            actKey4.setActivated(false);
        } else {
            actKey4.setActivated(true);
        }

        if(actKey5.getText().length()<5) {
            valid = false;
            actKey5.setActivated(false);
        } else {
            actKey5.setActivated(true);
        }

        submitButton.setEnabled(valid);

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Logger.debug("RegistrationFragment1 :: onCreate");
        super.onCreate(savedInstanceState);
        currentIndex = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.registration_fragment_1, container, false);

        //skip button for testing. can be turned on in RegistrationActivity:
        skipButton = (Button) v.findViewById(R.id.skip_button);
        RegistrationActivity parent = (RegistrationActivity) getActivity();
        if (parent.SHOW_SKIP_BUTTONS) {
            skipButton.setVisibility(View.VISIBLE);
            enableSkipClick();
        }

        companyID = (EditText) v.findViewById(R.id.companyIDField);
        userID = (EditText) v.findViewById(R.id.editText);
        actKey1 = (EditText) v.findViewById(R.id.actKey1);
        actKey2 = (EditText) v.findViewById(R.id.actKey2);
        actKey3 = (EditText) v.findViewById(R.id.actKey3);
        actKey4 = (EditText) v.findViewById(R.id.actKey4);
        actKey5 = (EditText) v.findViewById(R.id.actKey5);
        submitButton = (Button) v.findViewById(R.id.submit_button);

        //add them to the array for the tab buttons:
        tabbableInputs.add(companyID);
        tabbableInputs.add(userID);
        tabbableInputs.add(actKey1);
        tabbableInputs.add(actKey2);
        tabbableInputs.add(actKey3);
        tabbableInputs.add(actKey4);
        tabbableInputs.add(actKey5);
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

                if(actKey1.isFocused() && actKey1.getText().toString().length() >= 5) {
                    actKey2.requestFocus();
                } else if(actKey2.isFocused() && actKey2.getText().toString().length() >= 5) {
                    actKey3.requestFocus();
                } else if(actKey3.isFocused() && actKey3.getText().toString().length() >= 5) {
                    actKey4.requestFocus();
                } else if(actKey4.isFocused() && actKey4.getText().toString().length() >= 5) {
                    actKey5.requestFocus();
                }
            }
        };

        companyID.addTextChangedListener(textWatcher);
        userID.addTextChangedListener(textWatcher);
        actKey1.addTextChangedListener(textWatcher);
        actKey2.addTextChangedListener(textWatcher);
        actKey3.addTextChangedListener(textWatcher);
        actKey4.addTextChangedListener(textWatcher);
        actKey5.addTextChangedListener(textWatcher);

        //INITIALIZE ALL CUSTOM FONTS:
        companyID.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        userID.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        actKey1.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        actKey2.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        actKey3.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        actKey4.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        actKey5.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        submitButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));

        TextView companyIdLabel = (TextView) v.findViewById(R.id.companyIDLabel);
        TextView userIdLabel = (TextView) v.findViewById(R.id.userIDLabel);
        TextView activationKeyLabel = (TextView) v.findViewById(R.id.activationKeyLabel);

        companyIdLabel.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));
        userIdLabel.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));
        activationKeyLabel.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //clear all fields:
        companyID.setText("");
        userID.setText("");
        actKey1.setText("");
        actKey2.setText("");
        actKey3.setText("");
        actKey4.setText("");
        actKey5.setText("");

/**     There is no reason I am aware of to call authenticate before the user enters in any credentials
        setupFacade.authenticate(getCompanyId(), getUserName(), getActivationKey(), new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
                proceed();
            }

            @Override
            public void onTaskFailure(String reason) {
                mCallback.onAlert(RegistrationAlertBox.TYPE_WARNING, reason);
            }
        });
        */
    }

    public String getActivationKey() {
        return actKey1.getText().toString() + actKey2.getText().toString() + actKey3.getText().toString() + actKey4.getText().toString() + actKey5.getText().toString();
    }

    public String getUserName() {
        return userID.getText().toString();
    }

    public String getCompanyId() {
        return companyID.getText().toString();
    }

}