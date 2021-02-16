package com.target.runningapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.target.runningapp.R;

import java.util.ArrayList;
import java.util.List;

public class AppIntroAdapter extends PagerAdapter {
    private Context context;
    public List<Integer> images = new ArrayList<>();
    public List<String> text = new ArrayList<>();


    public AppIntroAdapter(Context context) {
        this.context = context;
        this.addPage(R.drawable.ic_undraw_fitness_stats_sht6,context.getResources().getString(R.string.first_page));
        this.addPage(R.drawable.ic_undraw_map_light_3hjy,context.getResources().getString(R.string.second_page));
        this.addPage(R.drawable.ic_undraw_working_out_6psf,context.getResources().getString(R.string.third_page));
    }


    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private void addPage(int image, String text) {
        this.images.add(image);
        this.text.add(text);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_info_page, container, false);
        ImageView imageView=v.findViewById(R.id.info_image);
        TextView textView=v.findViewById(R.id.info_text);
        imageView.setImageResource(images.get(position));
        textView.setText(text.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
