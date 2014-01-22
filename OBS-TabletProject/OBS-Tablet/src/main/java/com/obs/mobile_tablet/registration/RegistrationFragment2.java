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

public class RegistrationFragment2 extends RegistrationFragment {

    private EditText pin1;
    private EditText pin2;
    private TextView pin1RunnerText;
    private TextView pin2RunnerText;
    static TextView pinDescription;
    static String pattern;

    public static RegistrationFragment2 newInstance(int num) {
        RegistrationFragment2 f = new RegistrationFragment2();

        pattern = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=[\\S]+$).{5,10})";

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
        if(length == 0 && submitButton.isEnabled()) {
            submitButton.setEnabled(false);
        }

        Boolean valid = true;
        if(pin1.getText().length()==0) {
            pin1.setActivated(false);
            pin1RunnerText.setTextColor(getResources().getColor(R.color.obsRed));
            pin1RunnerText.setText("PIN does not meet requirements");
            valid = false;
        } else {

            if(pin1.getText().toString().matches(pattern)) {
                pin1.setActivated(true);
                pin1RunnerText.setTextColor(getResources().getColor(R.color.obsGreen));
                pin1RunnerText.setText("PIN meets requirements");
            } else {
                valid = false;
                pin1.setActivated(false);
                pin1RunnerText.setTextColor(getResources().getColor(R.color.obsRed));
                pin1RunnerText.setText("PIN does not meet requirements");
            }
        }

        if(pin2.getText().length()==0) {
            valid = false;
            pin2.setActivated(false);
            pin2RunnerText.setTextColor(getResources().getColor(R.color.obsRed));
            pin2RunnerText.setText("PIN does not meet requirements");
        } else {
            if(pin1.getText().toString().equals(pin2.getText().toString()) ) {

                if(valid) {
                    pin2.setActivated(true);
                    pin2RunnerText.setTextColor(getResources().getColor(R.color.obsGreen));
                    pin2RunnerText.setText("PINs match");
                } else {
                    pin2.setActivated(false);
                    pin2RunnerText.setTextColor(getResources().getColor(R.color.obsRed));
                    pin2RunnerText.setText("PIN does not meet requirements");
                }

            } else {
                valid = false;
                pin2.setActivated(false);
                pin2RunnerText.setTextColor(getResources().getColor(R.color.obsRed));
                pin2RunnerText.setText("PINs do not match");
            }
        }

        submitButton.setEnabled(valid);

        return true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Logger.debug("RegistrationFragment2 :: onCreate");
        super.onCreate(savedInstanceState);
        currentIndex = 1;
    }

    public static void updatePinDescription(String text) {
        if(text != null && !"".equalsIgnoreCase(text)){
            pinDescription.setText(text);
        }
    }

    public static void updatePinPattern(String newPattern){
        if(newPattern != null && !"".equalsIgnoreCase(newPattern)){
            pattern = newPattern;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_fragment_2, container, false);

        //skip button for testing. can be turned on in RegistrationActivity:
        skipButton = (Button) v.findViewById(R.id.skip_button);
        RegistrationActivity parent = (RegistrationActivity) getActivity();
        if (parent.SHOW_SKIP_BUTTONS) {
            skipButton.setVisibility(View.VISIBLE);
            enableSkipClick();
        }

        String serverDefinedPinDescription = null;
        try {
            serverDefinedPinDescription = setupFacade.getPinDescription();
        }
        catch(Exception e) {
            //nothing
        }
        pinDescription = (TextView) v.findViewById(R.id.pin_descriptive_text);
        if(serverDefinedPinDescription != null && !"".equalsIgnoreCase(serverDefinedPinDescription)){
            pinDescription.setText(serverDefinedPinDescription);
        }
        pin1 = (EditText) v.findViewById(R.id.enter_pin);
        pin2 = (EditText) v.findViewById(R.id.reenter_pin);
        pin1RunnerText = (TextView)v.findViewById(R.id.enter_pin_runner_text);
        pin2RunnerText = (TextView)v.findViewById(R.id.reenter_pin_runner_text);
        submitButton = (Button) v.findViewById(R.id.submit_button);
        restartButton = (Button) v.findViewById(R.id.restart_button);
        attachRestartListener();

        //add them to the array for the tab buttons:
        tabbableInputs.add(pin1);
        tabbableInputs.add(pin2);
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

        pin1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    pin1RunnerText.setAlpha(1);
                    validateInput(pin1.getText());

                } else {

                    pin1RunnerText.setAlpha(0);
                }
            }
        });

        pin2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    pin2RunnerText.setAlpha(1);
                    validateInput(pin2.getText());

                } else {

                    pin2RunnerText.setAlpha(0);
                }
            }
        });

        pin1.addTextChangedListener(textWatcher);
        pin2.addTextChangedListener(textWatcher);

        //INITIALIZE ALL CUSTOM FONTS:
        pin1.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        pin2.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));
        submitButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        restartButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));

        TextView enterPINLabel = (TextView) v.findViewById(R.id.enterPINLabel);
        TextView reenterPINLabel = (TextView) v.findViewById(R.id.reenterPINLabel);

        enterPINLabel.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));
        reenterPINLabel.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));

        pin1RunnerText.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));
        pin2RunnerText.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_LABEL));

        pin1RunnerText.setAlpha(0);
        pin2RunnerText.setAlpha(0);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //clear all fields:
        pin1.setText("");
        pin2.setText("");
    }

    @Override
    public void onSubmitClicked() {

        mCallback.onAlert(RegistrationAlertBox.TYPE_INFO, getResources().getString(R.string.registering_pin));
        //mCallback.showIndicator("Registering PIN...");
        setupFacade.createRegistration(getPin(), new Callback<String>() {
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

    public String getPin() {
        String pin = pin1.getText().toString();
        return pin;
    }
}