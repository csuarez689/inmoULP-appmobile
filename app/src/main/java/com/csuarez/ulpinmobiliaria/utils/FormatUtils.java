package com.csuarez.ulpinmobiliaria.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {

    private static final Locale LOCALE_ARGENTINA = new Locale("es", "AR");
    private static final String FORMATO_FECHA_API = "yyyy-MM-dd";
    private static final String FORMATO_FECHA_ARGENTINA = "dd/MM/yyyy";


    public static String formatearFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            return "";
        }

        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat(FORMATO_FECHA_API, Locale.getDefault());
            Date date = formatoEntrada.parse(fecha);

            SimpleDateFormat formatoSalida = new SimpleDateFormat(FORMATO_FECHA_ARGENTINA, LOCALE_ARGENTINA);
            return formatoSalida.format(date);
        } catch (Exception e) {
            return fecha; // si falla devolver la fecha original
        }
    }


    public static String formatearMonto(double monto) {
        NumberFormat formatoPrecio = NumberFormat.getCurrencyInstance(LOCALE_ARGENTINA);
        return formatoPrecio.format(monto);
    }


    public static String formatearMontoConPrefijo(double monto, String prefijo) {
        return prefijo + formatearMonto(monto);
    }
}