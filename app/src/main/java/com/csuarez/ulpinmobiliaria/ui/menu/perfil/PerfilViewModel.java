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
    private MutableLiveData<String> mMensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> mEditMode = new MutableLiveData<>(false);
    private MutableLiveData<String> mError = new MutableLiveData<>();


    private static final String REGEX_EMAIL = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final String REGEX_DNI = "^\\d{7,8}$";
    private static final String REGEX_TEL = "^\\d{6,15}$";
    private static final String REGEX_TEXTO = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$";

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Propietario> getMPropietario() {
        return mPropietario;
    }

    public LiveData<String> getMMensaje() {
        return mMensaje;
    }

    public LiveData<Boolean> getMEditMode() {
        return mEditMode;
    }

    public LiveData<String> getMError() {
        return mError;
    }

    //cargar datos del perfil
    public void cargarPerfil() {
        String token = ApiClient.getToken(getApplication());
        Call<Propietario> call = ApiClient.getClient().getPropietario("Bearer " + token);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    mPropietario.setValue(response.body());
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

    public void onBotonPrincipalClick(String nombre, String apellido, String dni, String telefono, String email) {
        Boolean modo = mEditMode.getValue();
        if (modo != null && modo) {
            Propietario propietario = new Propietario(
                    mPropietario.getValue().getIdPropietario(),
                    nombre,
                    apellido,
                    dni,
                    telefono,
                    email
            );
            if (propietario != null && validarCampos(propietario)) {
                guardarCambios(propietario);
            }
        } else {
            mEditMode.setValue(true);
        }
    }

    public void guardarCambios(Propietario propietario) {
        String token = ApiClient.getToken(getApplication());
        Call<Propietario> call = ApiClient.getClient().actualizarPropietario("Bearer " + token, propietario);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    mPropietario.setValue(response.body());
                    mMensaje.setValue("Perfil actualizado correctamente");
                    mEditMode.setValue(false);
                } else {
                    mError.setValue("Error al actualizar el perfil");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mError.setValue("Error de conexión al guardar");
            }
        });
    }

    private boolean validarCampos(Propietario p) {
        if (p.getNombre().isEmpty() || p.getApellido().isEmpty() ||
                p.getDni().isEmpty() || p.getTelefono().isEmpty() || p.getEmail().isEmpty()) {
            mError.setValue("No deje campos vacíos.");
            return false;
        }

        if (!p.getNombre().matches(REGEX_TEXTO)) {
            mError.setValue("El nombre contiene caracteres inválidos.");
            return false;
        }

        if (!p.getApellido().matches(REGEX_TEXTO)) {
            mError.setValue("El apellido contiene caracteres inválidos.");
            return false;
        }

        if (!p.getDni().matches(REGEX_DNI)) {
            mError.setValue("El DNI debe tener 7 u 8 números.");
            return false;
        }

        if (!p.getTelefono().matches(REGEX_TEL)) {
            mError.setValue("El teléfono no es válido.");
            return false;
        }

        if (!p.getEmail().matches(REGEX_EMAIL)) {
            mError.setValue("El email no es válido.");
            return false;
        }

        return true;
    }
}