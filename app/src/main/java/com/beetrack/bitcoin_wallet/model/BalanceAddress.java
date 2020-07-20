package com.beetrack.bitcoin_wallet.model;

public class BalanceAddress {

    private String mBalance;
    private String mUnconfirmedBalance;
    private String mFinalBalance;

    public BalanceAddress() {
        this.mBalance = "0";
        this.mUnconfirmedBalance = "0";
        this.mFinalBalance = "0";
    }

    public BalanceAddress(String mBalance, String mUnconfirmedBalance, String mFinalBalance) {
        this.mBalance = mBalance;
        this.mUnconfirmedBalance = mUnconfirmedBalance;
        this.mFinalBalance = mFinalBalance;
    }

    public String getBalance() {
        return mBalance;
    }

    public String getUnconfirmedBalance() {
        return mUnconfirmedBalance;
    }

    public String getFinalBalance() {
        return mFinalBalance;
    }


}
