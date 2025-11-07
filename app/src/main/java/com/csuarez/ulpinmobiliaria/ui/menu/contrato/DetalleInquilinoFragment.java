package com.csuarez.ulpinmobiliaria.ui.menu.contrato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.csuarez.ulpinmobiliaria.databinding.FragmentDetalleInquilinoBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.models.Inquilino;
import com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;

public class DetalleInquilinoFragment extends Fragment {

    private DetalleInquilinoViewModel detalleVm;
    private FragmentDetalleInquilinoBinding binding;
    private Inmueble inmueble;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detalleVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(DetalleInquilinoViewModel.class);
        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // observer para el loader
        detalleVm.getMCargando().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        // obtener inmueble del bundle
        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            detalleVm.cargarInquilino(inmueble.getIdInmueble());
        }

        // observer para el inquilino
        detalleVm.getMInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                binding.tvNombreInquilino.setText(inquilino.getNombre());
                binding.tvApellidoInquilino.setText(inquilino.getApellido());
                binding.tvDniInquilino.setText(inquilino.getDni());
                binding.tvEmailInquilino.setText(inquilino.getEmail());
                binding.tvTelefonoInquilino.setText(inquilino.getTelefono());
                binding.tvGaranteInquilino.setText(inquilino.getGarante());
                binding.tvTelefonoGaranteInquilino.setText(inquilino.getTelefonoGarante());
            }
        });

        // observer para errores
        detalleVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                SnackbarUtils.mostrarError(binding.getRoot(), error);
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