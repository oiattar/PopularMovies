package com.example.oi156f.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oi156f.popularmovies.Movie.*;
import com.example.oi156f.popularmovies.R;

/**
 * Created by oiatt on 9/24/2017.
 * Adapter to populate reviews RecyclerView
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context mContext;

    private Review[] mReviews;

    public ReviewAdapter(Context context, Review[] reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews[position];

        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
        if (position == getItemCount() - 1)
            holder.divider.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mReviews.length;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        public TextView content;
        View divider;

        ReviewViewHolder(View view) {
            super(view);

            author = (TextView) view.findViewById(R.id.review_author);
            content = (TextView) view.findViewById(R.id.review_content);
            divider = view.findViewById(R.id.review_divider);
        }
    }
}
