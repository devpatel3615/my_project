package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Home_fg extends AppCompatActivity {

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fg);

        viewPager2 = findViewById(R.id.view_page_slider);
        List<Slider_item> slider_items =new ArrayList<>();
        slider_items.add(new Slider_item(R.drawable.i_slider1));
        slider_items.add(new Slider_item(R.drawable.i_slider2));
        slider_items.add(new Slider_item(R.drawable.i_slider3));
        slider_items.add(new Slider_item(R.drawable.i_slider4));

        viewPager2.setAdapter(new Slider_image_Adapter(slider_items,viewPager2));

    }
}