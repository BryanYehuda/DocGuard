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

        algoritma1.setOnClickListener(view -> chooseType());
    }

    public void chooseType() {
        Intent intent = new Intent(this, ChooseType.class);
        startActivity(intent);
    }

}