package com.obs.mobile_tablet.accounts;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.components.AlertBannersDropdown;
import com.obs.mobile_tablet.components.OBSFragment;
import com.obs.mobile_tablet.components.PullToRefreshListView;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.AccountTotalObj;
import com.obs.mobile_tablet.datamodel.reporting.AccountTypeObj;
import com.obs.mobile_tablet.datamodel.reporting.BalanceObj;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountTotalsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionGroup;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionSearch;
import com.obs.mobile_tablet.datamodel.reporting.TransactionsObj;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.ForApplication;
import com.obs.mobile_tablet.utils.Logger;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

public class AccountsFragment extends OBSFragment implements AccountsListAdapter.OnItemListener {


    private AccountsListView mainListView;
    private ListView alerts_list;
    private ArrayList<BellItem> alertItems;
    private AccountsListAdapter mainAdapter;

    //keep a reference to this, so we can inject the graphs whenever we want:
    private ListItemGraphs accountTotalGraphs;
    private ArrayList<Object> displayItems = new ArrayList<Object>();

    private View currentOpenListItem = null;
    ArrayList<AccountObj> accountObjs;

    @Inject
    InformationReportingFacade reportingFacade;

    @Inject
    @ForApplication
    OBSApplication application;


    //receives an object from a clicked list item:
    public void onItemClicked(AccountObj acctObj) {
        AccountSummaryFragment f = AccountSummaryFragment.newInstance(1);
        application.inject(f);
        f.setAccount(acctObj);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
        ft.commit();
    }

    //interface implementation from AccountsListAdapter:
    public void onViewAccountSummary(AccountObj acctObj) {
        AccountSummaryFragment f = AccountSummaryFragment.newInstance(1);
        application.inject(f);
        f.setAccount(acctObj);

//        application.inject(f);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
        ft.commit();
    }

    public static AccountsFragment newInstance(int num) {
        AccountsFragment f = new AccountsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.accounts_fragment, container, false);

