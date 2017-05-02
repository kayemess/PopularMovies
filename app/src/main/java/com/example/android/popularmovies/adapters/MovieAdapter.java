package com.example.android.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by kristenwoodward on 12/7/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovieList;
    private Context mContext;
    private ListItemClickListener mOnItemClickListener;


    public interface ListItemClickListener{
        void onItemClickListener(int position, View posterView, String transitionName);
    }

    public MovieAdapter(ListItemClickListener listItemClickListener) {
        mOnItemClickListener = listItemClickListener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMoviePoster = R.layout.list_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachImmediately = false;

        View view = inflater.inflate(layoutIdForMoviePoster, parent, shouldAttachImmediately);

        mContext = view.getContext();

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onViewRecycled(MovieAdapterViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        if (holder.isRecyclable()) {
            String posterUrlString = mMovieList[position].getPosterPath();
            Picasso.with(mContext).load(posterUrlString).into(holder.getPosterImageView());
            holder.getPosterImageView().setTransitionName(createTransitionName(position));
        } else
            return;
    }

    private String createTransitionName(int position){
        return mContext.getString(R.string.poster_transition_name) + mMovieList[position].getMovieId();
    }

    @Override
    public long getItemId(int position) {
        Movie movie = mMovieList[position];
        return (movie.getMovieName() + movie.getPosterPath()).hashCode();
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final ImageView mPosterImageView;


        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.movie_poster_iv);
            mPosterImageView.setOnClickListener(this);
        }

        public ImageView getPosterImageView() {
            return this.mPosterImageView;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnItemClickListener.onItemClickListener(position, mPosterImageView, mPosterImageView.getTransitionName());

        }
    }
        public void setMovieData(Movie[] movieData) {
            mMovieList = movieData;
            notifyDataSetChanged();
        }


}
