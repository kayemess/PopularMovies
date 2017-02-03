package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by kristenwoodward on 2/3/17.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    @Override
    public boolean canScrollVertically() {
        return false;
    }

    public CustomLinearLayoutManager(Context context) {
        super(context);


    }
}
