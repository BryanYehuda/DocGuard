package com.its.dguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Enkripsi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enkripsi);

        ImageButton algoritma1 = findViewById(R.id.algoritma1);
        ImageButton algoritma2 = findViewById(R.id.algoritma2);
        ImageButton algoritma3 = findViewById(R.id.algoritma3);

        algoritma1.setOnClickListener(view -> opentype());
    }

    public void opentype() {
        Intent intent = new Intent(this, ChooseType.class);
        startActivity(intent);
    }

}