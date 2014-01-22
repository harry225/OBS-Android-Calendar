package com.obs.mobile_tablet.accounts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.obs.mobile_tablet.MainActivity;
import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.components.OBSFragment;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.BalanceObj;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.datamodel.reporting.PaymentThumbnailObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionsObj;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.navigation.NavigationData;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.ForApplication;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by jaimiespetseris on 1/16/14.
 */
public class AccountSummaryFragment extends OBSFragment implements AccountsTransactionPreviewAdapter.OnItemListener {

    private TextView viewTitle;
    private Button backButton;
    private AccountsBalanceHistoryChart balanceHistoryChart;
    private AccountsTransactionBarChart transactionsBarChart;
    private AccountsPercentTransactionsChart transactionsPieChart;
    private Button pieChartButton;
    private TextView paymentsTitle;
    private Button viewAllPaymentsButton;
    private LinearLayout paymentsHeader;
    private ListView paymentsPreviewList;
    private TextView noPaymentsText;
    private TextView transactionsTitle;
    private Button viewAllTransactionsButton;
    private LinearLayout transactionsHeader;
    private ListView transactionsPreviewList;
    private TextView noTransactionsText;
    private Button accountStatsButton;
    private Button makeDepositButton;
    private Button makePaymentButton;
    private Button positivePayButton;
    private Button stopPaymentButton;

    private AccountObj accountObj;
    private ArrayList<PaymentThumbnailObj> paymentList;
    private ArrayList<TransactionObj> transactionList;
    private int numRecentPayments = 0;
    private int numRecentTransactions = 0;

    @Inject
    InformationReportingFacade reportingFacade;

    @Inject
    @ForApplication
    OBSApplication application;


