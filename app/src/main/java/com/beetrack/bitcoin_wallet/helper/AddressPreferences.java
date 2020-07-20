package com.beetrack.bitcoin_wallet.helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.beetrack.bitcoin_wallet.model.BalanceAddress;

/**
 * @AddressPreferences
 *
 * @setAddressPreferences, Almacena la address para que se mantega viva en toda la appp
 * @setLastBalancePreferences Almacena el ultimo balance registado,
 *                            en caso de que no se haya conexion,
 *                            se muestra el monto almacenado
 */

public class AddressPreferences {

    private static final String ADDRESS_PREFERENCES = "PRINTER_PREFERENCES";
    private static final String ADDRESS = "ADDRESS";
    private static final String ADDRESS_LAST_BALANCE = "ADDRESS_LAST_BALANCE";
    private static final String ADDRESS_LAST_UNCONFIRME_BALANCE = "ADDRESS_LAST_UNCONFIRME_BALANCE";
    private static final String ADDRESS_LAST_FINAL_BALANCE = "ADDRESS_LAST_FINAL_BALANCE";

    private static SharedPreferences mPrefs;
    private static AddressPreferences _Instance;

    private AddressPreferences() {
    }

    public static AddressPreferences getInstance(Context ctx) {
        mPrefs = ctx.getSharedPreferences(ADDRESS_PREFERENCES, Context.MODE_PRIVATE);
        if (_Instance == null) {
            _Instance = new AddressPreferences();
        }
        return _Instance;
    }

    public boolean setAddressPreferences(String address) {
        if (mPrefs != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(ADDRESS, address);
            return editor.commit();
        } else return false;
    }


    public String getAddressPreferences() {
        try {
            if (mPrefs != null) {
                return mPrefs.getString(ADDRESS, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean setLastBalancePreferences(BalanceAddress balance) {
        if (mPrefs != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(ADDRESS_LAST_BALANCE, balance.getBalance());
            editor.putString(ADDRESS_LAST_UNCONFIRME_BALANCE, balance.getUnconfirmedBalance());
            editor.putString(ADDRESS_LAST_FINAL_BALANCE, balance.getFinalBalance());
            return editor.commit();
        } else return false;
    }

    public BalanceAddress getLastAddressBalance() {
        try {
            if (mPrefs != null) {
                return new BalanceAddress(
                        mPrefs.getString(ADDRESS_LAST_BALANCE, "0"),
                        mPrefs.getString(ADDRESS_LAST_UNCONFIRME_BALANCE, "0"),
                        mPrefs.getString(ADDRESS_LAST_FINAL_BALANCE, "0")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
