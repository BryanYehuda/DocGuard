package com.its.dguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        ImageButton chooseImg = (ImageButton) findViewById(R.id.chooseimg);
        ImageButton chooseFile = (ImageButton) findViewById(R.id.choosefile);

        chooseImg.setOnClickListener(view -> openUpImg());

        chooseFile.setOnClickListener(view -> openUpFile());
    }

    public void openUpImg() {
        Intent intent = new Intent(this, UploadImg.class);
        startActivity(intent);
    }

    public void openUpFile() {
        Intent intent = new Intent(this, com.its.dguard.UploadFile.class);
        startActivity(intent);
    }
}