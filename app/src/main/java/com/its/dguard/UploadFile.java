package com.its.dguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UploadFile extends AppCompatActivity {
    private ImageButton upload, useFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        upload = (ImageButton) findViewById(R.id.upload);
        useFile = (ImageButton) findViewById(R.id.usefile);

        useFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadFile();
            }
        });
    }

    public void downloadFile() {
        Intent intent = new Intent(this, com.its.dguard.DownloadFile.class);
        startActivity(intent);
    }
}