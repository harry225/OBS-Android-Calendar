package com.obs.mobile_tablet.accounts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.obs.mobile_tablet.R;

/**
 * Created by jaimiespetseris on 1/19/14.
 */
public class PDFDialogFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    private WebView webView;
    private ProgressDialog progressDialog;
    private String GoogleDocsPDFViewer = "https://docs.google.com/viewer?url="; //"http://docs.google.com/gview?embedded=true&url=";
    private String myUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View v = inflater.inflate(R.layout.pdf_dialog_fragment, container, false);

        webView = (WebView) v.findViewById(R.id.web_view);
        webView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);

        if (myUrl.length() > 0) { //make sure that the url has been set
            /*
            //following lines are to show the loader until downloading the pdf file for view.
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);
            progressDialog.setCancelable(false);
            webView.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    progressDialog.show();
                    view.loadUrl(url);

                    return true;
                }
                @Override
                public void onPageFinished(WebView view, final String url) {
                    progressDialog.dismiss();
                }
            });
            */

            webView.loadUrl(GoogleDocsPDFViewer + myUrl);
        }

        return v;
    }

    //must be called BEFORE "show" is called on the dialog:
    public void setUrlToLoad(String urlToLoad) {
        myUrl = urlToLoad;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