        //SET CUSTOM FONTS:
        TextView viewTitle = (TextView) rootView.findViewById(R.id.view_title);
        viewTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PAGE_TITLE));

        accountObjs = new ArrayList<AccountObj>();
        mainListView = (AccountsListView) rootView.findViewById(R.id.main_list_view);


        //============ BULLETINS STUFF ==============
        //set up dummy bulletins:
        alertItems = new ArrayList<BellItem>();
        for(int i = 0; i < 4; i++) {
            BellItem bi = new BellItem();
            bi.boxColor = getResources().getColor(R.color.obsRed);
            bi.alertMessage = "I will say something amazing.";
            alertItems.add(i,bi);
        }

        alertBannersPanel = (AlertBannersDropdown) rootView.findViewById(R.id.bulletins_panel);
        alertBannersButton = (ImageView) rootView.findViewById(R.id.account_bell);
        setupBanners(alertItems);
        //============ BULLETINS STUFF ==============


        // OPTIONAL: Disable scrolling when list is refreshing
        mainListView.setLockScrollWhileRefreshing(false);

        // MANDATORY: Set the onRefreshListener on the list. You could also use
        // mainListView.setOnRefreshListener(this); and let this Activity
        // implement OnRefreshListener.
        mainListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshAccountData();

                // Make sure you call listView.onRefreshComplete()
                // when the loading is done. This can be done from here or any
                // other place, like on a broadcast receive from your loading
                // service or the onPostExecute of your AsyncTask.
            }
        });

        mainAdapter = new AccountsListAdapter(getActivity(), new ArrayList<Object>(), this);
        mainListView.setDividerHeight(0);
        mainListView.setAdapter(mainAdapter);
        application.inject(mainAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showActivityIndicator("Fetching Accounts...");
    }

    protected void onFragmentAnimationEnd() {
        //Logger.debug("AccountsFragment :: onFragmentAnimationEnd");
        refreshAccountData();
    }

    private void refreshAccountData() {
        Logger.debug("AccountsFragment :: refreshAccountData");
        //clear the displayItems:
        displayItems = new ArrayList<Object>();

        //load the overview graphs - other info is loaded after this:
        getAccountOverviewGraphs();

    }

    private void getAccountOverviewGraphs() {
        //Logger.debug("AccountsFragment :: getAccountOverviewGraphs");
        //set up the graphs for the top of the page:
        String graphTitle = getResources().getString(R.string.overview_graphs_header);
        accountTotalGraphs = new ListItemGraphs(graphTitle.toUpperCase());
        displayItems.add(accountTotalGraphs);

        reportingFacade.getAccountTotals(new Callback<GetAccountTotalsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountTotalsResponse result) {
                ArrayList<AccountTotalObj> accountTotalObjs = result.getTotals();

                //get account totals graph data and pass it to the ListItemGraphs object:
                ArrayList<GraphObj> totalGraphs = new ArrayList<GraphObj>();
                for (AccountTotalObj accountTotalObj : accountTotalObjs) {
                    ArrayList<GraphObj> graphObjs = accountTotalObj.graphs;
                    for (GraphObj graphObj : graphObjs) {
                        totalGraphs.add(graphObj);
                    }
                }
                accountTotalGraphs.graphs = totalGraphs;

                //refresh the display:
                //mainAdapter.setAccountsList(displayItems);
                //mainAdapter.notifyDataSetChanged();
                getAccountInfo();
            }

            @Override
            public void onTaskFailure(String Reason) {
                //TODO:Some error message
                getAccountInfo();
            }
        });
    }

    private void getAccountInfo() {
        //Logger.debug("AccountsFragment :: getAccountInfo");
        //==== set up the data for the main accounts listview:
        reportingFacade.getAccounts(new Callback<GetAccountsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountsResponse result) {
                ArrayList<AccountObj> allAccts = result.getAccounts();

                //sort the results by type (just in case):
                Collections.sort(allAccts, new Comparator<AccountObj>() {
                    @Override
                    public int compare(AccountObj o1, AccountObj o2) {
                        return o1.type.compareTo(o2.type);
                    }
                });

                //set up the account list items and headers:
                ListItemHeader currentHeader = new ListItemHeader(""); //reference holder for helping with balance type headings
                String currentType = "";
                for (int i = 0; i < allAccts.size(); i++) {
                    AccountObj acct = allAccts.get(i);
                    ListItemHeader newHeader = new ListItemHeader(acct.type); //create a header in case you need it

                    //if this is a new header type, add it!!!
                    if (!currentType.equals(acct.type)) {
                        displayItems.add(newHeader);
                        currentHeader = newHeader;
                        currentType = acct.type;
                    }

                    //create and add list item
                    ListItemAcct newAcct = new ListItemAcct(acct);
                    displayItems.add(newAcct);

                    //check for balance headings:
                    Collection<BalanceObj> acctBalances = acct.balances;
                    if (!currentHeader.hasColumnHeadings) {
                        currentHeader.setBalanceTypes(acct.balances);
                    }
                }


                //refresh the display:
                mainAdapter.setAccountsList(displayItems);
                mainListView.onRefreshComplete();
                stopActivityIndicator();
                Logger.debug("Accounts ListView refreshed properly");
            }

            @Override
            public void onTaskFailure(String Reason) {
                //TODO:Some error message
                //mainListView.onRefreshComplete();
                stopActivityIndicator();
            }
        });
    }


