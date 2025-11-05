package com.csuarez.ulpinmobiliaria.ui.menu.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.models.Propietario;
import com.csuarez.ulpinmobiliaria.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();



    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Propietario> getMPropietario(){
        return mPropietario;
    }
    public LiveData<String> getMError(){
        return mError;
    }



    //cargar datos del perfil
    public void showPerfil() {
        String token = ApiClient.getToken(getApplication());
        Call<Propietario> call = ApiClient.getClient().getPropietario("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    mPropietario.postValue(response.body());
                } else {
                    mError.setValue("Error al obtener perfil");
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mError.setValue("Error de servidor");
            }
        });
    }
}