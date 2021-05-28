package com.rasyidcode.a4_sliding_image_with_viewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] imagesUrl = {
            "https://cdn-images.win.gg/resize/w/932/h/420/format/jpg/type/progressive/fit/cover/path/news/ac2460b56866901d732f996b82b69d31/f73a950d8b5cd36f005fcb826f864999/original.jpg",
            "https://cdn.vox-cdn.com/thumbor/htJinTo2xskjNE1oHhXTTStCm1Q=/0x0:4528x3019/1200x800/filters:focal(1902x1148:2626x1872)/cdn.vox-cdn.com/uploads/chorus_image/image/66449281/1078353108.jpg.0.jpg",
            "https://image-cdn.essentiallysports.com/wp-content/uploads/20200806114912/pokimane-announces-esports-scholarship-uci1.png",
            "https://staticg.sportskeeda.com/editor/2020/07/56a3f-15950665366630-800.jpg",
            "https://image-cdn.essentiallysports.com/wp-content/uploads/20200819193717/Capture-14.png"
    };

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mViewPager = findViewById(R.id.view_pager_test);
        mViewPagerAdapter = new ViewPagerAdapter(MainActivity.this, this.imagesUrl);
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}