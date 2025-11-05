package com.csuarez.ulpinmobiliaria.ui.menu.home;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa implements OnMapReadyCallback {
        private final LatLng ubicacion;

        public Mapa(Location ubicacion) {
            this.ubicacion = new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
        }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ubicacion);
        markerOptions.title("Inmobiliaria Suarez");
        Marker marker = googleMap.addMarker(markerOptions);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 12));

        CameraPosition cam = new CameraPosition.Builder()
                .target(ubicacion)
                .zoom(18)
                .bearing(25)
                .tilt(45)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cam);
        googleMap.setOnMapLoadedCallback(() -> {
            Log.println(Log.INFO,"info","in");
            googleMap.animateCamera(
                    cameraUpdate,
                    3000,
                    null
            );
            if (marker != null) {
                marker.showInfoWindow();
            }
        });
    }


}
