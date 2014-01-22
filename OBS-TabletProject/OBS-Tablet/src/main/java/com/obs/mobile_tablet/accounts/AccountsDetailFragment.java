package com.obs.mobile_tablet.accounts;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.obs.mobile_tablet.MainActivity;
import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.R;
import com.obs.mobile_tablet.components.DateButton;
import com.obs.mobile_tablet.components.DropdownAdapter;
import com.obs.mobile_tablet.components.OBSFragment;
import com.obs.mobile_tablet.components.TimePickerFragment;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.BalanceObj;
import com.obs.mobile_tablet.datamodel.reporting.GetTransactionGroupsResponse;
import com.obs.mobile_tablet.datamodel.reporting.GraphObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionGroup;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionSearch;
import com.obs.mobile_tablet.datamodel.reporting.TransactionsObj;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.FontFace;
import com.obs.mobile_tablet.utils.Logger;
//import com.testflightapp.lib.TestFlight;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class AccountsDetailFragment extends OBSFragment implements AdapterView.OnItemSelectedListener, TransactionsAdapter.OnItemListener, DatePickerDialog.OnDateSetListener {

    @Inject
    InformationReportingFacade reportingFacade;

    private ListView mainListView;
    private Button backButton;

    private static final int FILTER_START_DATE = 0;
    private static final int FILTER_END_DATE = 1;
    private static final int SEARCH_START_DATE = 2;
    private static final int SEARCH_END_DATE   = 3;
    private int currentDateFilter;

    private TransactionsObj transactionsData;
    private ArrayList<TransactionObj> allTransactionsList;
    private ArrayList<TransactionObj> sortedTransactionsList;
    private AccountObj account;

    private TextView viewTitle;
    private AccountDetailChart accountsPieChart;
    private EditText filterText;
    private Spinner typeSpinner;
    private Spinner advacedSearchTypeSpinner;
    private Spinner dateSpinner;
    private Date startDate;
    private Date endDate;
    private Button advancedSearchButton;
    private Button advancedSubmitButton;
    private AccountTransactionsAdvancedSearch advancedSearchBox;
    private RelativeLayout filterControls;
    private ArrayList<TransactionGroup> transactionGroups;
    private ListView balanceList;
    private ImageButton collapseAllButton;
    private boolean allCollapsed;

    private DateButton startDateFilterButton;
    private DateButton endDateFilterButton;
    private TextView toDateFilterLabel;
    private View rootView;
    private TransactionsAdapter adapter;

    private boolean showingCustomSearch;

    ListView transactionListView;


    public static AccountsDetailFragment newInstance(int num) {
        AccountsDetailFragment f = new AccountsDetailFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    //interface implementation:
    public void onCheckClicked(String pdfUrl) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        PDFDialogFragment newFragment = new PDFDialogFragment();
        newFragment.setUrlToLoad(pdfUrl);
        newFragment.show(fragmentManager, "dialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TestFlight.passCheckpoint("Account Details");

        rootView = inflater.inflate(R.layout.accounts_detail_fragment, container, false);

        filterText = (EditText) rootView.findViewById(R.id.filter_text);

        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTransactions();
            }
        });

        typeSpinner = (Spinner) rootView.findViewById(R.id.type_spinner);
        dateSpinner = (Spinner) rootView.findViewById(R.id.date_spinner);
        transactionListView = (ListView) rootView.findViewById(R.id.transaction_list);

        accountsPieChart = (AccountDetailChart) rootView.findViewById(R.id.account_detail_pie_chart);
        //accountsPieChart.setData(graphObj);

        backButton = (Button) rootView.findViewById(R.id.back_button);
        backButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_BLACK_BUTTON));
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccountSummaryFragment f = AccountSummaryFragment.newInstance(1);
                ((OBSApplication) getActivity().getApplication()).inject(f);
                f.setAccount(account);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(((ViewGroup)(getView().getParent())).getId(), f);
                ft.commit();
            }
        });

        allCollapsed = false;
        collapseAllButton = (ImageButton) rootView.findViewById(R.id.toggleExtensionButton);
        collapseAllButton.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(allCollapsed) {
                            expandAllDescriptions();
                        } else {

                            collapseAllDescriptions();
                        }
                    }
                }
        );

        //SET CUSTOM FONTS:
        viewTitle = (TextView) rootView.findViewById(R.id.all_transactions_title);
        viewTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PAGE_TITLE));

        TextView balanceTitle = (TextView) rootView.findViewById(R.id.balances_title);
        balanceTitle.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_HEADER_3));



        balanceList = (ListView) rootView.findViewById(R.id.balances_list_view);

        TextView dateHeader = (TextView) rootView.findViewById(R.id.transactions_date_header);
        dateHeader.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_TABLE_HEADER));

        TextView checkNOHeader = (TextView) rootView.findViewById(R.id.transactions_check_header);
        checkNOHeader.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_TABLE_HEADER));

        TextView descriptionHeader = (TextView) rootView.findViewById(R.id.transactions_description_header);
        descriptionHeader.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_TABLE_HEADER));

        TextView creditHeader = (TextView) rootView.findViewById(R.id.transactions_credit_header);
        creditHeader.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_TABLE_HEADER));

        TextView debitHeader = (TextView) rootView.findViewById(R.id.transactions_debit_header);
        debitHeader.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_TABLE_HEADER));

        TextView balanceHeader = (TextView) rootView.findViewById(R.id.transactions_balance_header);
        balanceHeader.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_TABLE_HEADER));

        filterControls = (RelativeLayout) rootView.findViewById(R.id.all_transactions_filter_controls);

        toDateFilterLabel = (TextView) rootView.findViewById(R.id.datefilter_to_label);
        toDateFilterLabel.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));
        toDateFilterLabel.setTextSize(12);

        startDateFilterButton = (DateButton) rootView.findViewById(R.id.start_datefilter_field);
        endDateFilterButton = (DateButton) rootView.findViewById(R.id.end_datefilter_field);
        startDateFilterButton.setDate(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        startDateFilterButton.setDate(cal.getTime());
        endDateFilterButton.setDate(new Date());
        showingCustomSearch = true;

        advancedSearchButton = (Button) rootView.findViewById(R.id.advanced_search_button);
        advancedSearchButton.setTypeface(FontFace.GetFace(getActivity(), FontFace.FONT_PRIMARY_BUTTON));

        advancedSearchBox = (AccountTransactionsAdvancedSearch) rootView.findViewById(R.id.advanced_search_wrapper);
        advancedSearchBox.collapse();

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        advancedSearchBox.endDateButton.setDate(new Date());
        advancedSearchBox.startDateButton.setDate(cal.getTime());

        //addItemsOnTypeSpinner(getActivity());

        LinearLayout background = (LinearLayout) rootView.findViewById(R.id.all_transactions_background);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //MainActivity parent = (MainActivity) getActivity();
        showActivityIndicator("Fetching Transactions...");
    }

    public void setAccount (AccountObj newAccount) {
        refreshData(newAccount);
    }

    private void refreshData(AccountObj newAccount) {
        account = newAccount;
        allTransactionsList = account.getTransactionDetails().getTransactions();
    }

    protected void onFragmentAnimationEnd() {
        activateMe();
        getInitialTransactions();
    }

    private void collapseAllDescriptions () {

        allCollapsed = true;
        adapter.hidingAll = true;
        adapter.notifyDataSetChanged();
        rotate(180, collapseAllButton);
    }

    private void expandAllDescriptions () {

        adapter.hidingAll = false;
        adapter.notifyDataSetChanged();
        allCollapsed = false;
        rotate(0, collapseAllButton);
    }
    //do anything that is dependent on the data:
    private void activateMe() {
        viewTitle.setText(account.displayName.toUpperCase());

        ArrayList<BalanceObj> balanceArray = new ArrayList<BalanceObj>(account.balances);
        BalanceAdapter balanceAdapter = new BalanceAdapter(
                getActivity(),
                R.layout.account_details_balance_cell,
                balanceArray);

        //new ArrayList<BalanceObj>(account.balances));
        balanceList.setAdapter(balanceAdapter);

        advancedSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advancedSearchBox.expand();
                filterControls.setVisibility(View.GONE);
            }
        });

        advancedSearchBox.closeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advancedSearchBox.collapse();
                filterControls.setVisibility(View.VISIBLE);
            }
        });

        advancedSearchBox.submitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advancedSearchBox.collapse();
                filterControls.setVisibility(View.VISIBLE);

                searchTransactions(advancedSearchBox.getTransactionSearch());
            }
        });

        advancedSearchBox.startDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentDateFilter = SEARCH_START_DATE;
                showDateDialog();
            }
        });

        advancedSearchBox.endDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentDateFilter = SEARCH_END_DATE;
                showDateDialog();
            }
        });

        startDateFilterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentDateFilter = FILTER_START_DATE;
                showDateDialog();
            }
        });

        endDateFilterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentDateFilter = FILTER_END_DATE;
                showDateDialog();
            }
        });

        addItemsOnDateSpinner(getActivity());
        getTransactionGroups();
        checkCustomSearch();
        filterTransactions();
    }

    private void showDateDialog(){

        FragmentManager fm = getActivity().getSupportFragmentManager();
        TimePickerFragment newFragment = new TimePickerFragment(this);
        newFragment.show(fm, "date_picker");

    }

    public void addItemsOnTypeSpinner(ArrayList<TransactionGroup> typeGroups) {

        transactionGroups = typeGroups;
        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("ALL TYPES");

        for(TransactionGroup tGroup: typeGroups) {

            if(tGroup != null) {

                typeList.add(tGroup.name);
            }
        }

        DropdownAdapter dataAdapter = new DropdownAdapter(getActivity(),
                R.layout.widget_spinner_layout, typeList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(dataAdapter);
        advancedSearchBox.groupSpinner.setAdapter(dataAdapter);
        typeSpinner.setEnabled(true);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterTransactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterTransactions();
            }
        });

    }

    public void addItemsOnDateSpinner(Context context) {

        ArrayList<String> dateList = new ArrayList<String>();
        dateList.add("ALL DATES");
        dateList.add("LAST WEEK");
        dateList.add("LAST 30 DAYS");
        dateList.add("LAST 60 DAYS");
        dateList.add("CUSTOM DATES");
        DropdownAdapter dataAdapter = new DropdownAdapter(context,
                R.layout.widget_spinner_layout, dateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dataAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                checkCustomSearch();
                filterTransactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                checkCustomSearch();
                filterTransactions();
            }
        });
    }

    private void getInitialTransactions () {

        reportingFacade.getTransactions(account, new Callback<TransactionsObj>() {
            @Override
            public void onTaskCompleted(TransactionsObj result) {
                stopActivityIndicator();
                GraphObj pieGraphData = result.getGraphs().get(1);
                accountsPieChart.setData(pieGraphData);

                //transactionsData = result;
            }

            @Override
            public void onTaskFailure(String Reason) {
                stopActivityIndicator();
            }
        });
    }

    private void filterTransactions() {

        if(allTransactionsList==null || allTransactionsList.size()==0) {
            return;
        }

        sortedTransactionsList = new ArrayList<TransactionObj>();

        for(TransactionObj transaction : allTransactionsList) {

            boolean isIncluded = true;

            if(filterText.getText().length() > 0) {

                isIncluded = transactionIncludesString(filterText.getText().toString(), transaction);
            }

            if(isIncluded && typeSpinner.getSelectedItemPosition() > 0) {

                //TransactionGroup tGroup = transactionGroups.get(typeSpinner.getSelectedItemPosition() - 1);
                if(!typeSpinner.getSelectedItem().equals(transaction.type)) {
                    isIncluded = false;
                }
            }

            if(isIncluded && dateSpinner.getSelectedItemPosition() > 0) {

                if(!isDateInRange(transaction.getRawDate())) {
                    isIncluded = false;
                }
            }

            if(isIncluded) {
                sortedTransactionsList.add(transaction);
            }
        }

       advancedSearchBox.startDateButton.setDate(allTransactionsList.get(allTransactionsList.size() - 1).getRawDate());

        adapter = new TransactionsAdapter(getActivity(),
                R.layout.accounts_all_transactions_cell, sortedTransactionsList, this);
        transactionListView.setAdapter(adapter);
    }

    private boolean transactionIncludesString (String searchString, TransactionObj transactionObj) {

        return stringIncludesString( searchString, transactionObj.displayText) ||
                stringIncludesString( searchString, transactionObj.displayCheckNumber) ||
                stringIncludesString( searchString, transactionObj.creditAmount) ||
                stringIncludesString( searchString, transactionObj.debitAmount) ||
                stringIncludesString( searchString, transactionObj.balanceAmount) ||
                stringIncludesString( searchString, transactionObj.checkNumber);
    }

    private boolean stringIncludesString(String needle, String haystack) {

        if(haystack != null && needle != null) {
            return Pattern.compile(Pattern.quote(needle), Pattern.CASE_INSENSITIVE).matcher(haystack).find();
        }

        return false;
    }


    private void checkCustomSearch () {

        if(dateSpinner.getSelectedItem().equals("CUSTOM DATES")) {
            if(!showingCustomSearch) {
                toDateFilterLabel.setVisibility(View.VISIBLE);
                startDateFilterButton.setVisibility(View.VISIBLE);
                endDateFilterButton.setVisibility(View.VISIBLE);
                showingCustomSearch = true;
            }
        } else {
            if(showingCustomSearch) {
                toDateFilterLabel.setVisibility(View.INVISIBLE);
                startDateFilterButton.setVisibility(View.INVISIBLE);
                endDateFilterButton.setVisibility(View.INVISIBLE);
                showingCustomSearch = false;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        switch(arg0.getId()) {
            case R.id.date_spinner:
                checkCustomSearch();
                break;
            case R.id.type_spinner:
                break;
        }

        filterTransactions();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        switch(arg0.getId()){
            case R.id.date_spinner:
                checkCustomSearch();
                break;
            case R.id.type_spinner:
                break;
        }
        filterTransactions();
    }

    private void getTransactionGroups () {

        typeSpinner.setEnabled(false);

        reportingFacade.getTransactionGroups(new Callback<Collection<TransactionGroup>>() {
            @Override
            public void onTaskCompleted(Collection<TransactionGroup> result) {

                addItemsOnTypeSpinner(new ArrayList<TransactionGroup>(result));
                advancedSearchBox.transactionGroups = new ArrayList<TransactionGroup>(result);
            }

            @Override
            public void onTaskFailure(String Reason) {

                Logger.debug("----------TRANSACTION ERROR: " + Reason);
            }
        });
    }

    public boolean isDateInRange (Date date) {

        int selection = dateSpinner.getSelectedItemPosition();
        Calendar cal = Calendar.getInstance();

        switch(selection) {

            //ALL DATES
            case 0:
                return true;
            //LAST WEEK
            case 1:
                cal.add(Calendar.DATE, -7);
                return cal.before(date);
            //LAST 30 DAYS
            case 2:
                cal.add(Calendar.DATE, -30);
                return cal.before(date);
            //LAST 60 DAYS
            case 3:
                cal.add(Calendar.DATE, -60);
                return cal.before(date);
            //CUSTOM DATES
            case 4:
                return startDateFilterButton.date.before(date) && endDateFilterButton.date.after(date);

            default:
                return true;
        }
    }

    // when dialog box is closed, below method will be called.
    public void onDateSet(DatePicker view, int selectedYear,
                          int selectedMonth, int selectedDay) {

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, selectedDay);
        c.set(Calendar.MONTH, selectedMonth);
        c.set(Calendar.YEAR, selectedYear);

        switch(currentDateFilter) {

            case FILTER_START_DATE:
                startDateFilterButton.setDate(c.getTime());
                filterTransactions();
                break;
            case FILTER_END_DATE:
                endDateFilterButton.setDate(c.getTime());
                filterTransactions();
                break;
            case SEARCH_START_DATE:
                advancedSearchBox.startDateButton.setDate(c.getTime());
                break;
            case SEARCH_END_DATE:
                advancedSearchBox.endDateButton.setDate(c.getTime());
                break;
            default:
                break;
        }

    }
    private void searchTransactions( TransactionSearch newSearch) {

        showActivityIndicator("Fetching Transactions...");
        reportingFacade.getTransactions(account, newSearch, new Callback<TransactionsObj>() {
            @Override
            public void onTaskCompleted(TransactionsObj result) {
                stopActivityIndicator();
                transactionsData = result;
                allTransactionsList = transactionsData.getTransactions();
                filterTransactions();
            }

            @Override
            public void onTaskFailure(String Reason) {
                stopActivityIndicator();
            }
        });
    }

    private class BalanceAdapter extends ArrayAdapter<BalanceObj> {

        ArrayList<BalanceObj> items;

        public BalanceAdapter(Context context, int textViewResourceId, ArrayList<BalanceObj> items) {

            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.account_details_balance_cell, null);
            }
            BalanceObj balance = items.get(position);

            if (balance != null) {

                int val = position % 2;
                RelativeLayout viewCell = (RelativeLayout) v.findViewById(R.id.account_balance_background);

                if(val == 1) {
                    viewCell.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    viewCell.setBackgroundColor( v.getResources().getColor(R.color.obsLightGray));
                }

                TextView titleText = (TextView) v.findViewById(R.id.balance_title_text);
                titleText.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
                titleText.setText(balance.displayText);

                TextView fieldText = (TextView) v.findViewById(R.id.balance_field_text);
                fieldText.setTypeface(FontFace.GetFace(v.getContext(), FontFace.FONT_TABLE_DATA));
                fieldText.setText(balance.amount);
            }

            return v;
        }
    }

    private void rotate(float degree, ImageButton imageView) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        imageView.startAnimation(rotateAnim);
    }
}