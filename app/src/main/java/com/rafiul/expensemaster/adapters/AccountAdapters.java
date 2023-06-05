package com.rafiul.expensemaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafiul.expensemaster.R;
import com.rafiul.expensemaster.databinding.RowAccountBinding;
import com.rafiul.expensemaster.models.Account;
import com.rafiul.expensemaster.models.Category;

import java.util.ArrayList;

public class AccountAdapters extends RecyclerView.Adapter<AccountAdapters.AccountHolder> {

    Context context;
    ArrayList<Account> accountArrayList;

    public interface AccountClickListener{
        void onAccountClicked(Account account);
    }

    AccountClickListener accountClickListener;

    public AccountAdapters(Context context, ArrayList<Account> accountArrayList, AccountClickListener accountClickListener) {
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.accountClickListener = accountClickListener;
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountHolder(LayoutInflater.from(context).inflate(R.layout.row_account,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        Account account = accountArrayList.get(position);

        holder.rowAccountBinding.accountName.setText(account.getAccountName());
        holder.itemView.setOnClickListener(view -> {
            accountClickListener.onAccountClicked(account);
        });

    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder{

        RowAccountBinding rowAccountBinding;
        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            rowAccountBinding = RowAccountBinding.bind(itemView);
        }
    }
}
