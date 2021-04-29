package com.facedetectionusingcamera.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.facedetectionusingcamera.R;
import com.facedetectionusingcamera.helper.GraphicOverlay;
import com.facedetectionusingcamera.helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private CameraView cameraView;
    private GraphicOverlay graphicOverlay;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
        clickButton();
        clickCamera();


    }


    private void initViews()
    {
        cameraView = findViewById(R.id.camera_kit_view);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        button = findViewById(R.id.btn_detect_face);
    }

    private void clickButton()
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }

        });
    }

    private void clickCamera()
    {
        cameraView.addCameraKitListener(new CameraKitEventListener()
        {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent)
            {
            }

            @Override
            public void onError(CameraKitError cameraKitError)
            {
                Toast.makeText(MainActivity.this, cameraKitError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage)
            {
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap , cameraView.getWidth() , cameraView.getHeight() , true);
                cameraView.start();

                processFaceDetect(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo)
            {
            }
        });
    }

    private void processFaceDetect(Bitmap bitmap)
    {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions = new FirebaseVisionFaceDetectorOptions.Builder().build();
        FirebaseVisionFaceDetector firebaseVisionFaceDetector = FirebaseVision.getInstance().getVisionFaceDetector(firebaseVisionFaceDetectorOptions);
        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage)
        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>()
        {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces)
            {
                faceResult(firebaseVisionFaces);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void faceResult(List<FirebaseVisionFace> firebaseVisionFaces)
    {

        for (FirebaseVisionFace visionFace : firebaseVisionFaces)
        {
            Rect rect = visionFace.getBoundingBox();
            RectOverlay rectOverlay = new RectOverlay(graphicOverlay , rect);
            graphicOverlay.add(rectOverlay);
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        cameraView.start();
    }
}