package com.obs.mobile_tablet.registration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.RegistrationActivity;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;

import javax.inject.Inject;


public class ResetPasswordDialogFragment extends DialogFragment {

    @Inject
    SetupFacade setupFacade;
    protected View dialogView;
    protected EditText resetField;
    protected Button resetButton;

    static public ResetPasswordDialogFragment newInstance(int num) {
        ResetPasswordDialogFragment f = new ResetPasswordDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = inflater.inflate(R.layout.login_reset_password_dialog, null);
        builder.setView(dialogView);

        builder.setPositiveButton("reset", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //will be overridden in onStart, but this needs to exist for older APIs
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ResetPasswordDialogFragment.this.getDialog().cancel();
            }
        });

        AlertDialog ad = builder.create();
        //ad.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        return ad;
    }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point

        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            //d.getWindow().setBackgroundDrawableResource(R.drawable.reset_password_dialog_bg);
            resetField = (EditText) dialogView.findViewById(R.id.reset_field);
            //set custom font:
            resetField.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_INPUT_FIELD));

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
            resetField.addTextChangedListener(textWatcher);

            final Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            assert positiveButton != null;
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    String value = resetField.getText().toString();
                    if(value.length() > 0) {
                        if (value.trim().equalsIgnoreCase("reset")) {
                            positiveButton.setEnabled(false);
                            setupFacade.deleteRegistration(new Callback<String>() {
                                @Override
                                public void onTaskCompleted(String result) {
//                                    dismiss();
                                    goToRegistrationActivity();
                                }
                                @Override
                                public void onTaskFailure(String Reason) {
//                                    dismiss();
                                    goToRegistrationActivity();
                                }
                            });

                        }
                    }
                    if(wantToCloseDialog) {
                        dismiss();
                    }
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }

    protected Boolean validateInput(Editable editable) {
        //Logger.debug("ResetPasswordDialogFragment :: validateInputs");
        Boolean valid = true;
        String fieldValue = resetField.getText().toString();
        if(fieldValue.length() > 0) {
            if (fieldValue.trim().equalsIgnoreCase("reset")) {
                resetField.setActivated(true);
            }
            valid = true;
        } else {
            resetField.setActivated(false);
            valid = false;
        }
        if (valid) {
            //resetButton.setEnabled(true);
        }
        else {
            //resetButton.setEnabled(false);
        }
        return valid;
    }

    public void goToRegistrationActivity() {
        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
        //prep some data here if necessary
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //only allow one activity to open
        startActivity(intent);
    }
}