package com.csuarez.ulpinmobiliaria.ui.menu.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.SupportMapFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeVm;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeVm = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeVm.getMMapa().observe(getViewLifecycleOwner(), new Observer<Mapa>() {
            @Override
            public void onChanged(Mapa mapa) {
                SupportMapFragment mapFragment = (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(mapa);
                }
            }
        });

        homeVm.getUbicacionInmobiliaria();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}