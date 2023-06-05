package com.rafiul.expensemaster.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafiul.expensemaster.R;
import com.rafiul.expensemaster.databinding.RowTransactionsBinding;
import com.rafiul.expensemaster.models.Category;
import com.rafiul.expensemaster.models.Transaction;
import com.rafiul.expensemaster.utils.Constants;
import com.rafiul.expensemaster.utils.Helper;
import com.rafiul.expensemaster.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TransactionAdapters extends RecyclerView.Adapter<TransactionAdapters.TransactionHolder> {

    Context context;

    RealmResults<Transaction> transactions;


    public TransactionAdapters(Context context, RealmResults<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionHolder(LayoutInflater.from(context).inflate(R.layout.row_transactions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.rowTransactionsBinding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.rowTransactionsBinding.accountLabelTV.setText(transaction.getAccount());//payment method type
//        holder.rowTransactionsBinding.accountLabelTV.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));
        holder.rowTransactionsBinding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.rowTransactionsBinding.transactionCategoryTV.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.rowTransactionsBinding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.rowTransactionsBinding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));
        holder.rowTransactionsBinding.accountLabelTV.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));


//        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());
//
//        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
//        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));
//
//        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));


        if (transaction.getType().equals(Constants.INCOME)) {
            holder.rowTransactionsBinding.transactionAmount.setTextColor(context.getColor(R.color.green));
        } else if (transaction.getType().equals(Constants.EXPENSE)) {
            holder.rowTransactionsBinding.transactionAmount.setTextColor(context.getColor(R.color.red));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure to delete this transaction?");

                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", ((dialogInterface, i) -> {
                    ((MainActivity)context).viewModel.deleteTransaction(transaction);

                }));

                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", ((dialogInterface, i) -> {
                    deleteDialog.dismiss();
                }));

                deleteDialog.show();
                return false;
            }
        });


        //start from here

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {

        RowTransactionsBinding rowTransactionsBinding;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            rowTransactionsBinding = RowTransactionsBinding.bind(itemView);
        }
    }
}
