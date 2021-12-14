package com.its.dguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.Date;
import java.util.UUID;

public class UploadFile extends AppCompatActivity {
    public static final String UPLOAD_URL = "http://10.0.2.2/upload/file.php";
    private final int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        ImageButton btnChoose = findViewById(R.id.upload);
        ImageButton btnUse = findViewById(R.id.usefile);

        btnChoose.setOnClickListener(view -> showFileChooser());
        btnUse.setOnClickListener(view -> uploadMultipart());
    }

    public void uploadMultipart() {
        Date d = new Date();
        CharSequence s = DateFormat.format("yyyyMMdd-hh-mm-ss", d.getTime());
        final String name = s.toString();
        String path = FilePath.getPath(this, filePath);

        if (path == null) {

            Toast.makeText(this, "Mohon pindahkan file .PDF anda ke Internal Storage dan coba lagi", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf")
                        .addParameter("name", name)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih PDF yang diinginkan"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Ijin diberikan, sekarang anda bisa mengakses Internal Storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Mohon maaf, anda masih belum diijinkan", Toast.LENGTH_LONG).show();
            }
        }
    }


}
