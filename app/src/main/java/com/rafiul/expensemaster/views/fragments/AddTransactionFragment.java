package com.rafiul.expensemaster.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rafiul.expensemaster.R;
import com.rafiul.expensemaster.adapters.AccountAdapters;
import com.rafiul.expensemaster.adapters.CategoryAdapters;
import com.rafiul.expensemaster.databinding.FragmentAddTransactionBinding;
import com.rafiul.expensemaster.databinding.ListDialogBinding;
import com.rafiul.expensemaster.models.Account;
import com.rafiul.expensemaster.models.Category;
import com.rafiul.expensemaster.models.Transaction;
import com.rafiul.expensemaster.utils.Constants;
import com.rafiul.expensemaster.utils.Helper;
import com.rafiul.expensemaster.views.activities.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class AddTransactionFragment extends BottomSheetDialogFragment {

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAddTransactionBinding.inflate(inflater);
        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textcolor));
            transaction.setType(Constants.INCOME);
        });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textcolor));
            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.YEAR, datePicker.getYear());

                String dateToShow = Helper.formatDate(calendar.getTime());

                binding.date.setText(dateToShow);

                transaction.setDate(calendar.getTime());
                transaction.setId(calendar.getTime().getTime());
            });

            datePickerDialog.show();
        });


        binding.category.setOnClickListener(view -> {
            ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryAlertDialog = new AlertDialog.Builder(getContext()).create();
            categoryAlertDialog.setView(listDialogBinding.getRoot());


            CategoryAdapters categoryAdapters = new CategoryAdapters(getContext(), Constants.categories, new CategoryAdapters.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryAlertDialog.dismiss();
                }
            });
            listDialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            listDialogBinding.recyclerView.setAdapter(categoryAdapters);

            categoryAlertDialog.show();
        });

        binding.account.setOnClickListener(view -> {
            ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountAlertDialog = new AlertDialog.Builder(getContext()).create();
            accountAlertDialog.setView(listDialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account("Cash", 0));
            accounts.add(new Account("Card", 0));
            accounts.add(new Account("Bank", 0));
            accounts.add(new Account("Bkash", 0));
            accounts.add(new Account("Other", 0));

            AccountAdapters accountAdapters = new AccountAdapters(getContext(), accounts, new AccountAdapters.AccountClickListener() {
                @Override
                public void onAccountClicked(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountAlertDialog.dismiss();
                }
            });

            listDialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            listDialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            listDialogBinding.recyclerView.setAdapter(accountAdapters);

            accountAlertDialog.show();
        });

        binding.saveTransactionBtn.setOnClickListener(view -> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();

            if (transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount * -1);
            } else {
                transaction.setAmount(amount);
            }
            transaction.setNote(note);

            ((MainActivity)getActivity()).viewModel.addTransactions(transaction);
            ((MainActivity)getActivity()).getTransactions();

            dismiss();
        });
        return binding.getRoot();
    }
}