package com.example.contactosapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Contacto implements Parcelable {
    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };
    private int id;
    private byte[] foto;
    private String nombre;
    private String telefono;
    private String direccion;
    private String twitter;
    private String instagram;

    public Contacto() {

    }


    public Contacto(byte[] foto, String nombre, String telefono, String direccion, String twitter, String instagram) {
        this.foto = foto;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.twitter = twitter;
        this.instagram = instagram;
    }

    protected Contacto(Parcel in) {
        id = in.readInt();
        foto = in.createByteArray();
        nombre = in.readString();
        telefono = in.readString();
        direccion = in.readString();
        twitter = in.readString();
        instagram = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(foto);
        dest.writeString(nombre);
        dest.writeString(telefono);
        dest.writeString(direccion);
        dest.writeString(twitter);
        dest.writeString(instagram);
    }
}
