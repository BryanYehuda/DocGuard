package com.its.dguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadFilemd5 extends AppCompatActivity {

    ImageButton btnChoose;
    private final String UPLOAD_URL = Endpoints.UPLOAD_FILE_URL;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        btnChoose = findViewById(R.id.upload);
        btnChoose.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, 1);
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Uri uri = data.getData();
            String uriString = uri.toString();
            String displayName;

            if (uriString.startsWith("content://")) {
                try (Cursor cursor = this.getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        uploadPDF(displayName, uri);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadPDF(final String pdfname, Uri pdffile) {

        Date d = new Date();
        CharSequence s = DateFormat.format("yyyyMMdd-hh-mm-ss", d.getTime());
        final String date = s.toString();

        InputStream iStream;
        try {
            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_URL,
                    response -> {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            jsonObject.toString().replace("\\\\", "");
                            jsonObject.getString("status");
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
                    params.put("file", new DataPart(pdfname, inputData));
                    return params;
                }
            };
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(UploadFilemd5.this);
            rQueue.add(volleyMultipartRequest);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {byteBuffer.write(buffer, 0, len);}
        return byteBuffer.toByteArray();
    }

}