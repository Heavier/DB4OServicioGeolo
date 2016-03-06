package com.example.dam.db4oserviciogeolo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam.db4oserviciogeolo.servicio.Servicio;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CTEPLAY = 1;
    private TextView textView;
    private NavigationView navview;
    private DrawerLayout drawerlayout;
    private boolean servicioOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        registerReceiver(receiver, new IntentFilter(Servicio.MENSAJE));
        this.drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navview = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        servicioOn = false;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!servicioOn) {
                    iniciar();
                    Snackbar.make(view, R.string.servOn, Snackbar.LENGTH_LONG).show();
                    servicioOn = true;
                } else {
                    detener();
                    Snackbar.make(view, R.string.servOff, Snackbar.LENGTH_LONG).show();
                    servicioOn = false;
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        textView = (TextView) findViewById(R.id.textView);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mapa) {
            startActivity(new Intent(this, Mapa.class));
        } else if (id == R.id.nav_servicio) {
            startActivity(new Intent(this, Principal.class));
        } else if (id == R.id.nav_puntos) {
            startActivity(new Intent(this, ListaPuntos.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CTEPLAY) {
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    Notification.Builder constructorNotificacion;
    NotificationManager gestorNotificacion;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String resultado= "";
            if (bundle != null) {
                resultado = bundle.getString("msg");
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            constructorNotificacion = new
                    Notification.Builder(Principal.this)
                    .setSmallIcon(R.drawable.ic_puntosw)
                    .setContentTitle(resultado)
                    .setContentText(resultado)
                    .setContentIntent(PendingIntent.getActivity(Principal.this, 0, intent, 0));
            gestorNotificacion = (NotificationManager)
                    getSystemService(Principal.this.NOTIFICATION_SERVICE);
        }
    };

    public void iniciar() {
        startService(new Intent(getBaseContext(), Servicio.class));
    }

    public void detener() {
        stopService(new Intent(getBaseContext(), Servicio.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
