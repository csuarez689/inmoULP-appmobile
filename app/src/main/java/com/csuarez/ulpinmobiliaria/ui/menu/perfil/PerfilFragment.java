package com.csuarez.ulpinmobiliaria.ui.menu.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.R;
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

        perfilVm.getMEditMode().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEditMode) {
                cambiarModo(isEditMode);
            }
        });

        perfilVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Snackbar.make(binding.getRoot(), mensaje, Snackbar.LENGTH_SHORT).show();
            }
        });

        perfilVm.getMValError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(0xFFE57373)
                            .show();
                }
            }
        });



        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilVm.onBotonPrincipalClick(
                        binding.etNombre.getText().toString(),
                        binding.etApellido.getText().toString(),
                        binding.etDni.getText().toString(),
                        binding.etTelefono.getText().toString(),
                        binding.etEmail.getText().toString()
                );
            }
        });


        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu)
                        .navigate(R.id.action_nav_perfil_to_CambiarPasswordFragment);
            }
        });

        perfilVm.cargarPerfil();
                cambiarModo(false);
        return root;
    }

    private void cambiarModo(boolean isEditMode){
        String text= isEditMode ? "Guardar" : "Editar";
        binding.btnGuardar.setText(text);
        binding.etNombre.setEnabled(isEditMode);
        binding.etApellido.setEnabled(isEditMode);
        binding.etTelefono.setEnabled(isEditMode);
        binding.etEmail.setEnabled(isEditMode);
        binding.etDni.setEnabled(isEditMode);
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding=null;
    }

}