package com.csuarez.ulpinmobiliaria;

import android.app.Application;
import android.text.Editable;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mAgitar = new MutableLiveData<>();

    private MutableLiveData<Boolean> mLoggedIn = new MutableLiveData<>();

    private  long lastShakeTime=0;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMError() {
        return mError;
    }

    public LiveData<Boolean> getMAgitar() {
        return mAgitar;
    }


    public LiveData<Boolean> getMLoggedIn() {
        return mLoggedIn;
    }

    public void login(Editable usuario, Editable clave) {


        String email = usuario.toString();
        String pass = clave.toString();

        if (email.isEmpty()) {
            mError.setValue("Ingrese un correo electrónico");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mError.setValue("Ingrese un correo electrónico válido");
            return;
        }
        if (pass.isEmpty()) {
            mError.setValue("Ingrese una clave");
            return;
        }

        ApiClient.ApiService api = ApiClient.getClient();
        Call<String> llamada = api.login(email, pass);
        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    ApiClient.saveToken(getApplication(),token);
                    mLoggedIn.postValue(true);
                }
                else {
                        mError.postValue("Usuario o contraseña incorrecta");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                mError.postValue("Error Servidor");
            }
        });
    }

    public void checkAccel(float x, float y, float z) {
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
        long currentTime = System.currentTimeMillis();

        if (acceleration > 12 && (currentTime - lastShakeTime) > 2000) {
            lastShakeTime = currentTime;
            mAgitar.setValue(true);
        }
    }

    public void resetAccel(){
        mAgitar.setValue(false);
    }






}
