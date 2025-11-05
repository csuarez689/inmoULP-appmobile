package com.csuarez.ulpinmobiliaria.ui.menu.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarPasswordViewModel extends AndroidViewModel {

    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<String> mSuccess = new MutableLiveData<>();
    public CambiarPasswordViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<String> getMError() { return mError; }
    public LiveData<String> getMSuccess() { return mSuccess; }


    public void actualizarPassword(String actual, String nueva, String confirmar) {
        String error = validar(actual, nueva, confirmar);
        if (error != null) {
            mError.setValue(error);
            return;
        }

        String token = ApiClient.getToken(getApplication());
        Call<Void> call = ApiClient.getClient()
                .cambiarPassword("Bearer " + token, actual, nueva);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mSuccess.setValue("Contraseña actualizada correctamente.");
                } else if (response.code() == 400) {
                    mError.setValue("Contraseña actual incorrecta.");
                } else {
                    mError.setValue("Error al cambiar la contraseña.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mError.postValue("Error de conexión con el servidor.");
            }
        });
    }

    private String validar(String actual, String nueva, String confirmar) {
        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            return "Todos los campos son obligatorios.";
        }

        if (nueva.length() < 6) {
            return "La nueva contraseña debe tener al menos 6 caracteres.";
        }

        if (nueva.equals(actual)) {
            return "La nueva contraseña no puede ser igual a la actual.";
        }

        if (!nueva.equals(confirmar)) {
            return "Las contraseñas no coinciden.";
        }

        return null; // Todo correcto
    }
}