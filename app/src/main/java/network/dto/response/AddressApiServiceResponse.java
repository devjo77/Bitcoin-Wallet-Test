package network.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressApiServiceResponse {

    @SerializedName("address")
    @Expose
    private String mAddress;

    public String getAddress() {
        return mAddress;
    }

    @Override
    public String toString() {
        return "AddressApiServiceResponse{" +
                "mAddress='" + mAddress + '\'' +
                '}';
    }
}
