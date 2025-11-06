package com.csuarez.ulpinmobiliaria.ui.menu.contrato;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.models.Contrato;
import com.csuarez.ulpinmobiliaria.models.Inquilino;
import com.csuarez.ulpinmobiliaria.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<Inquilino> mInquilino = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getMInquilino() {
        return mInquilino;
    }

    public LiveData<String> getMError() {
        return mError;
    }

    public void cargarInquilino(int idInmueble) {
        String token = ApiClient.getToken(getApplication());
        Call<Contrato> llamada = ApiClient.getClient().getContratoPorInmueble("Bearer " + token, idInmueble);

        llamada.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Contrato contrato = response.body();
                    if (contrato.getInquilino() != null) {
                        mInquilino.setValue(contrato.getInquilino());
                    } else {
                        mError.setValue("No se encontró información del inquilino");
                    }
                } else {
                    mError.setValue("Error al cargar información del inquilino");
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                mError.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}