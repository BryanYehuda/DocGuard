package com.its.dguard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile extends AppCompatActivity {

    ImageButton download;
    String DOWNLOAD_URL = Endpoints.GET_FILE_URL;
    private ProgressDialog pDialog;
    private RequestQueue mQueue;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);

        download = findViewById(R.id.downloadFile);
        mQueue = Volley.newRequestQueue(this);
        download.setOnClickListener(view -> jsonParse());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == progress_bar_type) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("File sedang di download, mohon menunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    private void jsonParse() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DOWNLOAD_URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("files");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject link = jsonArray.getJSONObject(i);
                            String file = link.getString("file");
                            new DownloadFileFromURL().execute(file);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask < String, String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String...f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghtOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/2011.pdf");

                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int)((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String...progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
        }
    }

}