    //interface implementation:
    public void onCheckClicked(String pdfUrl) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        PDFDialogFragment newFragment = new PDFDialogFragment();
        newFragment.setUrlToLoad(pdfUrl);
        newFragment.show(fragmentManager, "dialog");
    }

    public static AccountSummaryFragment newInstance(int num) {
        AccountSummaryFragment f = new AccountSummaryFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    public void setAccount(AccountObj acctObj) {
        //refreshData(acctObj);
        accountObj = acctObj;
        Logger.debug("You made it to the account summary");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TestFlight.passCheckpoint("Account Details");

        View rootView = inflater.inflate(R.layout.accounts_summary_fragment, container, false);

        viewTitle = (TextView) rootView.findViewById(R.id.view_title);
        balanceHistoryChart = (AccountsBalanceHistoryChart) rootView.findViewById(R.id.balance_history_chart);
        transactionsBarChart = (AccountsTransactionBarChart) rootView.findViewById(R.id.transactions_bar_chart);
        transactionsPieChart = (AccountsPercentTransactionsChart) rootView.findViewById(R.id.percent_transactions_pie_chart);
        pieChartButton = (Button) rootView.findViewById(R.id.pie_chart_button);
        paymentsTitle = (TextView) rootView.findViewById(R.id.payments_header);
        viewAllPaymentsButton = (Button) rootView.findViewById(R.id.view_all_payments_button);
        noPaymentsText = (TextView) rootView.findViewById(R.id.no_payments_textview);
        paymentsHeader = (LinearLayout) rootView.findViewById(R.id.pay_header);
        paymentsPreviewList = (ListView) rootView.findViewById(R.id.payments_preview_list);
        transactionsTitle = (TextView) rootView.findViewById(R.id.transactions_header);
        viewAllTransactionsButton = (Button) rootView.findViewById(R.id.view_all_transactions_button);
        noTransactionsText = (TextView) rootView.findViewById(R.id.no_transactions_textview);
        transactionsHeader = (LinearLayout) rootView.findViewById(R.id.trans_header);
        transactionsPreviewList = (ListView) rootView.findViewById(R.id.transactions_preview_list);
        accountStatsButton = (Button) rootView.findViewById(R.id.acct_stats_button);
        makeDepositButton = (Button) rootView.findViewById(R.id.deposit_button);
        makePaymentButton = (Button) rootView.findViewById(R.id.payment_button);
        positivePayButton = (Button) rootView.findViewById(R.id.positive_pay_button);
        stopPaymentButton = (Button) rootView.findViewById(R.id.stop_payment_button);

        //references needed only for fonts:
        TextView payTypeHdr = (TextView) rootView.findViewById(R.id.pay_type_header);
        TextView payAmtHdr = (TextView) rootView.findViewById(R.id.pay_amt_header);
        TextView payDateHdr = (TextView) rootView.findViewById(R.id.pay_date_header);
        TextView transDateHdr = (TextView) rootView.findViewById(R.id.trans_date_header);
        TextView transCheckHdr = (TextView) rootView.findViewById(R.id.trans_check_header);
        TextView transDescHdr = (TextView) rootView.findViewById(R.id.trans_description_header);
        TextView transCreditHdr = (TextView) rootView.findViewById(R.id.trans_credit_header);
        TextView transDebitHdr = (TextView) rootView.findViewById(R.id.trans_debit_header);

        //TODO: wire in these buttons and make them do something ========
        accountStatsButton.setVisibility(View.INVISIBLE);
        makeDepositButton.setVisibility(View.INVISIBLE);
        positivePayButton.setVisibility(View.INVISIBLE);
        stopPaymentButton.setVisibility(View.INVISIBLE);

        //payments and transactions stuff:
        viewAllPaymentsButton.setVisibility(View.INVISIBLE);
        paymentsHeader.setVisibility(View.INVISIBLE);
        noPaymentsText.setVisibility(View.INVISIBLE);
        viewAllTransactionsButton.setVisibility(View.INVISIBLE);
        transactionsHeader.setVisibility(View.INVISIBLE);
        noTransactionsText.setVisibility(View.INVISIBLE);

        //set custom fonts:
        viewTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PAGE_TITLE));
        paymentsTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        viewAllPaymentsButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        transactionsTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        viewAllTransactionsButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        accountStatsButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        makeDepositButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        makePaymentButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        positivePayButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        stopPaymentButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        payTypeHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        payAmtHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        payDateHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        transDateHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        transCheckHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        transDescHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        transCreditHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));
        transDebitHdr.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));

        backButton = (Button) rootView.findViewById(R.id.back_button);
        backButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_BLACK_BUTTON));
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccountsFragment f = AccountsFragment.newInstance(1);
                ((OBSApplication) getActivity().getApplication()).inject(f);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
                ft.commit();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showActivityIndicator("Fetching Account...");
    }

    public void onViewAllTransactionsClicked(AccountObj acctObj) {
        AccountsDetailFragment f = AccountsDetailFragment.newInstance(1);
        application.inject(f);
        f.setAccount(acctObj);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
        ft.commit();
    }

    protected void onFragmentAnimationEnd() {
        refreshData();
    }

    private void refreshData() {
        reportingFacade.getAccount(accountObj, new Callback<AccountObj>() {
            @Override
            public void onTaskCompleted(AccountObj result) {
                accountObj = result; //replace the old AccountObj with the new one (contains all details for graphs, etc)
                if (accountObj.historicalBalances != null) {
                    ArrayList<BalanceObj> balances = (ArrayList) accountObj.historicalBalances;
                    balanceHistoryChart.setData(balances);
                }


                paymentList = (ArrayList) accountObj.getRecentPayments();
                if (paymentList != null) {
                    numRecentPayments = paymentList.size();
                }

                TransactionsObj transObj = accountObj.getTransactionDetails();
                ArrayList<GraphObj> graphs = transObj.getGraphs();

                GraphObj barChartData = graphs.get(0);
                transactionsBarChart.setData(barChartData);

                GraphObj pieChartData = graphs.get(1);
                transactionsPieChart.setData(pieChartData);

                transactionList = transObj.getTransactions();
                if (transactionList != null) {
                    numRecentTransactions = transactionList.size();
                }

                stopActivityIndicator();
                activateMe();
            }

            @Override
            public void onTaskFailure(String Reason) {
                //TODO: some error message
                stopActivityIndicator();
            }
        });
    }


    //do anything that is dependent on the data:
    private void activateMe() {

        viewTitle.setText(accountObj.displayName.toUpperCase());

        paymentsTitle.setText("Last "+ numRecentPayments + " Payments");
        if (numRecentPayments == 0) {
            viewAllPaymentsButton.setVisibility(View.INVISIBLE);
            paymentsHeader.setVisibility(View.INVISIBLE);
            noPaymentsText.setVisibility(View.VISIBLE);
        }
        else {
            viewAllPaymentsButton.setVisibility(View.VISIBLE);
            paymentsHeader.setVisibility(View.VISIBLE);
            noPaymentsText.setVisibility(View.INVISIBLE);

            AccountsPaymentsPreviewAdapter paymentsAdapter = new AccountsPaymentsPreviewAdapter(getActivity(),
                    R.layout.accounts_payments_preview_cell, paymentList);
            paymentsPreviewList.setAdapter(paymentsAdapter);

        }
        viewAllPaymentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPayments(v);
            }
        });

        transactionsTitle.setText("Last "+ numRecentTransactions + " Transactions");
        if (numRecentTransactions == 0) {
            viewAllTransactionsButton.setVisibility(View.INVISIBLE);
            transactionsHeader.setVisibility(View.INVISIBLE);
            noTransactionsText.setVisibility(View.VISIBLE);
        }
        else {
            viewAllTransactionsButton.setVisibility(View.VISIBLE);
            transactionsHeader.setVisibility(View.VISIBLE);
            noTransactionsText.setVisibility(View.INVISIBLE);

            AccountsTransactionPreviewAdapter transactionPreviewAdapter = new AccountsTransactionPreviewAdapter(getActivity(),
                    R.layout.accounts_transactions_preview_cell, transactionList, this);
            transactionsPreviewList.setAdapter(transactionPreviewAdapter);

        }
        viewAllTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewAllTransactionsClicked(accountObj);
            }
        });
        pieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewAllTransactionsClicked(accountObj);
            }
        });

        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPayments(v);
            }
        });
    }

    private void gotoPayments(View v) {
        MainActivity hostActivity = (MainActivity) v.getContext();
        hostActivity.onNavClicked(NavigationData.NAV_PAYMENTS);
    }

}
