package com.example.contactosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AcercaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

    }

    public void volver(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
