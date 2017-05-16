package com.example.android.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by kristenwoodward on 2/3/17.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
