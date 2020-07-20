package network.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsApiServiceResponse {


    @SerializedName("txs")
    @Expose
    List<trx> mTransactions;

    public List<trx> getTransactions() {
        return mTransactions;
    }

    public class trx {


        @SerializedName("hash")
        @Expose
        private String mHash;


        @SerializedName("received")
        @Expose
        private String mDate;

        public String getDate() {
            return mDate;
        }

        public String getHash() {
            return mHash;
        }

        @SerializedName("outputs")
        @Expose
        private List<outputs> mOutputs;

        public List<outputs> getOutputs() {
            return mOutputs;
        }

        public class outputs {
            @SerializedName("value")
            @Expose
            private String mValue;

            public String getValue() {
                return mValue;
            }
        }
    }
}
