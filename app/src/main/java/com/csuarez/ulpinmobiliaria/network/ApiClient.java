package com.csuarez.ulpinmobiliaria.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ApiClient {

    private static final String BASE_URL="https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";
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
        Call<String> login(@Field("Usuario") String user, @Field("Clave") String password);
    }

}

