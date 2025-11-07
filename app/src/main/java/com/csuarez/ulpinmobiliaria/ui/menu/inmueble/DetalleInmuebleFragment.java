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
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;

public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel detalleVm;
    private FragmentDetalleInmuebleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detalleVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DetalleInmuebleViewModel.class);
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        detalleVm.cargarInmuebleDesdeArgumentos(getArguments());

        // observer para el inmueble
        detalleVm.getMInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                binding.tvDireccionDetalle.setText(inmueble.getDireccion());
                binding.tvTipoDetalle.setText(inmueble.getTipo());
                binding.tvUsoDetalle.setText(inmueble.getUso());
                binding.tvAmbientesDetalle.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvSuperficieDetalle.setText(inmueble.getSuperficie() + " m\u00b2");
                binding.tvPrecioDetalle.setText(inmueble.getPrecioFormateado());
                binding.switchDisponible.setChecked(inmueble.getDisponible() != null && inmueble.getDisponible());

                String imageUrl = inmueble.getImagenUrl();
                if (imageUrl != null) {
                    Glide.with(DetalleInmuebleFragment.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(binding.ivInmuebleDetalle);
                } else {
                    binding.ivInmuebleDetalle.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        });

        // observer para errores
        detalleVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                SnackbarUtils.mostrarError(binding.getRoot(), error);
            }
        });

        // observer para mensajes
        detalleVm.getMMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                SnackbarUtils.mostrarExito(binding.getRoot(), mensaje, true);
            }
        });

        // listener para el switch de disponibilidad
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}