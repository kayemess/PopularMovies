package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by kristenwoodward on 12/7/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovieList;
    private ImageView mPosterImageView;
    private Context mContext;
    //private TextView mTempTv;

    public MovieAdapter() {
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMoviePoster = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachImmediately = false;

        View view = inflater.inflate(layoutIdForMoviePoster, parent, shouldAttachImmediately);

        mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster_iv);
        mContext = view.getContext();
        //mTempTv = (TextView) view.findViewById(R.id.temporary_tv);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onViewRecycled(MovieAdapterViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        if (holder.isRecyclable()) {
            String posterUrlString = mMovieList[position].getPosterUrl().toString();
            Picasso.with(mContext).load(posterUrlString).into(holder.getPosterImageView());

            Log.v("MovieAdapter", "The following movie was just bound: " + mMovieList[position].getMovieName());
        } else
            return;
        //mTempTv.setText(posterUrlString);
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mPosterImageView;


        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.movie_poster_iv);
        }

        public ImageView getPosterImageView() {
            return this.mPosterImageView;
        }
    }

    public void setMovieData(Movie[] movieData) {
        mMovieList = movieData;
        notifyDataSetChanged();
    }

}
