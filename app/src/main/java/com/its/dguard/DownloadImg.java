package com.its.dguard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImg extends AppCompatActivity {

    ImageButton download;
    String DOWNLOAD_URL = Endpoints.GET_FILE_URL;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);


        download = (ImageButton) findViewById(R.id.downloadFile);
        String result = null;
        try {
            result = HTTPGetCall(DOWNLOAD_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = null;
        try {
            assert result != null;
            obj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String finalFileLink = null;
        try {
            assert obj != null;
            finalFileLink = obj.getString("file");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String finalFileLink1 = finalFileLink;
        download.setOnClickListener(v -> new DownloadFileFromURL().execute(finalFileLink1));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == progress_bar_type) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    protected String HTTPGetCall(String WebMethodURL) throws IOException {
        StringBuilder response = new StringBuilder();
        URL u = new URL(WebMethodURL);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()),8192);
            String line;
            while ((line = input.readLine()) != null)
            {
                response.append(line);
            }
            input.close();
        }
        return response.toString();
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
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
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
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

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
        }
    }


}