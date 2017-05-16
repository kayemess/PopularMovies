package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ui.MovieListFragment;
import com.example.android.popularmovies.utilities.NetworkConstants;
import com.example.android.popularmovies.utilities.NetworkUtils;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieListAdapter extends FragmentPagerAdapter {

    private Context mContext;

    private MovieListFragment mFavoritesFrag;
    private MovieListFragment mTopRatedFrag;
    private MovieListFragment mPopularFrag;

    public MovieListAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        //TODO: Refactor so that parameters aren't passed when creating fragment
        mFavoritesFrag = new MovieListFragment(this);
        mTopRatedFrag = new MovieListFragment(this);
        mPopularFrag = new MovieListFragment(this);

        Bundle popularBundleArgs = new Bundle();
        popularBundleArgs.putString(MovieListFragment.MOVIE_LIST_API_PATH, NetworkConstants.PATH_POPULAR);
        mPopularFrag.setArguments(popularBundleArgs);

        Bundle topRatedBundleArgs = new Bundle();
        topRatedBundleArgs.putString(MovieListFragment.MOVIE_LIST_API_PATH, NetworkConstants.PATH_TOP_RATED);
        mTopRatedFrag.setArguments(topRatedBundleArgs);

        Bundle favoritesBundleArgs = new Bundle();
        favoritesBundleArgs.putString(MovieListFragment.MOVIE_LIST_API_PATH, NetworkConstants.PATH_FAVORTIES);
        mFavoritesFrag.setArguments(favoritesBundleArgs);
    }

    @Override
    public MovieListFragment getItem(int position) {
        switch (position) {
            case 1:
                return mTopRatedFrag;
            case 2:
                return mFavoritesFrag;
            case 0:
            default:
                return mPopularFrag;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return mContext.getString(R.string.top_rated);
            case 2:
                return mContext.getString(R.string.favorite);
            case 0:
            default:
                return mContext.getString(R.string.most_popular);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        MovieListFragment currentFrag = (MovieListFragment) object;
        Bundle args = currentFrag.getArguments();

        String currentPage = args.getString(MovieListFragment.MOVIE_LIST_API_PATH);

        if(currentPage.equals(NetworkConstants.PATH_FAVORTIES)){
            // POSITION_NONE causes favorites page to reload if on favorites... in case a movie has been removed from faves list
            // side effect is that the page flickers if movie list hasn't changed
            // TODO: check to see if favorites list has changed; if it hasn't, return POSITION_UNCHANGED, otherwise POSITION_NONE
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }
}