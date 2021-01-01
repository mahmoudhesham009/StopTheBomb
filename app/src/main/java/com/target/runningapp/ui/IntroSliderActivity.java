package com.target.runningapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.target.runningapp.R;
import com.target.runningapp.adapter.AppIntroAdapter;

public class IntroSliderActivity extends AppCompatActivity {
    ViewPager viewPager;
    AppIntroAdapter appIntroAdapter;
    ImageButton imageButton;
    boolean finalNext=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        viewPager=findViewById(R.id.view_pager);
        imageButton=findViewById(R.id.next);
        final ImageView dots[]=new ImageView[]{findViewById(R.id.dot_1),findViewById(R.id.dot_2),findViewById(R.id.dot_3)};
        appIntroAdapter=new AppIntroAdapter(this);
        viewPager.setAdapter(appIntroAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dots.length;i++){
                    dots[i].setImageResource(R.drawable.button_2);
                }
                dots[position].setImageResource(R.drawable.button_1);
                if(viewPager.getCurrentItem()==appIntroAdapter.getCount()-1){
                    imageButton.setImageResource(R.drawable.ic_baseline_check_24);
                    finalNext=true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalNext){
                    startActivity(new Intent(getBaseContext(), MapActivity.class));
                    finish();
                }else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }
}