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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csuarez.ulpinmobiliaria.databinding.FragmentDetalleContratoBinding;
import com.csuarez.ulpinmobiliaria.models.Contrato;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.models.Pago;
import com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity;
import com.csuarez.ulpinmobiliaria.utils.FormatUtils;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetalleContratoFragment extends Fragment {

    private DetalleContratoViewModel detalleVm;
    private FragmentDetalleContratoBinding binding;
    private Inmueble inmueble;
    private PagoAdapter pagoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detalleVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(DetalleContratoViewModel.class);
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);
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

        // configurar recyclerview de pagos
        binding.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        pagoAdapter = new PagoAdapter(new ArrayList<>());
        binding.rvPagos.setAdapter(pagoAdapter);

        // obtener inmueble del bundle
        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            detalleVm.cargarContrato(inmueble.getIdInmueble());
        }

        // observer para el contrato
        detalleVm.getMContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                if (contrato.getInquilino() != null) {
                    binding.tvInquilino.setText(contrato.getInquilino().getNombreCompleto());
                }
                
                if (contrato.getInmueble() != null) {
                    binding.tvInmueble.setText(contrato.getInmueble().getDireccion());
                }
                
                binding.tvFechaInicio.setText(FormatUtils.formatearFecha(contrato.getFechaInicio()));
                binding.tvFechaFin.setText(FormatUtils.formatearFecha(contrato.getFechaFinalizacion()));
                binding.tvMontoAlquiler.setText(FormatUtils.formatearMonto(contrato.getMontoAlquiler()));
            }
        });

        // observer para los pagos
        detalleVm.getMPagos().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                pagoAdapter.actualizarLista(pagos);
            }
        });

        // observer para lista vacia
        detalleVm.getMPagosVacio().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean vacio) {
                if (vacio) {
                    binding.tvSinPagos.setVisibility(View.VISIBLE);
                    binding.rvPagos.setVisibility(View.GONE);
                } else {
                    binding.tvSinPagos.setVisibility(View.GONE);
                    binding.rvPagos.setVisibility(View.VISIBLE);
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}