//=========================== SAMPLE API CALLS ==============================

    /**
     * This is an example of a mixed fake and real api call. Feel free to delete.
     */
    public void testAPICallFeaturingRealANDFakeCalls() {
        reportingFacade.getAccounts(new Callback<GetAccountsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountsResponse result) {
                Log.d("onTaskCompleted", "Real Accounts Fetched");
                reportingFacade.fake().getAccount(result.getAccounts().get(0), new Callback<AccountObj>() {
                    @Override
                    public void onTaskCompleted(AccountObj accountObj) {
                        Log.e("Fake Account From Real", "AccountId: " + accountObj.accountId + "Name" + accountObj.displayName);
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        Log.e("Error: ", Reason);
                    }
                });
            }

            @Override
            public void onTaskFailure(String Reason) {
                Log.e("Error: ", Reason);
            }
        });
    }

    private void apiCalls(){

        /**Get all account totals. This will return balances and graphs for all accounts
         * In the iOS version this populated the cards at the top of the accounts page
         */

        reportingFacade.fake().getAccounts(new Callback<GetAccountsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountsResponse result) {
                Log.d("onTaskCompleted", "Accounts Fetched");
                for (AccountObj accountObj:result.getAccounts()) {
                    Log.e("Fake Account", "AccountId: " + accountObj.accountId + "Name" + accountObj.displayName);
                }
            }

            @Override
            public void onTaskFailure(String Reason) {

            }
        });
        reportingFacade.getAccountTotals(new Callback<GetAccountTotalsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountTotalsResponse result) {
                ArrayList<AccountTotalObj> accountTotalObjs = result.getTotals();
                AccountTotalObj accountTotalObj = accountTotalObjs.get(0);
                ArrayList<GraphObj> graphObjs = accountTotalObj.graphs;
                ArrayList<BalanceObj> balanceObjs = accountTotalObj.balanceTotals;
            }

            @Override
            public void onTaskFailure(String Reason) {

            }
        });



        /**Get all accounts. This call also returns AccountTypes. These accounts populate the
         * list view in the middle of the accounts page. In this example we do a call for further
         * information on a given account after we get all accounts.
         */
        reportingFacade.getAccounts(new Callback<GetAccountsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountsResponse result) {
                ArrayList<AccountObj> accountsArray = result.getAccounts();
                ArrayList<AccountTypeObj> accountTypes = result.getAccountTypes();

                /**Let's make a call to get complete information on a given AccountObj
                 * This call will return graph information for the given account,
                 * recent transactions, and other information.
                 */
                AccountObj accountObj = accountsArray.get(0);
                reportingFacade.getAccount(accountObj, new Callback<AccountObj>() {
                    @Override
                    public void onTaskCompleted(AccountObj result) {

                    }

                    @Override
                    public void onTaskFailure(String Reason) {

                    }
                });

                /**Here we will get the recent transactions for a given account.
                 * This is similar to the method that searches for transactions
                 */
                reportingFacade.getTransactions(accountObj, new Callback<TransactionsObj>() {
                    @Override
                    public void onTaskCompleted(TransactionsObj result) {

                    }

                    @Override
                    public void onTaskFailure(String Reason) {

                    }
                });

                /** Here is an example transaction search. For this search we are only searching
                 * for transactions in this given account, with a value between 500 and 5000. We do
                 * not care about the check number, date range, or which transaction group it is in.
                 */
                TransactionSearch search = new TransactionSearch(null,null,null,true,null, new BigDecimal(500), new BigDecimal(5000), accountObj.accountId,null,null);
                reportingFacade.getTransactions(accountObj, search, new Callback<TransactionsObj>() {
                    @Override
                    public void onTaskCompleted(TransactionsObj result) {
                        TransactionObj transactionObj = result.getTransactions().get(0);

                    }

                    @Override
                    public void onTaskFailure(String Reason) {

                    }
                });
            }

            @Override
            public void onTaskFailure(String Reason) {
                //Something went wrong
            }
        });


        /**Get all transactionGroups
         * The results of this are sometimes used in the advanced transaction searches
         */
        reportingFacade.getTransactionGroups(new Callback<Collection<TransactionGroup>>() {
            @Override
            public void onTaskCompleted(Collection<TransactionGroup> result) {
                TransactionGroup transactionGroup = (TransactionGroup) result.toArray()[0];

            }

            @Override
            public void onTaskFailure(String Reason) {

            }
        });

    }

}

