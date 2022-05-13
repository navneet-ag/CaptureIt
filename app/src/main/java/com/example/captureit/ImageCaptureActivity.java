package com.example.captureit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class ImageCaptureActivity extends AppCompatActivity implements View.OnClickListener {
    private Button capImg;
    private PreviewView imgPreview;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        capImg = findViewById(R.id.image_capture_button);
        imgPreview = findViewById(R.id.viewFinder);
        capImg.setOnClickListener(this);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() ->
        {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }
    private Executor getExecutor()
    {
        return ContextCompat.getMainExecutor(this);

    }
    private void startCameraX(ProcessCameraProvider cameraProvider)
    {
        //Camera Selection
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder().
                requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();

        // Preview
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(imgPreview.getSurfaceProvider());

        // Image capture
         imageCapture = new ImageCapture.Builder().
                                setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
         cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);
    }
    @Override
    public void onClick(View view) {
    if(view.getId()==R.id.image_capture_button)
    {
        clickPic();
    }
    }

    private void clickPic() {
        File photoDir = new File("storage/emulated/0/Pictures/ClickIt");
        if(!photoDir.exists())
            photoDir.mkdir();
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() +"/" + timestamp + ".jpg";
        File photoFile = new File(photoFilePath);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(ImageCaptureActivity.this,"Photo captured",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(ImageCaptureActivity.this,"Some error occurred " +exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}