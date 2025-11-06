package com.csuarez.ulpinmobiliaria.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.csuarez.ulpinmobiliaria.models.Contrato;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.models.Inquilino;
import com.csuarez.ulpinmobiliaria.models.Pago;
import com.csuarez.ulpinmobiliaria.models.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {

    // cambiar este flag para usar datos mock (true) o api real (false)
    public static final boolean USE_MOCK_DATA = true;

    public static final String BASE_URL="https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";
    private static Retrofit retrofit;
    private static ApiService mockService;


    public static ApiService getClient(){
        // si esta activado el modo mock devolver datos de prueba
        if (USE_MOCK_DATA) {
            if (mockService == null) {
                mockService = new MockApiService();
            }
            return mockService;
        }
        
        // modo normal: usar api real
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

        @FormUrlEncoded
        @POST("api/Propietarios/resetPassword")
        Call<String> resetPassword(@Field("email") String email);

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

        @GET("api/Inmuebles/GetContratoVigente")
        Call<java.util.List<Inmueble>> getInmueblesConContratoVigente(@Header("Authorization") String token);

        @GET("api/contratos/inmueble/{id}")
        Call<Contrato> getContratoPorInmueble(@Header("Authorization") String token, @Path("id") int idInmueble);

        @GET("api/pagos/contrato/{id}")
        Call<java.util.List<Pago>> getPagosPorContrato(@Header("Authorization") String token, @Path("id") int idContrato);

    }

       /**
     * Implementación MOCK de ApiService para testing cuando la API está caída
     * Devuelve datos de prueba simulando respuestas reales
     */
    private static class MockApiService implements ApiService {

        @Override
        public Call<String> login(String user, String pass) {
            return new MockCall<>("mock_token_12345");
        }

        @Override
        public Call<Propietario> getPropietario(String token) {
            Propietario propietario = new Propietario(
                    1, "Juan", "Pérez", "12345678", "2664123456", "juan.perez@mail.com"
            );
            return new MockCall<>(propietario);
        }

        @Override
        public Call<Propietario> actualizarPropietario(String token, Propietario propietario) {
            return new MockCall<>(propietario);
        }

        @Override
        public Call<Void> cambiarPassword(String token, String passActual, String passNueva) {
            return new MockCall<>(null);
        }

        @Override
        public Call<String> resetPassword(String email) {
            return new MockCall<>("Email enviado correctamente");
        }

        @Override
        public Call<java.util.List<Inmueble>> getInmuebles(String token) {
            java.util.List<Inmueble> inmuebles = new java.util.ArrayList<>();
            Propietario duenio = new Propietario(1, "Juan", "Pérez", "12345678", "2664123456", "juan.perez@mail.com");
            
            inmuebles.add(new Inmueble(1, "Av. Libertador 1234", "Residencial", "Casa", 3, 2, -33.3, -66.3, 250000.0,
                    "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=400", true, 1, duenio, false));
            
            inmuebles.add(new Inmueble(2, "Colon 323", "Residencial", "Departamento", 2, 1, -33.3, -66.3, 180000.0,
                    "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=400", false, 1, duenio, true));
            
            inmuebles.add(new Inmueble(3, "San Martin 567", "Comercial", "Local", 0, 1, -33.3, -66.3, 320000.0,
                    "https://images.unsplash.com/photo-1497366216548-37526070297c?w=400", false, 1, duenio, true));
            
            return new MockCall<>(inmuebles);
        }

        @Override
        public Call<Inmueble> cargarInmueble(String token, MultipartBody.Part imagen, RequestBody inmueble) {
            Propietario duenio = new Propietario(1, "Juan", "Pérez", "12345678", "2664123456", "juan.perez@mail.com");
            Inmueble nuevoInmueble = new Inmueble(999, "Nuevo Inmueble", "Residencial", "Casa", 2, 1, -33.3, -66.3, 200000.0,
                    "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=400", true, 1, duenio, false);
            return new MockCall<>(nuevoInmueble);
        }

        @Override
        public Call<Inmueble> actualizarInmueble(String token, Inmueble inmueble) {
            return new MockCall<>(inmueble);
        }

        @Override
        public Call<java.util.List<Inmueble>> getInmueblesConContratoVigente(String token) {
            java.util.List<Inmueble> inmuebles = new java.util.ArrayList<>();
            Propietario duenio = new Propietario(1, "Juan", "Pérez", "12345678", "2664123456", "juan.perez@mail.com");
            
            inmuebles.add(new Inmueble(2, "Colon 323", "Residencial", "Departamento", 2, 1, -33.3, -66.3, 180000.0,
                    "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=400", false, 1, duenio, true));
            
            inmuebles.add(new Inmueble(3, "San Martin 567", "Comercial", "Local", 0, 1, -33.3, -66.3, 320000.0,
                    "https://images.unsplash.com/photo-1497366216548-37526070297c?w=400", false, 1, duenio, true));
            
            return new MockCall<>(inmuebles);
        }

        @Override
        public Call<Contrato> getContratoPorInmueble(String token, int idInmueble) {
            Propietario duenio = new Propietario(1, "Juan", "Pérez", "12345678", "2664123456", "juan.perez@mail.com");
            
            Inmueble inmueble = new Inmueble(idInmueble, "Colon 323", "Residencial", "Departamento", 2, 1, -33.3, -66.3, 180000.0,
                    "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=400", false, 1, duenio, true);
            
            Inquilino inquilino = new Inquilino(1, "Mario", "Luna", "25340691", "2664253411",
                    "mario.luna@mail.com", "Lucero Roberto", "2664851422");
            
            Contrato contrato = new Contrato(1, idInmueble, 1, "2024-01-01", "2025-12-31", 85000.0,
                    true, inquilino, inmueble);
            
            return new MockCall<>(contrato);
        }

        @Override
        public Call<java.util.List<Pago>> getPagosPorContrato(String token, int idContrato) {
            java.util.List<Pago> pagos = new java.util.ArrayList<>();
            
            pagos.add(new Pago(1, idContrato, "2024-01-05", "Pago mes de Enero", 85000.0, true, null));
            pagos.add(new Pago(2, idContrato, "2024-02-05", "Pago mes de Febrero", 85000.0, true, null));
            pagos.add(new Pago(3, idContrato, "2024-03-05", "Pago mes de Marzo", 85000.0, true, null));
            pagos.add(new Pago(4, idContrato, "2024-04-05", "Pago mes de Abril", 85000.0, true, null));
            pagos.add(new Pago(5, idContrato, "2024-05-05", "Pago mes de Mayo", 85000.0, false, null));
            pagos.add(new Pago(6, idContrato, "2024-06-05", "Pago mes de Junio", 85000.0, false, null));
            
            return new MockCall<>(pagos);
        }
    }

    /**
     * Implementación simple de Call<T> para devolver respuestas mock
     */
    private static class MockCall<T> implements Call<T> {
        private T response;

        public MockCall(T response) {
            this.response = response;
        }

        @Override
        public retrofit2.Response<T> execute() {
            return retrofit2.Response.success(response);
        }

        @Override
        public void enqueue(retrofit2.Callback<T> callback) {
            callback.onResponse(this, retrofit2.Response.success(response));
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call<T> clone() {
            return this;
        }

        @Override
        public Request request() {
            return null;
        }

        @Override
        public okio.Timeout timeout() {
            return okio.Timeout.NONE;
        }
    }
}