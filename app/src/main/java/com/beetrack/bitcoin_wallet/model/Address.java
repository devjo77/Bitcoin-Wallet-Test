package com.beetrack.bitcoin_wallet.model;

public class Address {

    private String mAddress;
    private String mNotification;
    private boolean mIsAddressGenerate;
    private boolean mIsProgressDialogActive;

    public Address() {
    }

    public Address(String address, String notification, boolean isAddressGenerate) {
        this.mAddress = address;
        this.mNotification = notification;
        this.mIsAddressGenerate = isAddressGenerate;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getNotification() {
        return mNotification;
    }

    public void setNotification(String notification) {
        this.mNotification = notification;
    }

    public boolean isIsAddressGenerate() {
        return mIsAddressGenerate;
    }

    public void setIsAddressGenerate(boolean isAddressGenerate) {
        this.mIsAddressGenerate = isAddressGenerate;
    }

    public boolean isProgressDialogActive() {
        return mIsProgressDialogActive;
    }

    public void setProgressDialogActive(boolean isProgressDialogActive) {
        this.mIsProgressDialogActive = isProgressDialogActive;
    }
}
