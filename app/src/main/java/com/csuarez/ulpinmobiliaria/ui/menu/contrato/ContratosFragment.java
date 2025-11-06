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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.databinding.FragmentContratosBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.ui.menu.contrato.ContratoAdapter;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

public class ContratosFragment extends Fragment {

    private ContratosViewModel contratosVm;
    private FragmentContratosBinding binding;
    private ContratoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        contratosVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(ContratosViewModel.class);
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView
        binding.rvInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContratoAdapter(new ArrayList<>(), new ContratoAdapter.OnContratoClickListener() {
            @Override
            public void onVerInquilinoClick(Inmueble inmueble) {
                // Navegar al detalle del inquilino
                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble", inmueble);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu)
                        .navigate(R.id.action_nav_contratos_to_detalleInquilinoFragment, bundle);
            }

            @Override
            public void onVerContratoClick(Inmueble inmueble) {
                // Navegar al detalle del contrato
                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble", inmueble);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu)
                        .navigate(R.id.action_nav_contratos_to_detalleContratoFragment, bundle);
            }
        });
        binding.rvInquilinos.setAdapter(adapter);

        // Observers
        contratosVm.getMInmueblesConContrato().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                adapter.actualizarLista(inmuebles);
            }
        });

        contratosVm.getMListaVacia().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean vacia) {
                if (vacia) {
                    binding.tvListaVacia.setVisibility(View.VISIBLE);
                    binding.rvInquilinos.setVisibility(View.GONE);
                } else {
                    binding.tvListaVacia.setVisibility(View.GONE);
                    binding.rvInquilinos.setVisibility(View.VISIBLE);
                }
            }
        });

        contratosVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                SnackbarUtils.mostrarError(binding.getRoot(), error);
            }
        });

        // Cargar datos
        contratosVm.cargarInmueblesConContrato();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}