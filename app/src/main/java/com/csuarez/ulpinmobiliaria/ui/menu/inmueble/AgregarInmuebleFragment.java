package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.databinding.FragmentAgregarInmuebleBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity;
import com.csuarez.ulpinmobiliaria.utils.SnackbarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AgregarInmuebleFragment extends Fragment {

    private AgregarInmuebleViewModel agregarVm;
    private FragmentAgregarInmuebleBinding binding;
    private ActivityResultLauncher<Intent> galeriaLauncher;
    private ActivityResultLauncher<Intent> camaraLauncher;
    private Intent intentGaleria;
    private Intent intentCamara;
    private Uri tempPhotoUri;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // registrar launcher para solicitar permisos
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {
                            tomarFoto();
                        } else {
                            SnackbarUtils.mostrarError(binding.getRoot(), "Permiso de cámara denegado");
                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        agregarVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AgregarInmuebleViewModel.class);
        binding = FragmentAgregarInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // ocultar loader global al entrar al fragment
        MenuActivity activity = (MenuActivity) getActivity();
        if (activity != null) {
            activity.ocultarLoader();
        }

        // inicializar launchers
        abrirGaleria();
        abrirCamara();

        // configurar adapters para los dropdowns
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, Inmueble.TIPOS);
        binding.actvTipo.setAdapter(adapterTipo);

        ArrayAdapter<String> adapterUso = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, Inmueble.USOS);
        binding.actvUso.setAdapter(adapterUso);

        // observer para el loader
        agregarVm.getMCargando().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean cargando) {
                com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity activity = (com.csuarez.ulpinmobiliaria.ui.menu.MenuActivity) getActivity();
                if (activity != null) {
                    if (cargando) {
                        activity.mostrarLoader();
                    } else {
                        activity.ocultarLoader();
                    }
                }
            }
        });

        // observer para la imagen
        agregarVm.getMImagenUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivImagenPreview.setImageURI(uri);
            }
        });

        // observer para errores
        agregarVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                SnackbarUtils.mostrarError(binding.getRoot(), error);
            }
        });

        // observer para mensajes de exito
        agregarVm.getMMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                SnackbarUtils.mostrarExito(binding.getRoot(), mensaje, true);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu).popBackStack();
            }
        });

        // boton seleccionar imagen
        binding.btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriaLauncher.launch(intentGaleria);
            }
        });

        // boton tomar foto
        binding.btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarPermisos();
            }
        });

        // boton guardar
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtils.ocultarTeclado(v);
                agregarVm.crearInmueble(
                        binding.etDireccion.getText().toString(),
                        binding.actvTipo.getText().toString(),
                        binding.actvUso.getText().toString(),
                        binding.etAmbientes.getText().toString(),
                        binding.etSuperficie.getText().toString(),
                        binding.etPrecio.getText().toString(),
                        binding.etLatitud.getText().toString(),
                        binding.etLongitud.getText().toString(),
                        binding.switchDisponible.isChecked()
                );
            }
        });

        return root;
    }

    private void abrirGaleria() {
        intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        agregarVm.recibirFoto(result);
                    }
                });
    }

    private void abrirCamara() {
        camaraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        agregarVm.recibirFotoDeCamara(result, tempPhotoUri);
                    }
                });
    }

    private void solicitarPermisos() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            tomarFoto();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void tomarFoto() {
        try {
            tempPhotoUri = crearArchivoImagenTemporal();
            if (tempPhotoUri != null) {
                intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri);
                camaraLauncher.launch(intentCamara);
            }
        } catch (IOException e) {
            e.printStackTrace();
            SnackbarUtils.mostrarError(binding.getRoot(), "Error al crear el archivo de imagen");
        }
    }

    private Uri crearArchivoImagenTemporal() throws IOException {
        String nombreArchivo = "foto_inmueble_" + System.currentTimeMillis();
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreArchivo, ".jpg", storageDir);

        return FileProvider.getUriForFile(requireContext(),
                "com.csuarez.ulpinmobiliaria.provider",
                imagen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}