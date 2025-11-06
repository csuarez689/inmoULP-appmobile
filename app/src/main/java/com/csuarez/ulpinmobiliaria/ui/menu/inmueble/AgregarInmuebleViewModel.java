package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.network.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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

    // Regex para validaciones
    private static final String REGEX_LATITUD = "^-?([0-8]?[0-9]|90)(\\.[0-9]{1,10})?$"; // -90 a 90
    private static final String REGEX_LONGITUD = "^-?(1[0-7][0-9]|[0-9]?[0-9]|180)(\\.[0-9]{1,10})?$"; // -180 a 180
    private static final String REGEX_PRECIO = "^[0-9]+(\\.[0-9]{1,2})?$"; // Números positivos con hasta 2 decimales
    private static final String REGEX_ENTERO_POSITIVO = "^[1-9][0-9]*$"; // Enteros positivos (no cero)
    private static final String REGEX_DIRECCION = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ\\s.,#°-]{5,100}$"; // Dirección válida

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

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri uri = data.getData();
            mImagenUri.setValue(uri);
        }
    }

    public void recibirFotoDeCamara(ActivityResult result, Uri uri) {
        if (result.getResultCode() == RESULT_OK) {
            mImagenUri.setValue(uri);
        }
    }

    public void crearInmueble(String direccion, String tipo, String uso, String ambientes,
                              String superficie, String precio, String latitud, String longitud) {

        // Validaciones
        if (!validarCampos(direccion, tipo, uso, ambientes, superficie, precio, latitud, longitud)) {
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

        // Transformar imagen a bytes
        byte[] imagenBytes = transformarImagen();
        if (imagenBytes == null || imagenBytes.length == 0) {
            mError.setValue("Error al procesar la imagen");
            return;
        }

        // Convertir objeto Inmueble a JSON
        Gson gson = new Gson();
        String inmuebleJson = gson.toJson(nuevoInmueble);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);

        // Crear MultipartBody.Part para la imagen
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagenBytes);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);

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
                                  String superficie, String precio, String latitud, String longitud) {

        // Validar dirección
        if (direccion == null || direccion.trim().isEmpty()) {
            mError.setValue("La dirección es obligatoria");
            return false;
        }
        if (!direccion.trim().matches(REGEX_DIRECCION)) {
            mError.setValue("La dirección debe tener entre 5 y 100 caracteres válidos");
            return false;
        }

        // Validar tipo
        if (tipo == null || tipo.trim().isEmpty()) {
            mError.setValue("El tipo es obligatorio");
            return false;
        }

        // Validar uso
        if (uso == null || uso.trim().isEmpty()) {
            mError.setValue("El uso es obligatorio");
            return false;
        }

        // Validar ambientes
        if (ambientes == null || ambientes.trim().isEmpty()) {
            mError.setValue("Los ambientes son obligatorios");
            return false;
        }
        if (!ambientes.trim().matches(REGEX_ENTERO_POSITIVO)) {
            mError.setValue("Los ambientes deben ser un número entero positivo");
            return false;
        }
        int numAmbientes = Integer.parseInt(ambientes.trim());
        if (numAmbientes > 50) {
            mError.setValue("El número de ambientes no puede ser mayor a 50");
            return false;
        }

        // Validar superficie
        if (superficie == null || superficie.trim().isEmpty()) {
            mError.setValue("La superficie es obligatoria");
            return false;
        }
        if (!superficie.trim().matches(REGEX_ENTERO_POSITIVO)) {
            mError.setValue("La superficie debe ser un número entero positivo");
            return false;
        }
        int numSuperficie = Integer.parseInt(superficie.trim());
        if (numSuperficie > 1000000) {
            mError.setValue("La superficie no puede ser mayor a 1.000.000 m²");
            return false;
        }

        // Validar precio
        if (precio == null || precio.trim().isEmpty()) {
            mError.setValue("El precio es obligatorio");
            return false;
        }
        if (!precio.trim().matches(REGEX_PRECIO)) {
            mError.setValue("El precio debe ser un número positivo válido");
            return false;
        }
        double numPrecio = Double.parseDouble(precio.trim());
        if (numPrecio <= 0) {
            mError.setValue("El precio debe ser mayor a 0");
            return false;
        }
        if (numPrecio > 999999999.99) {
            mError.setValue("El precio es demasiado alto");
            return false;
        }

        // Validar latitud
        if (latitud == null || latitud.trim().isEmpty()) {
            mError.setValue("La latitud es obligatoria");
            return false;
        }
        if (!latitud.trim().matches(REGEX_LATITUD)) {
            mError.setValue("La latitud debe estar entre -90 y 90");
            return false;
        }

        // Validar longitud
        if (longitud == null || longitud.trim().isEmpty()) {
            mError.setValue("La longitud es obligatoria");
            return false;
        }
        if (!longitud.trim().matches(REGEX_LONGITUD)) {
            mError.setValue("La longitud debe estar entre -180 y 180");
            return false;
        }

        // Validar imagen
        if (mImagenUri.getValue() == null) {
            mError.setValue("Debe seleccionar una imagen");
            return false;
        }

        return true;
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = mImagenUri.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}