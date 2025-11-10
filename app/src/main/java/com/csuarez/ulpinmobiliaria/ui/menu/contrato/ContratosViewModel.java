package com.csuarez.ulpinmobiliaria.ui.menu.contrato;

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

public class ContratosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> mInmueblesConContrato = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mListaVacia = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCargando = new MutableLiveData<>();

    public ContratosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getMInmueblesConContrato() {
        return mInmueblesConContrato;
    }

    public LiveData<String> getMError() {
        return mError;
    }

    public LiveData<Boolean> getMListaVacia() {
        return mListaVacia;
    }

    public LiveData<Boolean> getMCargando() {
        return mCargando;
    }

    public void cargarInmueblesConContrato() {
        mCargando.setValue(true);

        String token = ApiClient.getToken(getApplication());
        Call<List<Inmueble>> call = ApiClient.getClient().getInmueblesConContratoVigente("Bearer " + token);

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                mCargando.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> inmuebles = response.body();
                    mInmueblesConContrato.setValue(inmuebles);
                    mListaVacia.setValue(inmuebles.isEmpty());
                } else {
                    mError.setValue("Error al cargar contratos");
                    mListaVacia.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                mCargando.setValue(false);
                mError.setValue("Error de conexión: " + t.getMessage());
                mListaVacia.setValue(true);
            }
        });
    }
}