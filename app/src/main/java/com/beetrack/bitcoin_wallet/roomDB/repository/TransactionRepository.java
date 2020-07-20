package com.beetrack.bitcoin_wallet.roomDB.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.beetrack.bitcoin_wallet.model.Transaction;
import com.beetrack.bitcoin_wallet.roomDB.dao.TransactionDao;
import com.beetrack.bitcoin_wallet.roomDB.db.AppDatabase;

import java.util.List;

public class TransactionRepository {

    private TransactionDao mTransactionDao;

    public final LiveData<PagedList<Transaction>> mTransactionDbLivePageList;

    public TransactionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTransactionDao = db.mTransactionDao();

        mTransactionDbLivePageList = new LivePagedListBuilder<>(
                mTransactionDao.getTransactionsPagedList(), /* page size */ 10).build();
    }


    public LiveData<PagedList<Transaction>> getTransactionDbLivePageList() {
        return mTransactionDbLivePageList;
    }

    public void insertTransactions(List<Transaction> transaction) {
        new InsertTechConfigGeneralAsyncTask(mTransactionDao).execute(transaction);
    }

    public class InsertTechConfigGeneralAsyncTask extends AsyncTask<List<Transaction> , Void, Void> {

        private TransactionDao mAsyncTaskDao;

        public InsertTechConfigGeneralAsyncTask(TransactionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<Transaction>... params) {
            mAsyncTaskDao.insertTransaction(params[0]);
            return null;
        }
    }
}
