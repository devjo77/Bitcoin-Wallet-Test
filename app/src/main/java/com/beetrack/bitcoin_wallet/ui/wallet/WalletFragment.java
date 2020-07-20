package com.beetrack.bitcoin_wallet.ui.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.beetrack.bitcoin_wallet.R;
import com.beetrack.bitcoin_wallet.databinding.FragmentWalletBinding;
import com.beetrack.bitcoin_wallet.helper.AddressPreferences;
import com.beetrack.bitcoin_wallet.helper.QrConvertHelper;
import com.beetrack.bitcoin_wallet.model.Address;
import com.beetrack.bitcoin_wallet.model.BalanceAddress;
import com.beetrack.bitcoin_wallet.utils.NetworkUtils;
import com.bumptech.glide.Glide;

import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;

public class WalletFragment extends Fragment {

    private WalletViewModel mWalletViewModel;
    private FragmentWalletBinding mWalletBindingView;
    private AddressPreferences mPreferences;
    private ProgressDialog mProgress;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mWalletBindingView = FragmentWalletBinding.inflate(inflater, container, false); //se crea el enlace a la vista...

        mWalletBindingView.setLifecycleOwner(getActivity()); // se asigna el control del ciclo de vida al binding....

        mWalletViewModel = new ViewModelProvider(getActivity()).get(WalletViewModel.class);  // se crea el viemodel para persitir los atributos y sus cambios en tiempo real...

        mWalletBindingView.setViewModelNotification(mWalletViewModel);  // se crea el enlace de datos con el viewmodel... livedata + binding, a medida que se actualiza el viewmodel se actualiza la vista si es requerido...

        mPreferences = AddressPreferences.getInstance(getActivity());

        return mWalletBindingView.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }

        loadView();

    }


    /**
     *
     * Inicializa la vista de con el balance actual,
     * en caso que no haya conoexion de red notifica el ultimo balance almacenado
     */
    private void loadView() {

        Address address = null;

        showProgressDialog();

        if (mPreferences.getAddressPreferences().equals("")) {
            address = new Address(getResources().getString(R.string.address_wallet_initialize), "", false);
        } else {
            mWalletBindingView.viewQR.textViewTitleAddressId.setText(R.string.address_enabled);
            address = new Address(mPreferences.getAddressPreferences(), "", true);
        }

        if (!NetworkUtils.isNetworkConnected(getActivity())) {

            if (mPreferences.getLastAddressBalance() != null) {
                mWalletViewModel.setBalanceAddress(mPreferences.getLastAddressBalance());
            }
        }


        if (address != null) {
            mWalletViewModel.setAddress(address);
        }

        mWalletViewModel.getAddress().observe(getViewLifecycleOwner(), mAddressObserver);
        mWalletViewModel.getBalanceAddress().observe(getViewLifecycleOwner(), mBalanceObserver);

        shareAddress();
    }

    /**
     *
     * Este observer permite que en le caso de que exista una direccion permite generar el qr en la vista
     */
    private Observer<Address> mAddressObserver = address -> {
        if (address.isIsAddressGenerate()) {
            if (mProgress != null)
                mProgress.show();
            mWalletViewModel.generateBalanceAddressFromServer();
            generateQR(address.getAddress());
        }
    };

    /**
     *
     * Este observer actualiza el balance cada vez que se requiera,
     *  ya sea desde el server o desde las SharedPreferences seteadas
     */
    private Observer<BalanceAddress> mBalanceObserver = balance -> {
        mPreferences.setLastBalancePreferences(balance);
    };


    /**
     *
     * Se inicializa un progrress dialog,
     *  es utilizado para cargar @loadView.
     */
    private void showProgressDialog() {
        mProgress = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setTitle(getResources().getString(R.string.address_dialog_title_wallet));
        mProgress.setMessage(getResources().getString(R.string.address_dialog_msg));
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
    }

    /**
     *
     * genera el codigo QR y lo setea en un imgview
     * @param address
     */
    public void generateQR(String address) {
        getActivity().runOnUiThread(() -> {
            Bitmap bmp = null;
            try {
                bmp = QrConvertHelper.qrConvert(address, getActivity());

                ImageView imageView = mWalletBindingView.viewQR.imageViewQRAddressId;
                imageView.setImageTintMode(null); // se elimina el tint al imageview por defecto

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                imageView.setImageBitmap(bmp);

                Glide.with(this)
                        .load(stream.toByteArray())
                        .error(R.drawable.preview_qr)
                        .circleCrop()
                        .into(imageView);


            } catch (WriterException e) {
                e.printStackTrace();
            } finally {
                if (mProgress != null)
                    mProgress.hide();
            }
        });
    }

    /**
     *
     * Esta funcion permite compartir la address,
     * No fue algo solicitado, sin embargo facilita el trabajo de obtener la direccion para realizar transferencias....
     */
    private void shareAddress() {

        mWalletBindingView.floatingSharedId.setOnClickListener(v -> {

            if (!mWalletViewModel.getAddress().getValue().getAddress().isEmpty()) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dirección");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mWalletViewModel.getAddress().getValue().getAddress());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);

            } else {
                Toast.makeText(getActivity(), "No posee dirección asociada para comppartir", Toast.LENGTH_SHORT).show();
            }
        });
    }

}