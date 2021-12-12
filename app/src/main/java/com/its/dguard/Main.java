package com.its.dguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton enkripsi = (ImageButton) findViewById(R.id.enkripsi);
        ImageButton dekripsi = (ImageButton) findViewById(R.id.dekripsi);

        enkripsi.setOnClickListener(view -> openEnkripsi());
        dekripsi.setOnClickListener(view -> openDekripsi());
    }

    public void openEnkripsi() {
        Intent intent = new Intent(this, Enkripsi.class);
        startActivity(intent);
    }

    public void openDekripsi() {
        Intent intent = new Intent(this, Dekripsi.class);
        startActivity(intent);
    }
}