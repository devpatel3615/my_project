package com.example.finalchocohub.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.finalchocohub.R;
import com.example.finalchocohub.Slider_image_Adapter;
import com.example.finalchocohub.Slider_item;

import java.util.ArrayList;
import java.util.List;


public class    Home_Fragment extends Fragment {
    ViewPager2 viewPager2;
    private Handler sliderhandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home_, container, false);
        View view = inflater.inflate(R.layout.fragment_home_, container, false);


        viewPager2 = view.findViewById(R.id.view_page_slider);

        List<Slider_item> slider_items = new ArrayList<>();
        slider_items.add(new Slider_item(R.drawable.i_slider1));
        slider_items.add(new Slider_item(R.drawable.i_slider2));
        slider_items.add(new Slider_item(R.drawable.i_slider3));
        slider_items.add(new Slider_item(R.drawable.i_slider4));

        viewPager2.setAdapter(new Slider_image_Adapter(slider_items, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderhandler.removeCallbacks(sliderRunnable);
                sliderhandler.postDelayed(sliderRunnable,3000);
            }
        });

        return view;
    }

    private Runnable sliderRunnable =new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderhandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderhandler.postDelayed(sliderRunnable,300);
    }
}