package network.interfaces;

import io.reactivex.rxjava3.core.Observable;
import network.dto.response.AddressApiServiceResponse;
import network.dto.response.BalanceAddressApiServiceResponse;
import network.dto.response.TransactionsApiServiceResponse;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {

    @POST("addrs")
    Observable<AddressApiServiceResponse> getAddress( @Query("token") String token);

    @GET
    Observable<BalanceAddressApiServiceResponse> getBalance(@Url String url);

    @GET
    Observable<TransactionsApiServiceResponse> getAddressTrx(@Url String url);

}
