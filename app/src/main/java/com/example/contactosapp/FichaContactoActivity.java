package com.example.contactosapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.contactosapp.utilidades.Utilidades;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class FichaContactoActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    Contacto contacto;
    private EditText editaNombre, editaTelefono, editaDireccion, editaTwitter, editaInstagram;
    private CircleImageView editaImagenFicha;
    private CircleImageView imagenFicha;
    private String nombreEditado, telefonoEditado, direccionEditada, twitterEditado, instagramEditado;
    private byte[] imagenEditada;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_contacto);
        contacto = getIntent().getParcelableExtra("seleccionContacto");
        if (getIntent().hasExtra("seleccionContacto")) {

            contacto = getIntent().getParcelableExtra("seleccionContacto");
        }


        editaNombre = findViewById(R.id.editaNombre);
        editaTelefono = findViewById(R.id.editaTelefono);
        editaDireccion = findViewById(R.id.editaDireccion);
        editaTwitter = findViewById(R.id.editaTwitter);
        editaInstagram = findViewById(R.id.editaInstagram);
        editaImagenFicha = findViewById(R.id.editaImagenFicha);

        //Se vínculan los valores de las bases de datos para mostrarlos en los campos
        editaNombre.setText(contacto.getNombre());
        editaTelefono.setText(contacto.getTelefono());
        editaDireccion.setText(contacto.getDireccion());
        editaTwitter.setText(contacto.getTwitter());
        editaInstagram.setText(contacto.getInstagram());
        editaImagenFicha.setImageBitmap(Utilidades.getImage(contacto.getFoto()));

        editaImagenFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(FichaContactoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "Permisos insuficientes", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editaImagenFicha.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //Mëtodo para el botón volver de la interface que realiza la actualización y regresa
    public void volver(View v) {
        actualizaContacto();
    }

    //Mëtodo que regresa a Main
    private void vueltaMain() {
        Intent intent = new Intent(FichaContactoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Método público para borrar contacto y volver al Main, se conecta con botón papelera de la interface
    public void borrar(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Borrar contacto")
                .setMessage("¿Seguro que desea borrar el contacto?")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borraContacto();
                        vueltaMain();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    //Método que implementa el borrado de un contacto
    private void borraContacto() {
        //Conexión a bd
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(this, "bd_contactos", null, 1);
        //Instancia de bd editable
        SQLiteDatabase db = con.getWritableDatabase();

        db.delete(Utilidades.TABLA_CONTACTOS, Utilidades.CAMPO_ID + "=?", new String[]{String.valueOf(contacto.getId())});
        db.close();

    }

    //Método que implementa la actualización de datos
    private void actualizaContacto() {

        if (editaNombre.getText().toString().equals("") || editaTelefono.getText().toString().equals("")) {
            Toast.makeText(this, "Rellene al menos los campos nombre y teléfono, por favor", Toast.LENGTH_SHORT).show();
        } else {
            nombreEditado = editaNombre.getText().toString();
            telefonoEditado = editaTelefono.getText().toString();
            direccionEditada = editaDireccion.getText().toString();
            twitterEditado = editaTwitter.getText().toString();
            instagramEditado = editaInstagram.getText().toString();
            imagenFicha = findViewById(R.id.editaImagenFicha);

            imagenEditada = Utilidades.imageViewToByte(imagenFicha);
            //Conexión a bd
            ConexionSQLiteHelper con = new ConexionSQLiteHelper(this, "bd_contactos", null, 1);
            //Instancia de bd editable
            SQLiteDatabase db = con.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_IMAGEN, imagenEditada);
            values.put(Utilidades.CAMPO_NOMBRE, nombreEditado);
            values.put(Utilidades.CAMPO_TELEFONO, telefonoEditado);
            values.put(Utilidades.CAMPO_DIRECCION, direccionEditada);
            values.put(Utilidades.CAMPO_TWITTER, twitterEditado);
            values.put(Utilidades.CAMPO_INSTAGRAM, instagramEditado);

            id = contacto.getId();


            db.update(Utilidades.TABLA_CONTACTOS, values, Utilidades.CAMPO_ID + "=?", new String[]{String.valueOf(id)});
            db.close();
            if (!nombreEditado.equals(contacto.getNombre()) || !telefonoEditado.equals(contacto.getTelefono()) || !direccionEditada.equals(contacto.getDireccion()) || !twitterEditado.equals(contacto.getTwitter())
                    || !instagramEditado.equals(contacto.getInstagram())) {
                Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show();

            }
            vueltaMain();

        }

    }
}
