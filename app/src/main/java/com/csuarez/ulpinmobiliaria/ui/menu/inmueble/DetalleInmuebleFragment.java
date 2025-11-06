package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.databinding.FragmentDetalleInmuebleBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.google.android.material.snackbar.Snackbar;

public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel detalleVm;
    private FragmentDetalleInmuebleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detalleVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DetalleInmuebleViewModel.class);
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Cargar inmueble desde argumentos
        detalleVm.cargarInmuebleDesdeArgumentos(getArguments());

        // Observer para el inmueble
        detalleVm.getMInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                if (inmueble != null) {
                    mostrarDatosInmueble(inmueble);
                }
            }
        });

        // Observer para errores
        detalleVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(0xFFE57373)
                            .show();
                }
            }
        });

        // Observer para mensajes
        detalleVm.getMMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if (mensaje != null && !mensaje.isEmpty()) {
                    Snackbar.make(binding.getRoot(), mensaje, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // Listener para el switch de disponibilidad
        binding.switchDisponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    detalleVm.actualizarDisponibilidad(isChecked);
                }
            }
        });

        return root;
    }

    private void mostrarDatosInmueble(Inmueble inmueble) {
        binding.tvDireccionDetalle.setText(inmueble.getDireccion());
        binding.tvTipoDetalle.setText(inmueble.getTipo());
        binding.tvUsoDetalle.setText(inmueble.getUso());
        binding.tvAmbientesDetalle.setText(String.valueOf(inmueble.getAmbientes()));
        binding.tvSuperficieDetalle.setText(inmueble.getSuperficie() + " m\u00b2");
        binding.tvPrecioDetalle.setText(inmueble.getPrecioFormateado());
        binding.switchDisponible.setChecked(inmueble.getDisponible() != null && inmueble.getDisponible());

        // Cargar imagen con Glide
        String imageUrl = inmueble.getImagenUrl();
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.ivInmuebleDetalle);
        } else {
            binding.ivInmuebleDetalle.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}