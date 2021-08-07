package com.example.contactosapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactosapp.utilidades.Utilidades;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterContactos.OnContactoListener {

    public static final int FICHA_CONTACTO = 2;

    ArrayList<Contacto> listaContactos = new ArrayList<>();
    RecyclerView recyclerContactos;
    //Conexion a base de datos
    ConexionSQLiteHelper con;

    FloatingActionButton fab;
    ExtendedFloatingActionButton agregar, buscar;
    Animation open, close, giroOpen, giroClose;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        con = new ConexionSQLiteHelper(this, "bd_contactos", null, 1);

        //Carga del recycler

        recyclerContactos = findViewById(R.id.recyclerBusqueda);
        recyclerContactos.setLayoutManager(new LinearLayoutManager(this));

        cargaLista();
        AdapterContactos adapter = new AdapterContactos(listaContactos, this);
        recyclerContactos.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        //Manejo del botón flotante del menú principal
        fab = findViewById(R.id.add_fab);
        agregar = findViewById(R.id.anadirFloatting);
        buscar = findViewById(R.id.buscarFloatting);

        open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        giroOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.giro_open);
        giroClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.giro_close);

        //Manejamos la animación de los botones al pulsar sobre ellos
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    agregar.startAnimation(close);
                    buscar.startAnimation(close);
                    fab.startAnimation(giroOpen);

                    agregar.setClickable(false);
                    buscar.setClickable(false);

                    isOpen = false;
                } else {
                    agregar.startAnimation(open);
                    buscar.startAnimation(open);
                    fab.startAnimation(giroClose);

                    agregar.setClickable(true);
                    buscar.setClickable(true);

                    isOpen = true;
                }

            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarActivity.class);
                startActivity(intent);
            }
        });


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BusquedaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargaLista() {
        //Se obetiene estancia legible de la tabla
        SQLiteDatabase db = con.getReadableDatabase();
        //Objeto contacto
        Contacto contacto = null;
        //Cursor con la sentencia a lanzar
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_CONTACTOS, null);

        while (cursor.moveToNext()) {
            contacto = new Contacto();
            contacto.setId(cursor.getInt(0));
            contacto.setFoto(cursor.getBlob(1));
            contacto.setNombre(cursor.getString(2));
            contacto.setTelefono(cursor.getString(3));
            contacto.setDireccion(cursor.getString(4));
            contacto.setTwitter(cursor.getString(5));
            contacto.setInstagram(cursor.getString(6));

            listaContactos.add(contacto);

        }

    }

    //Añade el menu superior para la opcion acerca de
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Se maneja la opción seleccionada del menu superior para abrir la nueva Activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.acerca_de:
                intent = new Intent(MainActivity.this, AcercaActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }

    //Maneja el click sobre un contacto para abrir su ficha completa
    @Override
    public void onContactoClick(int position) {
        Intent ficha = new Intent(this, FichaContactoActivity.class);
        ficha.putExtra("seleccionContacto", listaContactos.get(position));
        startActivity(ficha);
    }
}