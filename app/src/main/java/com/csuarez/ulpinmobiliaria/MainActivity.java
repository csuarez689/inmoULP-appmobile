package com.csuarez.ulpinmobiliaria;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.csuarez.ulpinmobiliaria.databinding.ActivityMainBinding;
import com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityMainBinding binding;
    private MainActivityViewModel mainVm;
    private ActivityResultLauncher<String[]> permisosLauncher;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mainVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainActivityViewModel.class);
        setContentView(binding.getRoot());

        initPermisosLauncher();
        verificarPermisos();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }


        //observer agitar
        mainVm.getMAgitar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean agitado) {
                if (agitado) {
                    llamar();
                }
            }
        });

        //observer login
        binding.btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mainVm.login(binding.etUsuario.getText(), binding.etClave.getText());
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                }
        });

        //observer error
        mainVm.getMError().observe(this, new Observer<String>() {
            @Override
            public void onChanged (String error){
                SnackbarUtils.mostrarError(binding.getRoot(), error);
            }
        });

        //observer mensaje
        mainVm.getMMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                SnackbarUtils.mostrarExito(binding.getRoot(), mensaje);
            }
        });

        mainVm.getMLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedIn) {
                if(loggedIn){
                    Intent intent = new Intent(getApplication(), MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intent);
                }
            }
        });

        // listener para olvide contrasena
        binding.tvOlvidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogResetPassword();
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mainVm.checkAccel(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    public void llamar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:2664774140"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            SnackbarUtils.mostrarError(binding.getRoot(), "Permiso de llamada no concedido");
        }
        mainVm.resetAccel();
    }

    private void verificarPermisos() {
        List<String> permisosPendientes = new ArrayList<>();

        String[] permisos = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        for (String permiso : permisos) {
            if (ContextCompat.checkSelfPermission(this, permiso)
                    != PackageManager.PERMISSION_GRANTED) {
                permisosPendientes.add(permiso);
            }
        }

        if (!permisosPendientes.isEmpty()) {
            permisosLauncher.launch(permisosPendientes.toArray(new String[0]));
        }
    }

    private void initPermisosLauncher(){
        permisosLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean todosOtorgados = true;
                    boolean algunoNoPreguntar = false;

                    for (String permiso : result.keySet()) {
                        boolean concedido = Boolean.TRUE.equals(result.get(permiso));
                        if (!concedido) {
                            todosOtorgados = false;

                            // si el usuario marco "no volver a preguntar"
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                                algunoNoPreguntar = true;
                            }
                        }
                    }

                    if (todosOtorgados) {
                        SnackbarUtils.mostrarExito(binding.getRoot(), "Permisos concedidos correctamente", true);
                    } else if (algunoNoPreguntar) {
                        Toast.makeText(this,
                                "Algunos permisos fueron denegados permanentemente. Habilítelos en Configuración.",
                                Toast.LENGTH_LONG).show();

                        // redirigir al usuario a la configuracion de la app
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                        finish(); //cerrar app luego del aviso
                    } else {
                        Toast.makeText(this,
                                "Debe otorgar los permisos para continuar.",
                                Toast.LENGTH_LONG).show();
                        // reintenta pedir los permisos una vez mas
                        verificarPermisos();
                    }
                });
    }

    private void mostrarDialogResetPassword() {
        // crear textinputlayout para mejor ux
        final com.google.android.material.textfield.TextInputLayout inputLayout = 
                new com.google.android.material.textfield.TextInputLayout(this);
        final com.google.android.material.textfield.TextInputEditText input = 
                new com.google.android.material.textfield.TextInputEditText(inputLayout.getContext());
        
        input.setHint("Email");
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        
        // configurar padding para el layout
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        inputLayout.setPadding(padding, padding/2, padding, 0);
        inputLayout.addView(input);
        
        new AlertDialog.Builder(this)
                .setTitle("Recuperar Contraseña")
                .setMessage("Ingrese su email para recibir instrucciones de recuperación")
                .setView(inputLayout)
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
                        mainVm.resetPassword(email);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
