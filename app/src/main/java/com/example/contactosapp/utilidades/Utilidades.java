package com.example.contactosapp.utilidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Utilidades {
    //Constantes
    public static final String TABLA_CONTACTOS = "contactos";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_IMAGEN = "imagen";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_TELEFONO = "telefono";
    public static final String CAMPO_DIRECCION = "direccion";
    public static final String CAMPO_TWITTER = "twitter";
    public static final String CAMPO_INSTAGRAM = "instagram";

    public static final String CREAR_TABLA_CONTACTO = "CREATE TABLE " + TABLA_CONTACTOS + "("
            + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CAMPO_IMAGEN + " BLOB,"
            + CAMPO_NOMBRE + " TEXT,"
            + CAMPO_TELEFONO + " TEXT,"
            + CAMPO_DIRECCION + " TEXT,"
            + CAMPO_TWITTER + " TEXT,"
            + CAMPO_INSTAGRAM + " TEXT)";




        //Convierte de bitmap a byte array
        public static byte[] imageViewToByte(ImageView image) {
            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }

        //Convierte de byte array a bitmap
        public static Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }

}
