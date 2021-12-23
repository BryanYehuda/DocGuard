package com.its.dguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseType2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type2);

        ImageButton chooseImg = (ImageButton) findViewById(R.id.chooseimg);
        ImageButton chooseFile = (ImageButton) findViewById(R.id.choosefile);

        //dapatkan string dari dekripsi
        Intent i = getIntent();
        String type = i.getStringExtra("dekripsi");

        Toast.makeText(this,type,Toast.LENGTH_SHORT).show();

        //pengecekan
        if(type.equals("md5") ){
            chooseImg.setOnClickListener(view -> {
                Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
                Intent intentImg = new Intent(this, UploadImgMd5.class);
                intentImg.putExtra("type","dekripsi");
                startActivity(intentImg);
            });
            chooseFile.setOnClickListener(view -> {
                Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
                Intent intentFile = new Intent(this , UploadFileMd5.class);
                intentFile.putExtra("type","dekripsi");
                startActivity(intentFile);
            });
        }
        else if (type.equals("sha256")){
            chooseImg.setOnClickListener(view -> {
                Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
                Intent intentImg = new Intent(this, UploadImgSha.class);
                intentImg.putExtra("type","dekripsi");
                startActivity(intentImg);
            });
            chooseFile.setOnClickListener(view -> {
                Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
                Intent intentFile = new Intent(this , UploadFileSha.class);
                intentFile.putExtra("type","dekripsi");
                startActivity(intentFile);
            });
        }
        else if (type.equals("algo")){
            chooseImg.setOnClickListener(view -> {
                Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
                Intent intentImg = new Intent(this,UploadImg.class);
                intentImg.putExtra("type","dekripsi");
                startActivity(intentImg);
            });
            chooseFile.setOnClickListener(view -> {
                Toast.makeText(this,type,Toast.LENGTH_SHORT).show();
                Intent intentFile = new Intent(this , UploadFile.class);
                intentFile.putExtra("type","dekripsi");
                startActivity(intentFile);
            });
        }
    }

}