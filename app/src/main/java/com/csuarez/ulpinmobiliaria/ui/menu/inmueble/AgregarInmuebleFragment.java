package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.R;

public class AgregarInmuebleFragment extends Fragment {

    private AgregarInmuebleViewModel mViewModel;

    public static AgregarInmuebleFragment newInstance() {
        return new AgregarInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_inmueble, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AgregarInmuebleViewModel.class);
        // TODO: Use the ViewModel
    }

}