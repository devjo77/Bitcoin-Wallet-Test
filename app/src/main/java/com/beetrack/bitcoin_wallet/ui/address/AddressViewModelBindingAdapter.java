package com.beetrack.bitcoin_wallet.ui.address;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class AddressViewModelBindingAdapter {

    @BindingAdapter("visibleIF")
    public static void setVisibility(View view, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE: View.GONE);
    }
}
