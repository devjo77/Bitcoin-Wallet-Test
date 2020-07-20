package com.beetrack.bitcoin_wallet.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beetrack.bitcoin_wallet.R;
import com.beetrack.bitcoin_wallet.adapter.TransactionAdapterDiff;
import com.beetrack.bitcoin_wallet.databinding.FragmentTransactionsBinding;
import com.beetrack.bitcoin_wallet.helper.AddressPreferences;
import com.beetrack.bitcoin_wallet.model.Address;
import com.beetrack.bitcoin_wallet.utils.NetworkUtils;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel mTransactionsViewModel;
    private FragmentTransactionsBinding mTransactionsBindingView;
    private AddressPreferences mPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTransactionsBindingView = FragmentTransactionsBinding.inflate(inflater, container, false); //se crea el enlace a la vista...

        mTransactionsBindingView.setLifecycleOwner(getActivity()); // se asigna el control del ciclo de vida al binding....

        mTransactionsViewModel = new ViewModelProvider(getActivity()).get(TransactionsViewModel.class);  // se crea el viemodel para persitir los atributos y sus cambios en tiempo real...

        mTransactionsBindingView.setTransactionsViewModel(mTransactionsViewModel);  // se crea el enlace de datos con el viewmodel... livedata + binding, a medida que se actualiza el viewmodel se actualiza la vista si es requerido...

        mPreferences = AddressPreferences.getInstance(getActivity());

        return mTransactionsBindingView.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        if( !NetworkUtils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), getResources().getString(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }

        loadView();

    }

    /**
     *
     * carga los aspectos fundamentales de la vista
     *  incializa la lista de transacciones desde la base de datos...
     */
    private void loadView() {

        createRecyclerViewDiff();

        Address address = null;

        if (mPreferences.getAddressPreferences().equals("")) {
            address = new Address("", "", false);
        } else {
            address = new Address(mPreferences.getAddressPreferences(), "", true);
        }

        if (address != null) {
            mTransactionsViewModel.setAddress(address);
        }

        mTransactionsViewModel.getAddress().observe(getViewLifecycleOwner(), mAddressObserver);
    }


    /**
     *  crear el recyclerview y se le seta un adapter que cargara los datos de la vista de la base de datos.
     */
    private void createRecyclerViewDiff() {

        RecyclerView recyclerView = mTransactionsBindingView.recyclerTransactionId;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        TransactionAdapterDiff adapter = new TransactionAdapterDiff();
        mTransactionsViewModel.getTransactionsDbLivePagedList().observe(getActivity(), adapter::submitList);
        recyclerView.setAdapter(adapter);
    }


    private Observer<Address> mAddressObserver = address -> {
        mTransactionsViewModel.getTransactionsFromServer();
    };


}

