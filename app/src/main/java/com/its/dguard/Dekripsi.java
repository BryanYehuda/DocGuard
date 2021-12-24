package com.its.dguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Dekripsi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dekripsi);

        ImageButton algoritma1 = (ImageButton) findViewById(R.id.algoritma1);
        ImageButton algoritma2 = (ImageButton) findViewById(R.id.algoritma2);
        ImageButton algoritma3 = (ImageButton) findViewById(R.id.algoritma3);

        algoritma1.setOnClickListener(view -> opentype1());
        algoritma2.setOnClickListener(view -> opentype2());
        algoritma3.setOnClickListener(view -> opentype3());
    }

    public void opentype1() {
        Intent intent = new Intent(this, ChooseType2.class);
        intent.putExtra("dekripsi","ANDA TELAH MEMILIH MD5");
        startActivity(intent);
    }
    public void opentype2() {
        Intent intent = new Intent(this, ChooseType2.class);
        intent.putExtra("dekripsi","ANDA TELAH MEMILIH SHA256");
        startActivity(intent);
    }
    public void opentype3() {
        Intent intent = new Intent(this, ChooseType2.class);
        intent.putExtra("dekripsi","ANDA TELAH MEMILIH RSA");
        startActivity(intent);
    }

}