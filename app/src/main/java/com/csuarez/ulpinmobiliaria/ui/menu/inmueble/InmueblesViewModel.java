package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    
    private MutableLiveData<List<Inmueble>> mLista = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCargando = new MutableLiveData<>();
    private MutableLiveData<Boolean> mListaVacia = new MutableLiveData<>(true);

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getMLista() {
        return mLista;
    }

    public LiveData<String> getMError() {
        return mError;
    }
    
    public LiveData<Boolean> getMCargando() {
        return mCargando;
    }
    
    public LiveData<Boolean> getMListaVacia() {
        return mListaVacia;
    }

    public void cargarInmuebles() {
        mCargando.setValue(true);
        
        String token = ApiClient.getToken(getApplication());
        Call<List<Inmueble>> call = ApiClient.getClient().getInmuebles("Bearer " + token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(@NonNull Call<List<Inmueble>> call, @NonNull Response<List<Inmueble>> response) {
                mCargando.setValue(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> inmuebles = response.body();
                    mLista.setValue(inmuebles);
                    mListaVacia.setValue(inmuebles.isEmpty());
                } else {
                    mError.setValue("Error al cargar inmuebles");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Inmueble>> call, @NonNull Throwable t) {
                mCargando.setValue(false);
                mError.setValue("Error de servidor");
                mListaVacia.setValue(true);
            }
        });
    }
}