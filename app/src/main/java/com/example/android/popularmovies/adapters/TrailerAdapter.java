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

    public TrailerAdapter(ListItemClickListener listItemClickListener){
        mOnItemClickListener = listItemClickListener;
    }

    @Override
    public MovieTrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForTrailer = R.layout.fragment_trailer_detail;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachImmediately = false;

        View view = inflater.inflate(layoutIdForTrailer, parent, shouldAttachImmediately);

        mContext = view.getContext();

        return new TrailerAdapter.MovieTrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapterViewHolder holder, int position) {
        //if (holder.isRecyclable()) {
            String trailerType = mMovieTrailerList[position].getTrailerType();
            String CTA = "Play " + trailerType;
            holder.mMovieTrailerNameTV.setText(CTA);

            String trailerDescription = mMovieTrailerList[position].getTrailerName();
            holder.mMovieTrailerDescriptionTV.setText(trailerDescription);
        //} else
        //    return;
    }

    @Override
    public int getItemCount() {
        if (mMovieTrailerList == null) return 0;
        return mMovieTrailerList.length;
    }

    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mMovieTrailerNameTV;
        public final TextView mMovieTrailerDescriptionTV;


        public MovieTrailerAdapterViewHolder(View itemView) {
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

    public void setMovieTrailerData(Trailer[] trailerData){
        mMovieTrailerList = trailerData;
        notifyDataSetChanged();
    }
}
