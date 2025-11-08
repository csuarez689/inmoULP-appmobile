package com.csuarez.ulpinmobiliaria.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    // ocultar teclado desde una vista
    public static void ocultarTeclado(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // ocultar teclado desde una actividad
    public static void ocultarTeclado(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }
}