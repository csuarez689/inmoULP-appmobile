package com.csuarez.ulpinmobiliaria.ui.menu.home;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class HomeViewModel extends AndroidViewModel {
    private FusedLocationProviderClient fused;
    private MutableLiveData<Mapa> mMapa = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        fused = LocationServices.getFusedLocationProviderClient(getApplication());
    }

    public LiveData<Mapa> getMMapa() {
        return mMapa;
    }


    public void getUbicacionInmobiliaria() {
        Location ubicacion = new Location("manual");
        ubicacion.setLatitude(-33.312367);
        ubicacion.setLongitude(-66.332135);
        mMapa.setValue(new Mapa(ubicacion));
    }


}


