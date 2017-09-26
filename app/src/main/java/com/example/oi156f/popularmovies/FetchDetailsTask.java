package com.example.oi156f.popularmovies;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.example.oi156f.popularmovies.adapters.ReviewAdapter;
import com.example.oi156f.popularmovies.adapters.TrailerAdapter;
import com.example.oi156f.popularmovies.utilities.MovieUtils;
import com.example.oi156f.popularmovies.Movie.*;

import java.net.URL;

/**
 * Created by oiatt on 9/24/2017.
 * AsyncTask to fetch details of selected movie like trailers and reviews
 */

class FetchDetailsTask extends AsyncTask<Movie, Movie, Movie> {

    private Activity mActivity;
    private View rootView;
    @BindView(R.id.trailers_list) RecyclerView trailersList;
    @BindView(R.id.reviews_list) RecyclerView reviewsList;
    @BindView(R.id.movie_runtime) TextView runtime;
    @BindView(R.id.trailers_loading_icon) ProgressBar trailersLoading;
    @BindView(R.id.reviews_loading_icon) ProgressBar reviewsLoading;

    private Unbinder unbinder;

    FetchDetailsTask(Activity activity, View view) {
        mActivity = activity;
        rootView = view;
        unbinder = ButterKnife.bind(this, rootView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        trailersLoading.setVisibility(View.VISIBLE);
        reviewsLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected Movie doInBackground(Movie... params) {
        Movie movie = params[0];
        URL detailsUrl = MovieUtils.buildDetailsUrl(movie.getId(), MovieUtils.ALL);
        URL trailersUrl = MovieUtils.buildDetailsUrl(movie.getId(), MovieUtils.TRAILERS);
        URL reviewsUrl = MovieUtils.buildDetailsUrl(movie.getId(), MovieUtils.REVIEWS);

        try {
            String detailsJson = MovieUtils.getResponseFromHttpUrl(detailsUrl);
            movie.setRuntime(MovieUtils.getRuntimeFromJson(detailsJson));
            publishProgress(movie);
            String trailersJson = MovieUtils.getResponseFromHttpUrl(trailersUrl);
            Trailer[] trailers = MovieUtils.getTrailersFromJson(trailersJson);
            movie.setTrailers(trailers);
            String reviewsJson = MovieUtils.getResponseFromHttpUrl(reviewsUrl);
            Review[] reviews = MovieUtils.getReviewsFromJson(reviewsJson);
            movie.setReviews(reviews);
            return movie;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Movie... movie) {
        runtime.setText(mActivity.getString(R.string.movie_runtime, movie[0].getRuntime()));
    }

    @Override
    protected void onPostExecute(Movie movie) {
        trailersLoading.setVisibility(View.GONE);
        reviewsLoading.setVisibility(View.GONE);
        if(movie != null) {
            TrailerAdapter trailerAdapter = new TrailerAdapter(mActivity, movie.getTrailers());
            trailersList.setAdapter(trailerAdapter);
            trailersList.setLayoutManager(new LinearLayoutManager(mActivity));
            ReviewAdapter reviewAdapter = new ReviewAdapter(mActivity, movie.getReviews());
            reviewsList.setAdapter(reviewAdapter);
            reviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
        }
        unbinder.unbind();
    }
}
