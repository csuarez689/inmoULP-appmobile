package com.csuarez.ulpinmobiliaria.utils;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.csuarez.ulpinmobiliaria.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {

    // muestra un snackbar de exito (verde)
    public static void mostrarExito(View view, String mensaje) {
        mostrarExito(view, mensaje, false);
    }

    public static void mostrarExito(View view, String mensaje, boolean esCorto) {
        if (mensaje != null && !mensaje.isEmpty()) {
            int duracion = esCorto ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG;
            Snackbar.make(view, mensaje, duracion)
                    .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.snackbarSuccess))
                    .show();
        }
    }

    // muestra un snackbar de error (rojo)
    public static void mostrarError(View view, String mensaje) {
        mostrarError(view, mensaje, false);
    }

    public static void mostrarError(View view, String mensaje, boolean esCorto) {
        if (mensaje != null && !mensaje.isEmpty()) {
            int duracion = esCorto ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG;
            Snackbar.make(view, mensaje, duracion)
                    .setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.snackbarError))
                    .show();
        }
    }
}