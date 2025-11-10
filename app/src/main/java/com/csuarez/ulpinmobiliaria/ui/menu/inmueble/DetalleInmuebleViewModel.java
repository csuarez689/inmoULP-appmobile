package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;

import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<String> mError;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Boolean> mCargando;

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
        mInmueble = new MutableLiveData<>();
        mError = new MutableLiveData<>();
        mMensaje = new MutableLiveData<>();
        mCargando = new MutableLiveData<>();
    }

    public LiveData<Inmueble> getMInmueble() {
        return mInmueble;
    }

    public LiveData<String> getMError() {
        return mError;
    }

    public LiveData<String> getMMensaje() {
        return mMensaje;
    }

    public LiveData<Boolean> getMCargando() {
        return mCargando;
    }

    public void setInmueble(Inmueble inmueble) {
        mInmueble.setValue(inmueble);
    }

    public void cargarInmuebleDesdeArgumentos(Bundle arguments) {
        if (arguments != null) {
            Inmueble inmueble = (Inmueble) arguments.getSerializable("inmueble");
            if (inmueble != null) {
                mInmueble.setValue(inmueble);
            }
        }
    }

    public void actualizarDisponibilidad(boolean disponible) {
        Inmueble inmueble = mInmueble.getValue();
        if (inmueble == null) {
            mError.setValue("Error: No hay inmueble cargado");
            return;
        }

        inmueble.setDisponible(disponible);
        mCargando.setValue(true);

        String token = ApiClient.getToken(getApplication());

        Call<Inmueble> call = ApiClient.getClient().actualizarInmueble("Bearer " + token, inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                mCargando.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    mInmueble.setValue(response.body());
                    mMensaje.setValue("Disponibilidad actualizada correctamente");
                } else {
                    mError.setValue("Error al actualizar la disponibilidad");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mCargando.setValue(false);
                mError.setValue("Error de servidor");
            }
        });
    }
}