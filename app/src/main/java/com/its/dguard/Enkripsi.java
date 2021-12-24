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

        algoritma1.setOnClickListener(view -> opentype1());
        algoritma2.setOnClickListener(view -> opentype2());
        algoritma3.setOnClickListener(view -> opentype3());
    }

    public void opentype1() {
        Intent intent = new Intent(this, ChooseType.class);
        intent.putExtra("enkripsi","ANDA TELAH MEMILIH AES");
        startActivity(intent);
    }
    public void opentype2() {
        Intent intent = new Intent(this, ChooseType.class);
        intent.putExtra("enkripsi","ANDA TELAH MEMILIH DES");
        startActivity(intent);
    }
    public void opentype3() {
        Intent intent = new Intent(this, ChooseType.class);
        intent.putExtra("enkripsi","ANDA TELAH MEMILIH RSA");
        startActivity(intent);
    }

}