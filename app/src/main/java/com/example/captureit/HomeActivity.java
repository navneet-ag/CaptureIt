package com.example.captureit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HomeActivity extends AppCompatActivity {
    public static final int CAM_REQUEST_CODE = 102;
    private Button selfieBtn, galBtn;
    private ImageView curImg;
    private static int camReqCode = 100;
    private static int writeReqCode = 704;
    private static int readReqCode = 705;

    private static String TAG = "HomeActivity";
    private static Bitmap rotateBitmap(Bitmap source,  float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(), matrix, true
        );
    }


    private void checkCamPermission()
    {
        Log.d(TAG,"In checkCamPermission");
//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},writeReqCode);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},camReqCode);
        }

        else
        {
            capImg();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"In onRequestPermissionsResult");
        Log.d(TAG,grantResults[0] + " " + permissions[0]);
        if(permissions.length!=0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG,"In if condition");
            capImg();
        }
        else
        {
            Toast.makeText(HomeActivity.this, "Please provide permission to access Camera", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void capImg() {
        Intent intent = new Intent(HomeActivity.this,ImageCaptureActivity.class);
        HomeActivity.this.startActivity(intent);
//        Toast.makeText(HomeActivity.this, "Permission was granted", Toast.LENGTH_SHORT).show();
//        Intent camIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camIntent, CAM_REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},writeReqCode);
//        }
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, new String[] {},readReqCode);
//        }
//
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        selfieBtn = findViewById(R.id.selfieBtn);
        galBtn = findViewById(R.id.galleryBtn);
        curImg = findViewById(R.id.imageView);

        selfieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCamPermission();

            }
        });
    }
}