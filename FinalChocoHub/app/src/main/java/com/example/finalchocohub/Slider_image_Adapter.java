package com.example.finalchocohub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class Slider_image_Adapter extends RecyclerView.Adapter<Slider_image_Adapter.SliderViewHolder>{

    private List<Slider_item> slider_items;
    private ViewPager2 viewPager2;

    public Slider_image_Adapter(List<Slider_item> slider_items, ViewPager2 viewPager2) {
        this.slider_items = slider_items;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slider_itel_container,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slider_items.get(position));
        if (position == slider_items.size() -2 ){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return slider_items.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageslider);
        }
        void setImage(Slider_item slider_item)
        {
            imageView.setImageResource(slider_item.getImage());
        }
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            slider_items.addAll(slider_items);
            notifyDataSetChanged();
        }
    };
}

