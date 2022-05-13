package com.example.captureit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.core.Tag;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView curImg;
    private Button cropBtn,rotBtn,saveBtn;
    private Bitmap curBitMap;
    private String Tag = "EditActivity";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        curBitMap = (Bitmap) intent.getParcelableExtra("BitMap");
        curImg = findViewById(R.id.imageViewEdit);
        curImg.setImageBitmap(curBitMap);
        cropBtn = findViewById(R.id.cropBtn);
        rotBtn = findViewById(R.id.rotBtn);
        saveBtn = findViewById(R.id.saveBtn);
        rotBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.cropBtn)
        {

            if(curBitMap!=null){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                curBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), curBitMap, imageFileName+".jpg", null);
//            System.out.println(Uri.parse(path));
                Uri uri= Uri.parse(path);
            }
            CropImage.activity(uri)
                    .start(this);
        }
        if(view.getId()==R.id.rotBtn)
        {
            Toast.makeText(this,"Rotating by 90 degrees",Toast.LENGTH_SHORT).show();
            curBitMap = HomeActivity.rotateBitmap(curBitMap,90);
            curImg.setImageBitmap(curBitMap);
        }

        if(view.getId()==R.id.saveBtn)
        {

        }

    }
}