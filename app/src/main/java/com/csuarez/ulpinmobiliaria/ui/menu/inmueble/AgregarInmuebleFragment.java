package com.csuarez.ulpinmobiliaria.ui.menu.inmueble;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csuarez.ulpinmobiliaria.R;
import com.csuarez.ulpinmobiliaria.databinding.FragmentAgregarInmuebleBinding;
import com.csuarez.ulpinmobiliaria.models.Inmueble;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AgregarInmuebleFragment extends Fragment {

    private AgregarInmuebleViewModel agregarVm;
    private FragmentAgregarInmuebleBinding binding;
    private ActivityResultLauncher<String> imagePicker;
    private File imagenSeleccionada;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Registrar el selector de imágenes
        imagePicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        agregarVm.setImagenUri(uri);
                        binding.ivImagenPreview.setImageURI(uri);
                        // Convertir Uri a File
                        imagenSeleccionada = uriToFile(uri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        agregarVm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AgregarInmuebleViewModel.class);
        binding = FragmentAgregarInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar adapters para los dropdowns
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, Inmueble.TIPOS);
        binding.actvTipo.setAdapter(adapterTipo);

        ArrayAdapter<String> adapterUso = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, Inmueble.USOS);
        binding.actvUso.setAdapter(adapterUso);

        // Observer para errores
        agregarVm.getMError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(0xFFE57373)
                            .show();
                }
            }
        });

        // Observer para mensajes de éxito
        agregarVm.getMMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if (mensaje != null && !mensaje.isEmpty()) {
                    Snackbar.make(binding.getRoot(), mensaje, Snackbar.LENGTH_SHORT).show();
                    // Volver atrás
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu).popBackStack();
                }
            }
        });

        // Botón seleccionar imagen
        binding.btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.launch("image/*");
            }
        });

        // Botón guardar
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarVm.crearInmueble(
                        binding.etDireccion.getText().toString(),
                        binding.actvTipo.getText().toString(),
                        binding.actvUso.getText().toString(),
                        binding.etAmbientes.getText().toString(),
                        binding.etSuperficie.getText().toString(),
                        binding.etPrecio.getText().toString(),
                        binding.etLatitud.getText().toString(),
                        binding.etLongitud.getText().toString(),
                        imagenSeleccionada
                );
            }
        });

        return root;
    }

    private File uriToFile(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            File file = new File(getContext().getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            
            outputStream.close();
            inputStream.close();
            
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}