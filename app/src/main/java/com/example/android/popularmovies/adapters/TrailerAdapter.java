package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Trailer;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieTrailerAdapterViewHolder> {
    private Trailer[] mMovieTrailerList;
    private Context mContext;
    private ListItemClickListener mOnItemClickListener;

    public interface ListItemClickListener {
        void onItemClickListener(int position);
    }

    public TrailerAdapter(ListItemClickListener listItemClickListener) {
        mOnItemClickListener = listItemClickListener;
    }

    @Override
    public MovieTrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_trailer_detail, parent, false);

        mContext = view.getContext();

        return new TrailerAdapter.MovieTrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapterViewHolder holder, int position) {
        String trailerType = mMovieTrailerList[position].getTrailerType();
        String CTA = "Play " + trailerType;
        holder.mMovieTrailerNameTV.setText(CTA);

        String trailerDescription = mMovieTrailerList[position].getTrailerName();
        holder.mMovieTrailerDescriptionTV.setText(trailerDescription);
    }

    @Override
    public int getItemCount() {
        if (mMovieTrailerList == null) return 0;
        return mMovieTrailerList.length;
    }

    public void setMovieTrailerData(Trailer[] trailerData) {
        mMovieTrailerList = trailerData;
        notifyDataSetChanged();
    }

    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mMovieTrailerNameTV;
        private final TextView mMovieTrailerDescriptionTV;

        private MovieTrailerAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mMovieTrailerNameTV = (TextView) itemView.findViewById(R.id.trailer_name_tv);
            mMovieTrailerDescriptionTV = (TextView) itemView.findViewById(R.id.trailer_details_tv);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnItemClickListener.onItemClickListener(position);
        }
    }
}
