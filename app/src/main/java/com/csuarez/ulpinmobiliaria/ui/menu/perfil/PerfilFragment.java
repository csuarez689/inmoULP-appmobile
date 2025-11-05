package com.csuarez.ulpinmobiliaria.ui.menu.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.databinding.FragmentPerfilBinding;
import com.csuarez.ulpinmobiliaria.models.Propietario;
import com.google.android.material.snackbar.Snackbar;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilVm;
    private FragmentPerfilBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        perfilVm=ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PerfilViewModel.class);
        binding=FragmentPerfilBinding.inflate(inflater,container,false);
        View root=binding.getRoot();


        perfilVm.getMPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    binding.etNombre.setText(propietario.getNombre());
                    binding.etApellido.setText(propietario.getApellido());
                    binding.etDni.setText(propietario.getDni());
                    binding.etEmail.setText(propietario.getEmail());
                    binding.etTelefono.setText(propietario.getTelefono());
                }
            }
        });

        perfilVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Snackbar.make(binding.getRoot(), mensaje, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.WHITE)
                        .setTextColor(Color.RED)
                        .show();
            }
        });


        perfilVm.showPerfil();
        return root;
    }



    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding=null;
    }

}