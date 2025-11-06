package com.csuarez.ulpinmobiliaria.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.models.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {

    public static final String BASE_URL="https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";
    private static Retrofit retrofit;


    public static ApiService getClient(){
        if (retrofit == null) {

        //interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        //gson
        Gson gson=new GsonBuilder()
                .setStrictness(Strictness.LENIENT)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        //retrofit
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        }
        return retrofit.create(ApiService.class);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("inmobiliaria_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("inmobiliaria_prefs", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }




    public interface ApiService {

        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String user, @Field("Clave") String pass);

        @GET("api/Propietarios")
        Call<Propietario> getPropietario(@Header("Authorization") String token);

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarPassword(@Header("Authorization") String token, @Field("currentPassword") String passActual, @Field("newPassword") String passNueva);

        @GET("api/Inmuebles")
        Call<java.util.List<Inmueble>> getInmuebles(@Header("Authorization") String token);

        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(
                @Header("Authorization") String token,
                @Part MultipartBody.Part imagen,
                @Part("inmueble") RequestBody inmueble
        );

        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);

    }

}

