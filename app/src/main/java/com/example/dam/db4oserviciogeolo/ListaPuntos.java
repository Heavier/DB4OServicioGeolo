package com.example.dam.db4oserviciogeolo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.example.dam.db4oserviciogeolo.pojo.Punto;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ListaPuntos extends AppCompatActivity {

    private ObjectContainer bd;
    private android.widget.TextView textView2;
    private android.widget.ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_puntos);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.textView2 = (TextView) findViewById(R.id.textView2);

        getPuntos();
    }
    public void getPuntos() {
        bd = Db4oEmbedded.openFile(getExternalFilesDir(null) + "/bd.db4o");
        Punto punto = new Punto(0, 0, null);
        List<Punto> puntos = bd.queryByExample(punto);
        for (Punto p : puntos) {
            textView2.append("\n" + p.toString());
        }
        bd.close();
    }
}
