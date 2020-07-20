package com.beetrack.bitcoin_wallet.ui.viewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beetrack.bitcoin_wallet.databinding.TransactionItemBinding;
import com.beetrack.bitcoin_wallet.model.Transaction;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

     private TransactionItemBinding mTransactionItemBinding;

    public TransactionViewHolder(@NonNull TransactionItemBinding transactionItemBinding) {
        super(transactionItemBinding.getRoot());
        mTransactionItemBinding = transactionItemBinding;
    }

    public void setTransaction(Transaction transaction){
        mTransactionItemBinding.setTransactionModel(transaction);
    }


}
