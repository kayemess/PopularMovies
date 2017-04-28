package com.example.android.popularmovies.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.FilterAdapter;

public class MainActivity extends AppCompatActivity {

    public boolean shouldUpdateFavorites = false;

    public void updateFavorites(){
        shouldUpdateFavorites = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        final FilterAdapter adapter = new FilterAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

}