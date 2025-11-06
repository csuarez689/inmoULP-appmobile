package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.network.ApiClient;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<String> mError = new MutableLiveData<>();
    private MutableLiveData<String> mMensaje = new MutableLiveData<>();
    private MutableLiveData<Uri> mImagenUri = new MutableLiveData<>();

    public AgregarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMError() {
        return mError;
    }

    public LiveData<String> getMMensaje() {
        return mMensaje;
    }

    public LiveData<Uri> getMImagenUri() {
        return mImagenUri;
    }

    public void setImagenUri(Uri uri) {
        mImagenUri.setValue(uri);
    }

    public void crearInmueble(String direccion, String tipo, String uso, String ambientes,
                              String superficie, String precio, String latitud, String longitud, File imagenFile) {

        // Validaciones
        if (!validarCampos(direccion, tipo, uso, ambientes, superficie, precio, latitud, longitud, imagenFile)) {
            return;
        }

        // Crear objeto Inmueble
        Inmueble nuevoInmueble = new Inmueble();
        nuevoInmueble.setDireccion(direccion);
        nuevoInmueble.setTipo(tipo);
        nuevoInmueble.setUso(uso);
        nuevoInmueble.setAmbientes(Integer.parseInt(ambientes));
        nuevoInmueble.setSuperficie(Integer.parseInt(superficie));
        nuevoInmueble.setValor(Double.parseDouble(precio));
        nuevoInmueble.setLatitud(Double.parseDouble(latitud));
        nuevoInmueble.setLongitud(Double.parseDouble(longitud));

        // Convertir objeto Inmueble a JSON
        Gson gson = new Gson();
        String inmuebleJson = gson.toJson(nuevoInmueble);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("text/plain"), inmuebleJson);

        // Crear MultipartBody.Part para la imagen
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imagenFile);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", imagenFile.getName(), requestFile);

        // Llamada a la API
        String token = ApiClient.getToken(getApplication());
        Call<Inmueble> llamada = ApiClient.getClient().cargarInmueble(
                "Bearer " + token,
                imagenPart,
                inmuebleBody
        );

        llamada.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mMensaje.setValue("Inmueble creado correctamente");
                } else {
                    mError.setValue("Error al crear el inmueble");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mError.setValue("Error de servidor");
            }
        });
    }

    private boolean validarCampos(String direccion, String tipo, String uso, String ambientes,
                                  String superficie, String precio, String latitud, String longitud, File imagenFile) {

        if (direccion == null || direccion.trim().isEmpty()) {
            mError.setValue("La dirección es obligatoria");
            return false;
        }

        if (tipo == null || tipo.trim().isEmpty()) {
            mError.setValue("El tipo es obligatorio");
            return false;
        }

        if (uso == null || uso.trim().isEmpty()) {
            mError.setValue("El uso es obligatorio");
            return false;
        }

        if (ambientes == null || ambientes.trim().isEmpty()) {
            mError.setValue("Los ambientes son obligatorios");
            return false;
        }

        if (superficie == null || superficie.trim().isEmpty()) {
            mError.setValue("La superficie es obligatoria");
            return false;
        }

        if (precio == null || precio.trim().isEmpty()) {
            mError.setValue("El precio es obligatorio");
            return false;
        }

        if (latitud == null || latitud.trim().isEmpty()) {
            mError.setValue("La latitud es obligatoria");
            return false;
        }

        if (longitud == null || longitud.trim().isEmpty()) {
            mError.setValue("La longitud es obligatoria");
            return false;
        }

        if (imagenFile == null || !imagenFile.exists()) {
            mError.setValue("Debe seleccionar una imagen");
            return false;
        }

        // Validar que sean números válidos
        try {
            Integer.parseInt(ambientes);
            Integer.parseInt(superficie);
            Double.parseDouble(precio);
            Double.parseDouble(latitud);
            Double.parseDouble(longitud);
        } catch (NumberFormatException e) {
            mError.setValue("Los valores numéricos no son válidos");
            return false;
        }

        return true;
    }
}