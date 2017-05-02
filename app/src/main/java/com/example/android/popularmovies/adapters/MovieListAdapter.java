package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragments.MovieListFragment;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieListAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final static String FILTER_SELECTION = "filterSelection";

    private MovieListFragment mFavoritesFrag;
    private MovieListFragment mTopRatedFrag;
    private MovieListFragment mPopularFrag;

    private Bundle mFavoritesBundleArgs = new Bundle();
    private Bundle mTopRatedBundleArgs = new Bundle();
    private Bundle mPopularBundleArgs = new Bundle();

    public MovieListAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        mFavoritesFrag = new MovieListFragment(this);
        mTopRatedFrag = new MovieListFragment(this);
        mPopularFrag = new MovieListFragment(this);

        mPopularBundleArgs.putInt(FILTER_SELECTION, 0);
        mPopularFrag.setArguments(mPopularBundleArgs);

        mTopRatedBundleArgs.putInt(FILTER_SELECTION, 1);
        mTopRatedFrag.setArguments(mTopRatedBundleArgs);

        mFavoritesBundleArgs.putInt(FILTER_SELECTION, 2);
        mFavoritesFrag.setArguments(mFavoritesBundleArgs);

    }

    @Override
    public MovieListFragment getItem(int position) {
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
        MovieListFragment currentFrag = (MovieListFragment) object;
        Bundle args = currentFrag.getArguments();

        int currentPage = args.getInt(FILTER_SELECTION);

        if(currentPage == 2){
            // POSITION_NONE causes favorites page to reload if on favorites... in case a movie has been removed from faves list
            // side effect is that the page flickers if movie list hasn't changed
            // TODO: check to see if favorites list has changed; if it hasn't, return POSITION_UNCHANGED, otherwise POSITION_NONE
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }

    }


}