package com.beetrack.bitcoin_wallet.ui.transactions;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.beetrack.bitcoin_wallet.model.Transaction;
import com.beetrack.bitcoin_wallet.roomDB.repository.TransactionRepository;
import com.beetrack.bitcoin_wallet.ui.address.AddressViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import network.ApiController;
import network.dto.response.TransactionsApiServiceResponse;
import network.interfaces.APIService;

public class TransactionsViewModel extends AddressViewModel {

    private static final String TAG = TransactionsViewModel.class.getSimpleName();

    private final LiveData<PagedList<Transaction>> mTransactionsDbLivePagedList;

    private TransactionRepository mRepository;
    private MutableLiveData<List<Transaction>> mTransactions;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);

        mRepository = new TransactionRepository(application);

        mTransactionsDbLivePagedList = mRepository.getTransactionDbLivePageList(); //objeto con la paginacion....

        if (mTransactions == null) {
            mTransactions = new MutableLiveData<>();
        }
    }

    public LiveData<PagedList<Transaction>> getTransactionsDbLivePagedList() {
        return mTransactionsDbLivePagedList;
    }

    public MutableLiveData<List<Transaction>> getTransactions() {
        return mTransactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.mTransactions.setValue(transactions);
    }

    public void insertTransactionsLiveDb(List<Transaction> transactions) {
        mRepository.insertTransactions(transactions);
    }

    public void getTransactionsFromServer() {

        final String address = getAddress().getValue().getAddress();

        String urlCall =   address + ApiController.ADDRESS_TRANSACTION_URL;

        if (!address.isEmpty()) {
            APIService apiService = ApiController.getAPIService();
            apiService.getAddressTrx(urlCall)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TransactionsApiServiceResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(TransactionsApiServiceResponse transactionsApiServiceResponse) {
                            List<Transaction> transactions = new ArrayList<>();

                            if (transactionsApiServiceResponse.getTransactions().size() > 0)
                                for (TransactionsApiServiceResponse.trx item : transactionsApiServiceResponse.getTransactions()) {
                                    transactions.add(new Transaction(
                                            (item.getDate()),
                                            //formatDate(item.getDate()),
                                            item.getOutputs().get(0).getValue(),
                                            item.getHash()

                                    ));
                                    insertTransactionsLiveDb(transactions);
                                    //setTransactions(transactions);
                                }
                            Log.d(TAG, "onNext: " + transactionsApiServiceResponse.toString());

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e.getCause());
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");
                        }
                    });

        }
    }

}