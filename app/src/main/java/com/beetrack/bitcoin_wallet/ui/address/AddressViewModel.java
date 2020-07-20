package com.beetrack.bitcoin_wallet.ui.address;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.beetrack.bitcoin_wallet.model.Address;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import network.ApiController;
import network.dto.response.AddressApiServiceResponse;
import network.interfaces.APIService;

public class AddressViewModel extends AndroidViewModel {

    protected MutableLiveData<Address> mAddress;
    private MutableLiveData<Boolean> mIsSaveAddress;

    private static final String TAG = AddressViewModel.class.getSimpleName();

    public AddressViewModel(@NonNull Application application) {
        super(application);

        if (mAddress == null) {
            mAddress = new MutableLiveData<>();
        }

        if (mIsSaveAddress == null) {
            mIsSaveAddress = new MutableLiveData<>();
        }

        if (mIsSaveAddress == null) {
            mIsSaveAddress = new MutableLiveData<>();
            mIsSaveAddress.setValue(false);
        }
    }

    public void setAddress(Address address) {
        this.mAddress.setValue(address);
    }

    public MutableLiveData<Address> getAddress() {
        return mAddress;
    }

    public MutableLiveData<Boolean> getSave() {
        return mIsSaveAddress;
    }

    public void setSave(Boolean visible) {
        this.mIsSaveAddress.setValue(visible);
    }

    public void onCLickGenerateAddress() {
        generateAddressFromServer();
    }

    public void onClickSaveAddress() {
        setSave(true);
    }

    public void generateAddressFromServer() {

        mAddress.getValue().setProgressDialogActive(true);

        setAddress(mAddress.getValue());

        APIService apiService = ApiController.getAddressAPIService();
        apiService.getAddress( ApiController.TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressApiServiceResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(AddressApiServiceResponse addressApiServiceResponse) {
                        Log.d(TAG, "onNext: " + addressApiServiceResponse.toString());
                        setAddress(new Address(addressApiServiceResponse.getAddress(), "", true));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e.getCause());
                        setAddress(new Address(mAddress.getValue().getAddress(), "Error al obtener la direccion, verifique su conexion de red", false));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

    }


}