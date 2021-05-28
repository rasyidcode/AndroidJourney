package com.rasyidcode.a5_zooming_imageview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {

//    private ImageView mImageView;
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mImageView = findViewById(R.id.image_pokimane);
//        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(mImageView);
//        photoViewAttacher.update();

        mPhotoView = findViewById(R.id.photo_view_pokimane);
    }
}