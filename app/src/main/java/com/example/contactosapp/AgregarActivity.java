package com.example.contactosapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.contactosapp.utilidades.Utilidades;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarActivity extends AppCompatActivity {


    final int REQUEST_CODE_GALLERY = 999;
    EditText agregaNombre, agregaTelefono, agregaDireccion, agregaTwitter, agregaInstagram;
    CircleImageView imagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        agregaNombre = findViewById(R.id.editaNombre);
        agregaTelefono = findViewById(R.id.editaTelefono);
        agregaDireccion = findViewById(R.id.editaDireccion);
        agregaTwitter = findViewById(R.id.editaTwitter);
        agregaInstagram = findViewById(R.id.editaInstagram);

        imagen = findViewById(R.id.agregaImagen);
        //Permite abrir galeria para selección de foto
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AgregarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
                imagen.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void guardar(View view) {
        agregarContacto();

    }

    public void cancelar(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void agregarContacto() {
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(this, "bd_contactos", null, 1);
        //Obtenemos instancia db para poder modificarla
        SQLiteDatabase db = con.getWritableDatabase();

        if (agregaNombre.getText().toString().equals("") || agregaTelefono.getText().toString().equals("")) {
            Toast.makeText(this, "Rellene al menos los campos nombre y teléfono, por favor", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();

            values.put(Utilidades.CAMPO_IMAGEN, Utilidades.imageViewToByte(imagen));
            values.put(Utilidades.CAMPO_NOMBRE, agregaNombre.getText().toString());
            values.put(Utilidades.CAMPO_TELEFONO, agregaTelefono.getText().toString());
            values.put(Utilidades.CAMPO_DIRECCION, agregaDireccion.getText().toString());
            values.put(Utilidades.CAMPO_TWITTER, agregaTwitter.getText().toString());
            values.put(Utilidades.CAMPO_INSTAGRAM, agregaInstagram.getText().toString());

            //Se realiza la inserción en la tabla
            db.insert(Utilidades.TABLA_CONTACTOS, null, values);


            //Cerramos conexión
            db.close();

            Toast.makeText(this, "Contacto creado correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}


