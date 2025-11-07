package com.csuarez.ulpinmobiliaria.ui.menu.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.databinding.FragmentCambiarPasswordBinding;
import com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;

public class CambiarPasswordFragment extends Fragment {

    private CambiarPasswordViewModel cambiarPasswordVm;
    private FragmentCambiarPasswordBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cambiarPasswordVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(CambiarPasswordViewModel.class);
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // observer para el loader
        cambiarPasswordVm.getMCargando().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean cargando) {
                MenuActivity activity = (MenuActivity) getActivity();
                if (activity != null) {
                    if (cargando) {
                        activity.mostrarLoader();
                    } else {
                        activity.ocultarLoader();
                    }
                }
            }
        });

        cambiarPasswordVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                SnackbarUtils.mostrarError(binding.getRoot(), error);
            }
        });

        cambiarPasswordVm.getMSuccess().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msj) {
                    binding.etClaveActual.setText("");
                    binding.etNuevaClave.setText("");
                    binding.etConfirmarNuevaClave.setText("");
                    SnackbarUtils.mostrarExito(binding.getRoot(), msj, true);
                    NavHostFragment.findNavController(CambiarPasswordFragment.this).popBackStack();

            }
        });

        binding.btnCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarPasswordVm.actualizarPassword(
                        binding.etClaveActual.getText().toString(),
                        binding.etNuevaClave.getText().toString(),
                        binding.etConfirmarNuevaClave.getText().toString()
                );

            }
        });




        return root;

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}