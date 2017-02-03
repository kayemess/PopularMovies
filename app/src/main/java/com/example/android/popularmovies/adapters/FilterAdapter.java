package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragments.FilterFragment;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class FilterAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final static String FILTER_SELECTION = "filterSelection";

    private FilterFragment mFavoritesFrag;
    private FilterFragment mTopRatedFrag;
    private FilterFragment mPopularFrag;

    private Bundle mFavoritesBundleArgs = new Bundle();
    private Bundle mTopRatedBundleArgs = new Bundle();
    private Bundle mPopularBundleArgs = new Bundle();

    public FilterAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        mFavoritesFrag = new FilterFragment(this);
        mTopRatedFrag = new FilterFragment(this);
        mPopularFrag = new FilterFragment(this);

        mPopularBundleArgs.putInt(FILTER_SELECTION, 0);
        mPopularFrag.setArguments(mPopularBundleArgs);

        mTopRatedBundleArgs.putInt(FILTER_SELECTION, 1);
        mTopRatedFrag.setArguments(mTopRatedBundleArgs);

        mFavoritesBundleArgs.putInt(FILTER_SELECTION, 2);
        mFavoritesFrag.setArguments(mFavoritesBundleArgs);

    }

    @Override
    public FilterFragment getItem(int position) {
        Bundle args = new Bundle();

        switch (position) {
            case 0: return mPopularFrag;
            case 1: return mTopRatedFrag;
            case 2: return mFavoritesFrag;
            default: return mPopularFrag;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0: return mContext.getString(R.string.most_popular);
            case 1: return mContext.getString(R.string.top_rated);
            case 2: return mContext.getString(R.string.favorite);
            default: return mContext.getString(R.string.most_popular);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        FilterFragment currentFrag = (FilterFragment) object;
        Bundle args = currentFrag.getArguments();
        int currentPage = args.getInt(FILTER_SELECTION);
        if(currentPage == 2){
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }

    }


}