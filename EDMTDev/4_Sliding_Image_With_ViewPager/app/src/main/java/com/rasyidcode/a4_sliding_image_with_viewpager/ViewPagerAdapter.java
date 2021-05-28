package com.rasyidcode.a4_sliding_image_with_viewpager;

import android.app.Activity;
import android.content.Context;
import android.media.tv.TvView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Activity activity;
    private String[] listImagesUrl;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity, String[] listImagesUrl) {
        this.activity = activity;
        this.listImagesUrl = listImagesUrl;
    }

    @Override
    public int getCount() {
        return listImagesUrl.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_viewpager, container, false);

        ImageView imageView;
        imageView = itemView.findViewById(R.id.image_test);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        imageView.setMinimumHeight(height);
        imageView.setMinimumWidth(width);

        try {
            Log.d(ViewPagerAdapter.class.getSimpleName(), "load image");
            Log.d(ViewPagerAdapter.class.getSimpleName(), "image : " + listImagesUrl[position]);

            Picasso.get()
                    .load(listImagesUrl[position])
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
