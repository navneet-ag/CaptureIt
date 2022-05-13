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
    private static String TAG = "HomeActivity";
    public static Bitmap rotateBitmap(Bitmap source,  float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(), matrix, true
        );
    }


    private void checkCamPermission()
    {
        Log.d(TAG,"In checkCamPermission");
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
        Toast.makeText(HomeActivity.this, "Permission was granted", Toast.LENGTH_SHORT).show();
        Intent camIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST_CODE)
        {
            Bitmap curImgBitMap  = (Bitmap) data.getExtras().get("data");
            // Reference https://stackoverflow.com/questions/7698409/android-transform-a-bitmap-into-an-input-stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            curImgBitMap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] bitmapdata = baos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

            Bitmap rotatedBitmap = curImgBitMap;
            rotatedBitmap = rotateBitmap(curImgBitMap,270);

            curImg.setImageBitmap(rotatedBitmap);
            Intent intent = new Intent(HomeActivity.this,EditActivity.class);
            intent.putExtra("BitMap",rotatedBitmap);
            HomeActivity.this.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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