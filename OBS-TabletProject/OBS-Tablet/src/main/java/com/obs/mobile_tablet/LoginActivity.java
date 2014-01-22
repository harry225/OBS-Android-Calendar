package com.obs.mobile_tablet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountsResponse;
import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.registration.ResetPasswordDialogFragment;
import com.obs.mobile_tablet.utils.Logger;
import com.testflightapp.lib.TestFlight;

import org.springframework.util.StringUtils;
import org.w3c.dom.Text;

import javax.inject.Inject;

public class LoginActivity extends ActionBarActivity {

    @Inject
    AuthenticationFacade authenticationFacade;

    @Inject
    SetupFacade setupFacade;

    @Inject
    InformationReportingFacade reportingFacade;


    Button forgotPinButton;
    Button loginButton;
    Button skipButton;

    TextView enterPinHeader;
    TextView pleaseRememberText;
    TextView companyId;
    EditText pin;
    Boolean showingAlert;

    final Boolean ALLOW_SKIP_LOGIN = false;

    private ProgressDialog activitySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OBSApplication)getApplication()).inject(this);

        setContentView(R.layout.login_activity);

        loginButton = (Button) findViewById(R.id.login_button);
        forgotPinButton = (Button) findViewById(R.id.forgot_pin_button);
        pin = (EditText) findViewById(R.id.pin_field);
        skipButton = (Button) findViewById(R.id.skip_button);

        if (ALLOW_SKIP_LOGIN) {
            skipButton.setVisibility(View.VISIBLE);
            skipButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    goToMainActivity();
                }
            });
        }

        //INITIALIZE CUSTOM FONTS:
        loginButton.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_PRIMARY_BUTTON));
        forgotPinButton.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_BASE));

        enterPinHeader = (TextView)findViewById(R.id.enter_pin);
        pleaseRememberText = (TextView)findViewById(R.id.please_remember_text);
        companyId = (TextView)findViewById(R.id.pin_field);

        enterPinHeader.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_HEADER_1));
        pleaseRememberText.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_BASE));
        companyId.setTypeface(FontFace.GetFace(getApplicationContext(), FontFace.FONT_BASE));

        activitySpinner = new ProgressDialog(this);
        showingAlert = false;

        enableForgotPinButton();


    }

    @Override
    public void onStart() {
        super.onStart();
        enableLoginButton();
    }

    private void enableForgotPinButton() {
        forgotPinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //forgotPinButton.setOnClickListener(null); //take only one click

                ResetPasswordDialogFragment dialogFrag = ResetPasswordDialogFragment.newInstance(1);
                ((OBSApplication) getApplication()).inject(dialogFrag);

                dialogFrag.show(getSupportFragmentManager(), "ResetPasswordDialogFragment");
            }
        });
    }

    private void enableLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                loginButton.setOnClickListener(null); //take only one click
//                goToMainActivity(); //Always work for now until the setups working

              showingAlert = true;
              showActivityIndicator("Authenticating PIN...");
              setupFacade.login(pin.getText().toString(), new Callback() {
                    @Override
                    public void onTaskCompleted(Object result) {
                        TestFlight.passCheckpoint("Logged In");
                        stopActivityIndicator();
                        goToMainActivity();
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        //TODO: SHOW A FAILURE MESSAGE

                        stopActivityIndicator();
                        enableLoginButton();
                        showAlertDialog(Reason);

                    }
                });
            }
        });
    }

    public void goToRegistrationActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        //prep some data here if necessary
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //only allow one activity to open
        startActivity(intent);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //prep some data here if necessary
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //only allow one activity to open
        startActivity(intent);
    }

    private Boolean validateLogin() {
        return true;
    }

    private void showActivityIndicator(String loadString) {

        activitySpinner.setMessage(loadString);
        activitySpinner.setCancelable(false);
        activitySpinner.show();
    }

    private void stopActivityIndicator() {
        //Logger.debug("HIDING ACTIVITY INDICATOR");

        this.runOnUiThread(new Runnable() {
            public void run() {

                if (activitySpinner != null) {
                    activitySpinner.hide();
                    activitySpinner.dismiss();
                }
            }
        });
    }

    private void showAlertDialog(String reason) {

        final String message = reason;
        if(showingAlert) {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Unsuccessful")
                            .setMessage(message)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete'
                                    showingAlert = false;
                                }
                            })
                            .show();

                }
            });
        }
    }
}
