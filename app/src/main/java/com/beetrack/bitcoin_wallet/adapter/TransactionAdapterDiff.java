package com.beetrack.bitcoin_wallet.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.beetrack.bitcoin_wallet.R;
import com.beetrack.bitcoin_wallet.databinding.TransactionItemBinding;
import com.beetrack.bitcoin_wallet.model.Transaction;
import com.beetrack.bitcoin_wallet.ui.viewHolder.TransactionViewHolder;

public class TransactionAdapterDiff extends PagedListAdapter<Transaction, TransactionViewHolder> {

    public TransactionAdapterDiff() {
        super(DIFF_CALLBACK);
    }


    private static DiffUtil.ItemCallback<Transaction> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Transaction>() {
                // Concert details may have changed if reloaded from the database,// but ID is fixed.
                @Override
                public boolean areItemsTheSame(Transaction oldTransaction, Transaction newTransaction) {
                    return oldTransaction.getHash() == newTransaction.getHash();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Transaction oldTransaction, Transaction newTransaction) {
                    return oldTransaction.equals(newTransaction);
                }
            };


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransactionItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.transaction_item,
                        parent,
                        false);

        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.setTransaction(getItem(position));
    }

}
