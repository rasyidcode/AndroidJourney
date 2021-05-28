package com.rasyidcode.a3_load_image_from_url;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    public static String IMAGE_URL = "https://foto.wartaekonomi.co.id/files/arsip_foto_2019_07_29/kimi_hime_120005_big.webp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.image_test);
        Picasso.get().load(IMAGE_URL)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Load image successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this, "Load image failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}