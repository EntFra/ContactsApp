package com.example.contactosapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactosapp.utilidades.Utilidades;

import java.util.ArrayList;


public class BusquedaActivity extends AppCompatActivity {


    ArrayList<Contacto> listaContactos = new ArrayList<>();
    RecyclerView recyclerContactos;
    //Conexion a base de datos
    ConexionSQLiteHelper con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        con = new ConexionSQLiteHelper(this, "bd_contactos", null, 1);

        //Carga del recycler

        recyclerContactos = findViewById(R.id.recyclerBusqueda);
        recyclerContactos.setLayoutManager(new LinearLayoutManager(this));

        cargaLista();
        AdapterContactos adapter = new AdapterContactos(listaContactos, null);
        recyclerContactos.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        SearchView searchView = findViewById(R.id.busqueda);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
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

}
