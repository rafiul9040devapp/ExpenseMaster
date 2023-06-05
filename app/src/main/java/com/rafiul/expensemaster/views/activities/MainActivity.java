package com.rafiul.expensemaster.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.rafiul.expensemaster.R;
import com.rafiul.expensemaster.adapters.TransactionAdapters;
import com.rafiul.expensemaster.databinding.ActivityMainBinding;
import com.rafiul.expensemaster.models.Transaction;
import com.rafiul.expensemaster.utils.Constants;
import com.rafiul.expensemaster.utils.Helper;
import com.rafiul.expensemaster.viewmodels.MainViewModel;
import com.rafiul.expensemaster.views.fragments.AddTransactionFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    Calendar calendar;

//    Realm realm;

    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class); //initializing the view model
//        setOfDatabase(); //Database is applied in this application

        setSupportActionBar(mainBinding.toolBar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        mainBinding.nextDateBtn.setOnClickListener(view -> {

            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        mainBinding.previousDateBtn.setOnClickListener(view -> {

            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        mainBinding.floatingActionButtion.setOnClickListener(v -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        mainBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        ArrayList<Transaction> transactionArrayList = new ArrayList<>();


//        //adding the data in the database process
//
//        realm.beginTransaction();//starting of the adding process
//
//        //all the processing code is written in here
//
//
//        //data inserting process in the realm
//        //this need realm object and so Transaction class extends RealmObject
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Cash", "Some note here", new Date(), 500, new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.EXPENSE, "Investment", "Bank", "Some note here", new Date(), -900, new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Rent", "Card", "Some note here", new Date(), 500, new Date().getTime()));
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME, "Business", "Other", "Some note here", new Date(), 500, new Date().getTime()));
//
//        realm.commitTransaction();//ending of the read & write process

//        //Realm is noSQL database and it is easy to fetch data from the database
//
//        //Data fetching process
//        RealmResults<Transaction> transactionLists = realm.where(Transaction.class).findAll(); //fetching all the transaction and It returns RealmResults

        mainBinding.transactionListRec.setLayoutManager(new LinearLayoutManager(this));

        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionAdapters transactionAdapters = new TransactionAdapters(MainActivity.this, transactions);
                mainBinding.transactionListRec.setAdapter(transactionAdapters);

                if (transactions.size() > 0) {
                    mainBinding.emptyStateIV.setVisibility(View.GONE);
                } else {
                    mainBinding.emptyStateIV.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                mainBinding.incomeLabel.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                mainBinding.expenseLabel.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                mainBinding.totalLabel.setText(String.valueOf(aDouble));
            }
        });

        viewModel.getTransactions(calendar);


    }

//    void setOfDatabase(){
//        Realm.init(this);//initializing the REALM DATABASE in this activity
//
////        realm = Realm.getInstance(); this is mainly used for custome purpose
//
//        realm = Realm.getDefaultInstance();
//    }


    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }

    void updateDate() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, YYYY");
//        mainBinding.currentDateTV.setText(Helper.formatDate(calendar.getTime()));
//        viewModel.getTransactions(calendar);

        if (Constants.SELECTED_TAB == Constants.DAILY) {
            mainBinding.currentDateTV.setText(Helper.formatDate(calendar.getTime()));
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            mainBinding.currentDateTV.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}