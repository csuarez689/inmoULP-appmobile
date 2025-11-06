package com.csuarez.ulpinmobiliaria.ui.menu.contrato;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.models.Contrato;
import com.csuarez.ulpinmobiliaria.models.Pago;
import com.csuarez.ulpinmobiliaria.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {

    private MutableLiveData<Contrato> mContrato = new MutableLiveData<>();
    private MutableLiveData<List<Pago>> mPagos = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mPagosVacio = new MutableLiveData<>();

    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getMContrato() {
        return mContrato;
    }

    public LiveData<List<Pago>> getMPagos() {
        return mPagos;
    }

    public LiveData<String> getMError() {
        return mError;
    }

    public LiveData<Boolean> getMPagosVacio() {
        return mPagosVacio;
    }

    public void cargarContrato(int idInmueble) {
        String token = ApiClient.getToken(getApplication());
        Call<Contrato> llamada = ApiClient.getClient().getContratoPorInmueble("Bearer " + token, idInmueble);

        llamada.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Contrato contrato = response.body();
                    mContrato.setValue(contrato);
                    // Cargar pagos del contrato
                    cargarPagos(contrato.getIdContrato());
                } else {
                    mError.setValue("Error al cargar información del contrato");
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                mError.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void cargarPagos(int idContrato) {
        String token = ApiClient.getToken(getApplication());
        Call<List<Pago>> llamada = ApiClient.getClient().getPagosPorContrato("Bearer " + token, idContrato);

        llamada.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pago> pagos = response.body();
                    // Ordenar en orden inverso (más reciente primero)
                    java.util.Collections.reverse(pagos);
                    mPagos.setValue(pagos);
                    mPagosVacio.setValue(pagos.isEmpty());
                } else {
                    mPagosVacio.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                mError.setValue("Error al cargar pagos: " + t.getMessage());
                mPagosVacio.setValue(true);
            }
        });
    }
}