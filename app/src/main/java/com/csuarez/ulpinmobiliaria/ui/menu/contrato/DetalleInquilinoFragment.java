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

        // Obtener inmueble del bundle
        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                detalleVm.cargarInquilino(inmueble.getIdInmueble());
            }
        }

        // Observer para el inquilino
        detalleVm.getMInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                if (inquilino != null) {
                    mostrarDatosInquilino(inquilino);
                }
            }
        });

        // Observer para errores
        detalleVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    SnackbarUtils.mostrarError(binding.getRoot(), error);
                }
            }
        });

        return root;
    }

    private void mostrarDatosInquilino(Inquilino inquilino) {
        binding.tvNombreInquilino.setText(inquilino.getNombre());
        binding.tvApellidoInquilino.setText(inquilino.getApellido());
        binding.tvDniInquilino.setText(inquilino.getDni());
        binding.tvEmailInquilino.setText(inquilino.getEmail());
        binding.tvTelefonoInquilino.setText(inquilino.getTelefono());
        binding.tvGaranteInquilino.setText(inquilino.getGarante());
        binding.tvTelefonoGaranteInquilino.setText(inquilino.getTelefonoGarante());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}