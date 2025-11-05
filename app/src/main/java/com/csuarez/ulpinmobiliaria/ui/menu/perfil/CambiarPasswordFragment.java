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
import com.google.android.material.snackbar.Snackbar;

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


        cambiarPasswordVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(0xFFE57373)
                            .show();
                }
            }
        });

        cambiarPasswordVm.getMSuccess().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msj) {
                    binding.etClaveActual.setText("");
                    binding.etNuevaClave.setText("");
                    binding.etConfirmarNuevaClave.setText("");
                    Snackbar.make(binding.getRoot(), msj, Snackbar.LENGTH_SHORT).show();
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