package com.its.dguard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadImg extends AppCompatActivity {

    ImageButton btnCapture, btnChoose, btnUse;
    ImageView imageView;
    String fileName;
    Bitmap bitmap;

    private static final int CODE_CAMERA_CAPTURE = 222;
    final int CODE_GALLERY_REQUEST = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);

        imageView = findViewById(R.id.imageView14);
        btnCapture = findViewById(R.id.captureimg);
        btnChoose = findViewById(R.id.choosephoto);
        btnUse = findViewById(R.id.usephoto);

        btnCapture.setOnClickListener(view -> {
            Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "HasilFoto");
            imagesFolder.mkdirs();
            Date date = new Date();
            CharSequence string = DateFormat.format("yyyyMMdd-hh-mm-ss", date.getTime());
            fileName = imagesFolder + File.separator + string.toString() + ".jpg";
            File image = new File(fileName);

            Uri uriSavedImage = Uri.fromFile(image);
            capture.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            startActivityForResult(capture, CODE_CAMERA_CAPTURE);
        });

        btnChoose.setOnClickListener(view -> ActivityCompat.requestPermissions(UploadImg.this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, CODE_GALLERY_REQUEST));

        btnUse.setOnClickListener(view -> {

            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bm = drawable.getBitmap();

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, 1024, 768, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            uploadBitmap(rotatedBitmap);

            Intent intent = new Intent(this, DownloadImg.class);
            startActivity(intent);

        });

    }

    private void captureImage() throws IOException {
        Bitmap bm;
        BitmapFactory.Options options;
        options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bm = BitmapFactory.decodeFile(fileName, options);
        imageView.setImageBitmap(bm);
        Toast.makeText(this, "Foto Telah Terupload ke ImageView dan Tersimpan di Gallery Dengan Nama " + fileName, Toast.LENGTH_SHORT).show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "Anda Belum Mempunyai Ijin Untuk Mengakses Gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_CAMERA_CAPTURE) {
                try {
                    captureImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                btnUse.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(UploadImg.this, "Foto Telah Terupload ke ImageView", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        Date d = new Date();
        CharSequence s = DateFormat.format("yyyyMMdd-hh-mm-ss", d.getTime());
        final String date = s.toString();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Endpoints.UPLOAD_IMAGE_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map < String, String > getParams() {
                Map < String, String > params = new HashMap < > ();
                params.put("date", date);
                return params;
            }

            @Override
            protected Map < String, DataPart > getByteData() {
                Map < String, DataPart > params = new HashMap < > ();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

}