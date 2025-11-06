package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.databinding.FragmentInmueblesBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private InmueblesViewModel inmueblesVm;
    private FragmentInmueblesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inmueblesVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observer para errores
        inmueblesVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(0xFFE57373)
                            .show();
            }
        });

        // Observer para la lista de inmuebles
        inmueblesVm.getMLista().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> listaInmuebles) {
                if (listaInmuebles != null && !listaInmuebles.isEmpty()) {
                    binding.tvListaVacia.setVisibility(View.GONE);
                    binding.rvInmuebles.setVisibility(View.VISIBLE);
                    
                    InmueblesAdapter adapter = new InmueblesAdapter(listaInmuebles, getLayoutInflater(), getContext());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvInmuebles.setLayoutManager(layoutManager);
                    binding.rvInmuebles.setAdapter(adapter);
                } else {
                    binding.tvListaVacia.setVisibility(View.VISIBLE);
                    binding.rvInmuebles.setVisibility(View.GONE);
                }
            }
        });

        // Cargar inmuebles
        inmueblesVm.cargarInmuebles();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}