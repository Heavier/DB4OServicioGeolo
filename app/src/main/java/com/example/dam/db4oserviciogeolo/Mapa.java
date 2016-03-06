package com.example.dam.db4oserviciogeolo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.example.dam.db4oserviciogeolo.pojo.Punto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ObjectContainer bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng granada = new LatLng(37.176101, -3.601643);
        mMap.addMarker(new MarkerOptions().position(granada).title("Marker in Granada"));
        List<Punto> puntos = getPuntos();
        float alpha = 1;
        int cont = 0;
        MarkerOptions marker = new MarkerOptions();
        for(Punto paseo : puntos){
            marker.position(new LatLng(paseo.getLatitud(), paseo.getLongitud()));
            marker.visible(true);
            marker.alpha(alpha);
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            marker.title(paseo.getFecha().toString());
            mMap.addMarker(marker);
            if (cont < 20){
                alpha -= 0.1f;
            }
            cont++;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(granada));
        bd.close();
    }

    public List<Punto> getPuntos() {
        bd = Db4oEmbedded.openFile(getExternalFilesDir(null) + "/bd.db4o");
        Punto punto = new Punto(0, 0, null);
        List<Punto> puntos = bd.queryByExample(punto);
        return puntos;
    }
}
