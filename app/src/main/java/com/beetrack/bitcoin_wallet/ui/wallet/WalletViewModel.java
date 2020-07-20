package com.beetrack.bitcoin_wallet.ui.wallet;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.beetrack.bitcoin_wallet.model.BalanceAddress;
import com.beetrack.bitcoin_wallet.ui.address.AddressViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import network.ApiController;
import network.interfaces.APIService;


import network.dto.response.BalanceAddressApiServiceResponse;

public class WalletViewModel extends AddressViewModel {

    private MutableLiveData<BalanceAddress> mBalanceAddress;

    private static final String TAG = WalletViewModel.class.getSimpleName();

    public WalletViewModel(@NonNull Application application) {
        super(application);
        if (mBalanceAddress == null) {
            mBalanceAddress = new MutableLiveData<>();
            setBalanceAddress(new BalanceAddress());
        }
    }

    public MutableLiveData<BalanceAddress> getBalanceAddress() {
        return mBalanceAddress;
    }

    public void setBalanceAddress(BalanceAddress balanceAddress) {
        this.mBalanceAddress.setValue(balanceAddress);
    }

    public void generateBalanceAddressFromServer() {

        final String address = getAddress().getValue().getAddress();

        String urlCall =   address + ApiController.ADDRESS_BALANCE_URL;

        if (!address.isEmpty()) {
            APIService apiService = ApiController.getAPIService();
            apiService.getBalance(urlCall)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BalanceAddressApiServiceResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: ");
                        }

                        @Override
                        public void onNext(BalanceAddressApiServiceResponse balanceAddressApiServiceResponse) {
                            setBalanceAddress(new BalanceAddress(
                                    balanceAddressApiServiceResponse.getBalance(),
                                    balanceAddressApiServiceResponse.getUnconfirmedBalance(),
                                    balanceAddressApiServiceResponse.getFinalBalance()
                            ));

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ", e.getCause());
                            setBalanceAddress(getBalanceAddress().getValue()); //actualiza el balance almacenado en caso de error
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");
                        }
                    });

        }
    }

}