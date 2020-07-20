package com.beetrack.bitcoin_wallet.ui.address;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import androidx.navigation.Navigation;

import com.beetrack.bitcoin_wallet.R;
import com.beetrack.bitcoin_wallet.databinding.FragmentAddressBinding;
import com.beetrack.bitcoin_wallet.helper.AddressPreferences;
import com.beetrack.bitcoin_wallet.helper.QrConvertHelper;
import com.beetrack.bitcoin_wallet.model.Address;
import com.beetrack.bitcoin_wallet.model.BalanceAddress;
import com.beetrack.bitcoin_wallet.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;

/**
 *
 * @AddressFragment
 *   Permite que el usuario genera y almacen la address
 *
 */

public class AddressFragment extends Fragment {

    private AddressViewModel mAddressViewModel;
    private FragmentAddressBinding mBindingViewAddress;
    private ProgressDialog mProgress;
    private AddressPreferences mPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBindingViewAddress = FragmentAddressBinding.inflate(inflater, container, false); //se crea el enlace a la vista...

        mBindingViewAddress.setLifecycleOwner(getActivity()); // se asigna el control del ciclo de vida al binding....

        mAddressViewModel = new ViewModelProvider(getActivity()).get(AddressViewModel.class);  // se crea el viemodel para persitir los atributos y sus cambios en tiempo real...

        mBindingViewAddress.setViewModelAddress(mAddressViewModel);  // se crea el enlace de datos con el viewmodel... livedata + binding, a medida que se actualiza el viewmodel se actualiza la vista si es requerido...

        mPreferences = AddressPreferences.getInstance(getActivity()); //seteo las preferencias globalmente


        return mBindingViewAddress.getRoot();
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
     * carga los aspectos fundamentales de la vista
     *  En caso de que la address este almacenada la permite visualizar....
     *  En caso de que la addres no haya sido generada notifica que debe generarse...
     */
    private void loadView() {

        Address address = null;

        if (mPreferences.getAddressPreferences().equals("")) {
            address = new Address(getResources().getString(R.string.address_initialize), "", false);
        } else {
            mBindingViewAddress.viewQR.textViewTitleAddressId.setText(R.string.address_enabled);
            address = new Address(mPreferences.getAddressPreferences(), "", true);
        }

        if (address != null)
            mAddressViewModel.setAddress(address);

        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), mAddressObserver);
        mAddressViewModel.getSave().observe(getViewLifecycleOwner(), mSaveObserver);
    }

    private Observer<Address> mAddressObserver = address -> {

        if (address.isProgressDialogActive() && mProgress == null) {
            showProgressDialog();
        } else if (address.isIsAddressGenerate()) {
            generateQR(address.getAddress());
        } else {
            destoyProgressdialog();
        }
    };


    /**
     *
     *  Este Observer notifica que el usuario solicito guardar la address
     *  y llama @notifySaveAddress
     */
    private Observer<Boolean> mSaveObserver = save -> {

        if (save) {
            String address = mAddressViewModel.getAddress().getValue().getAddress();
            if (!address.equals("")) {
                if (mPreferences.getAddressPreferences().equals(address)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.address_duplicate), Toast.LENGTH_SHORT).show();
                } else {
                    notifySaveAddress(address);
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.address_disabled_error), Toast.LENGTH_SHORT).show();
            }
            mAddressViewModel.setSave(false);
        }
    };

    /**
     *  Almacena la address en un archivo a traves de SharedPreferences
     * @param address
     */
    private void saveAddressPreferences(String address) {
        AddressPreferences.getInstance(getActivity()).setAddressPreferences(address);
        mBindingViewAddress.viewQR.textViewTitleAddressId.setText(R.string.address_enabled);
        mBindingViewAddress.textViewNotificationId.setText("");
        mPreferences.setLastBalancePreferences(new BalanceAddress("0", "0", "0")); // se inicializa el balance address
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_wallet);

    }

    private void destoyProgressdialog() {
        if (mProgress != null) {
            mProgress.hide();
            mProgress = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAddressViewModel = null;
    }

    /**
     *
     * Se activa un progress dialog, para notificar al usurio que se esta generando la nueva address
     */
    private void showProgressDialog() {
        mProgress = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setTitle(getResources().getString(R.string.address_dialog_title));
        mProgress.setMessage(getResources().getString(R.string.address_dialog_msg));
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.show();
    }

    /**
     *
     *  Genera el dialog que permitira confirmar al usuario el almacenamiento de la address
     * @param address
     */
    public void notifySaveAddress(final String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.ask_before_saving));
        builder.setMessage(address);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.save), (dialog, id) -> {
            saveAddressPreferences(address);
            dialog.dismiss();
        });
        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), (dialog, id) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
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

                ImageView imageView = mBindingViewAddress.viewQR.imageViewQRAddressId;
                imageView.setImageTintMode(null); // se elimina el tint al imageview por defecto

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                imageView.setImageBitmap(bmp);

                Glide.with(this)
                        .load(stream.toByteArray())
                        .error(R.drawable.preview_qr)
                        .circleCrop()
                        .into(imageView);


                if (!mPreferences.getAddressPreferences().equals(address)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.address_enabled), Toast.LENGTH_SHORT).show();
                    mBindingViewAddress.textViewNotificationId.setText(R.string.address_for_save);
                }


            } catch (WriterException e) {
                e.printStackTrace();
            } finally {

                destoyProgressdialog();
            }
        });
    }
}