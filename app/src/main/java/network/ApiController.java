package network;

import network.interfaces.APIService;

public class ApiController {

    private ApiController() {
    }

    public static final String BASE_URL= "https://api.blockcypher.com/v1/btc/test3/addrs/";
    public static final String ADDRESS_BASE_URL = "https://api.blockcypher.com/v1/btc/test3/";
    public static final String ADDRESS_BALANCE_URL = "/balance";
    public static final String ADDRESS_TRANSACTION_URL = "/full";
    public static final String TOKEN = "f35e42a065104ae5a244d221e267ebf1";


    public static APIService getAddressAPIService() {
        return RetrofitClient.getInstance().getClient(ADDRESS_BASE_URL).create(APIService.class);
    }

    public static APIService getAPIService() {
        return RetrofitClient.getInstance().getClient(BASE_URL).create(APIService.class);
    }

}

