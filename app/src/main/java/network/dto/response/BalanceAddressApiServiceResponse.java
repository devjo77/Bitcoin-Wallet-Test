package network.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceAddressApiServiceResponse {

    @SerializedName("balance")
    @Expose
    private String mBalance;

    @SerializedName("unconfirmed_balance")
    @Expose
    private String mUnconfirmedBalance;

    @SerializedName("final_balance")
    @Expose
    private String mFinalBalance;

    public BalanceAddressApiServiceResponse() {
    }

    public BalanceAddressApiServiceResponse(String balance, String unconfirmedBalance, String finalBalance) {
        this.mBalance = balance;
        this.mUnconfirmedBalance = unconfirmedBalance;
        this.mFinalBalance = finalBalance;
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

    @Override
    public String toString() {
        return "BalanceAddress{" +
                "mBalance='" + mBalance + '\'' +
                ", mUnconfirmedBalance='" + mUnconfirmedBalance + '\'' +
                ", mFinalBalance='" + mFinalBalance + '\'' +
                '}';
    }
}

