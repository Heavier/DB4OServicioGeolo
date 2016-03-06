package com.example.dam.db4oserviciogeolo.servicio;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.example.dam.db4oserviciogeolo.Principal;
import com.example.dam.db4oserviciogeolo.R;
import com.example.dam.db4oserviciogeolo.pojo.Punto;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Servicio  extends Service {

    Notification.Builder constructorNotificacion;
    NotificationManager gestorNotificacion;
    public static final String MENSAJE = "Escuchando";
    private static final int CTEPLAY = 1;
    private GoogleApiClient cliente = null;
    private LocationRequest peticionLocalizaciones;
    private double lat;
    private double lon;
    private float acc;
    private double alt;
    private String pro;
    private ObjectContainer bd;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Task task = new Task(this);
        task.execute();

        Intent i = new Intent(Servicio.this, Servicio.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        constructorNotificacion = new
                Notification.Builder(Servicio.this)
                .setSmallIcon(R.drawable.ic_puntosw)
                .setContentTitle(MENSAJE)
                .setContentText(MENSAJE)
                .setContentIntent(PendingIntent.getActivity(Servicio.this, 0, i, 0));
        gestorNotificacion = (NotificationManager)
                getSystemService(Servicio.this.NOTIFICATION_SERVICE);
        startForeground(1, constructorNotificacion.build());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class Task extends AsyncTask<String, Integer, String> implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener{

        private Context context;

        public Task(Context context) {
            this.context = context;
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (status == ConnectionResult.SUCCESS) {
                cliente = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                cliente.connect();
            } else {
                if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                    GooglePlayServicesUtil.getErrorDialog(status, (Activity) context, CTEPLAY).show();
                }
            }

            try {
                bd = Db4oEmbedded.openFile(dbConfig(), getExternalFilesDir(null) + "/bd.db4o");
                bd.commit();
            } catch (IOException e) {
                bd.rollback();
                bd.close();
                Toast.makeText(context, R.string.databerror, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... sUrl) {
            return "";
        }

        private EmbeddedConfiguration dbConfig() throws IOException {
            EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
            configuration.common().add(new AndroidSupport());
            configuration.common().activationDepth(25);
            configuration.common().objectClass(GregorianCalendar.class).storeTransientFields(true);
            configuration.common().objectClass(GregorianCalendar.class).callConstructor(true);
            configuration.common().exceptionsOnNotStorable(false);
            configuration.common().objectClass(Punto.class).objectField("fecha").indexed(true);
            return configuration;
        }

        public void insertar(double lat, double lon) {
            Calendar c = Calendar.getInstance();
            Date d = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_WEEK)).getTime();
            Punto p = new Punto(lat, lon, d);
            bd.store(p);
            bd.commit();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            stopSelf();

            Intent in = new Intent(MENSAJE);
            in.putExtra("msg", MENSAJE);
            sendBroadcast(in);
        }

        @Override
        public void onConnected(Bundle bundle) {
            peticionLocalizaciones = new LocationRequest();
            peticionLocalizaciones.setInterval(20000);
            peticionLocalizaciones.setFastestInterval(10000);
            peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizaciones, this);
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            acc = location.getAccuracy();
            alt = location.getAltitude();
            pro = location.getProvider();
            Toast.makeText(context, lat+" "+lon+" "+acc+" "+alt+" "+pro, Toast.LENGTH_SHORT).show();
            insertar(lat, lon);
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }
}