package com.obs.mobile_tablet.registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.View;
import android.widget.Button;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.RegistrationActivity;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.BannersReceivedEvent;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;


public class RegistrationFragment extends Fragment {

    @Inject
    SetupFacade setupFacade;

    public OnSubmitListener mCallback;
    public String infoText;
    protected int currentIndex = 0;
    protected Button submitButton;
    protected Button skipButton;
    protected Button restartButton;
    private AlertDialog ad;
    protected ArrayList<View> tabbableInputs = new ArrayList<View>();


    // Container Activity must implement this interface
    public interface OnSubmitListener {
        public void onFormSubmitted(int formNum);
        public void onFormSuccess(int formNum);
        public void onAlert(int type, String message);
        public void showIndicator(String message);
        public void stopIndicator();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSubmitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitListener");
        }
    }


    public void tabPrev() {
        int numInputs = tabbableInputs.size();
        if (numInputs > 1) {
            int currentIndex = -1;
            for (int i=0; i<numInputs; i++) {
                if (tabbableInputs.get(i).hasFocus()) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex >= 0) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = numInputs - 1;
                }
                tabbableInputs.get(currentIndex).requestFocus();
            }
        }
    }

    public void tabNext() {
        int numInputs = tabbableInputs.size();
        if (numInputs > 1) {
            int currentIndex = -1;
            for (int i=0; i<numInputs; i++) {
                if (tabbableInputs.get(i).hasFocus()) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex >= 0) {
                currentIndex++;
                if (currentIndex >= numInputs) {
                    currentIndex = 0;
                }
                tabbableInputs.get(currentIndex).requestFocus();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Logger.debug("RegistrationFragment1 :: onCreate");
        super.onCreate(savedInstanceState);
    }

    //code stub: add local form validation code here
    protected Boolean validateInput(Editable editable) {
        //Logger.debug("RegistrationFragment :: validateInputs");
        return true;
    }

    public void onSubmitClicked() {
        //TODO: add calls to data service here
        if (true) {
            proceed();
        }
        else {
            showUserDataError();
        }
    }

    public void onSkipClicked() {
        mCallback.onFormSubmitted(currentIndex);
        startTestTimer();
    }

    public void onRestartClicked() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        setupFacade.deleteRegistration(new Callback() {
                            @Override
                            public void onTaskCompleted(Object result) {
                                goToRegistrationActivity();
                            }
                            @Override
                            public void onTaskFailure(String Reason) {
                                goToRegistrationActivity();
                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(getResources().getString(R.string.restart_registration_alert)).setPositiveButton("Restart", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();

    }

    protected void attachRestartListener(){
        try {
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onRestartClicked();
                }
            });
        }
        catch(Exception e) {
            //do nothing. the restartButton isn't properly initialized
        }
    }

    protected void proceed() {
        mCallback.onFormSubmitted(currentIndex);
        //startTestTimer();
    }

    //code stub: override in subclass
    protected void enableSubmitClick() {
        try {

            submitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //submitButton.setOnClickListener(null);
                    onSubmitClicked();
                }
            });
            submitButton.setEnabled(true);
        }
        catch(Exception e) {
            //do nothing. the submitButton isn't properly initialized
        }
    }

    //when skip button is showing, this will skip through the reg steps without validating anything:
    protected void enableSkipClick() {
        try {

            skipButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //submitButton.setOnClickListener(null);
                    onSkipClicked();
                }
            });
            skipButton.setEnabled(true);
        }
        catch(Exception e) {
            //do nothing. the submitButton isn't properly initialized
        }
    }

    public void goToRegistrationActivity() {
        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
        //prep some data here if necessary
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //only allow one activity to open
        this.getActivity().finish();
        startActivity(intent);
    }

//================== TEST HARNESS JUNK ===========================
    Handler timerHandler = new Handler();
    long startTime = 0;

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;

            if(millis > 1500) {
                stopTestTimer();
            } else {
                timerHandler.postDelayed(this, 500);
            }
        }
    };

    private void startTestTimer() {

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        //runs without a timer by reposting this handler at the end of the runnable
    }

    private void stopTestTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        Random r = new Random();
        int result= 4;//r.nextInt(8);
        switch(result) {
            case 0:
                mCallback.onAlert(RegistrationAlertBox.TYPE_INFO, "INFO TEST");
                break;
            case 1:
                mCallback.onAlert(RegistrationAlertBox.TYPE_WARNING, "WARNING TEST");
                break;
            case 2:
                mCallback.onAlert(RegistrationAlertBox.TYPE_SUCCESS, "SUCCESS TEST");
                break;
            default:
                mCallback.onFormSuccess(currentIndex);
                break;
        }
    }

    //================== TEST HARNESS JUNK ===========================

    protected void showUserDataError() {
        Logger.debug("validation failed");

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
        adBuilder.setTitle("VALIDATION FAILED");
        adBuilder.setMessage("You must fix your input before proceeding");
        adBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked CANCEL button
                ad.cancel();
            }
        });
        ad = adBuilder.create();
        adBuilder.show();

        enableSubmitClick(); //re-enable the button
    }
}
