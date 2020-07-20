package com.beetrack.bitcoin_wallet.roomDB.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.beetrack.bitcoin_wallet.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM 'transaction' ORDER BY id ASC")
    DataSource.Factory<Integer, Transaction> getTransactionsPagedList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTransaction(List<Transaction> transactions);

}
