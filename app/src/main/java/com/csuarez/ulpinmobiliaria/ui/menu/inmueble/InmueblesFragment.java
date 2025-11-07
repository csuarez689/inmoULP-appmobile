package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.databinding.FragmentInmueblesBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private InmueblesViewModel inmueblesVm;
    private FragmentInmueblesBinding binding;
    private InmueblesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inmueblesVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // configurar recyclerview una sola vez
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvInmuebles.setLayoutManager(layoutManager);

        // observer para el loader
        inmueblesVm.getMCargando().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        // observer para errores
        inmueblesVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                SnackbarUtils.mostrarError(binding.getRoot(), s);
            }
        });

        // observer para visibilidad de lista vacia
        inmueblesVm.getMListaVacia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean vacia) {
                if (vacia) {
                    binding.tvListaVacia.setVisibility(View.VISIBLE);
                    binding.rvInmuebles.setVisibility(View.GONE);
                } else {
                    binding.tvListaVacia.setVisibility(View.GONE);
                    binding.rvInmuebles.setVisibility(View.VISIBLE);
                }
            }
        });

        // observer para la lista de inmuebles
        inmueblesVm.getMLista().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> listaInmuebles) {
                if (adapter == null) {
                    adapter = new InmueblesAdapter(listaInmuebles, getLayoutInflater(), getContext(), new InmueblesAdapter.OnInmuebleClickListener() {
                        @Override
                        public void onInmuebleClick(Inmueble inmueble) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("inmueble", inmueble);
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu)
                                    .navigate(R.id.action_nav_inmuebles_to_detalleInmuebleFragment, bundle);
                        }
                    });
                    binding.rvInmuebles.setAdapter(adapter);
                }
            }
        });

        inmueblesVm.cargarInmuebles();

        // fab para agregar inmueble
        binding.fabAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu)
                        .navigate(R.id.action_nav_inmuebles_to_agregarInmuebleFragment);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
    }
}