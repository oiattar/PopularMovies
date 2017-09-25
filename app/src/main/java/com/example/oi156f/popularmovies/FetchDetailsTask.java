package com.example.oi156f.popularmovies;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.oi156f.popularmovies.utilities.MovieUtils;
import com.example.oi156f.popularmovies.Movie.*;

import java.net.URL;

/**
 * Created by oiatt on 9/24/2017.
 */

public class FetchDetailsTask extends AsyncTask<Movie, Void, Movie> {

    private Activity mActivity;
    private View rootView;
    private RecyclerView trailersList;
    private RecyclerView reviewsList;
    private TextView runtime;

    public FetchDetailsTask(Activity activity, View view) {
        mActivity = activity;
        rootView = view;
        trailersList = (RecyclerView) view.findViewById(R.id.trailers_list);
        reviewsList = (RecyclerView) view.findViewById(R.id.reviews_list);
        runtime = (TextView) view.findViewById(R.id.movie_runtime);
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
    protected void onPostExecute(Movie movie) {
        //loadingIcon.setVisibility(View.INVISIBLE);
        if(movie != null) {
            //TODO: move runtime to onProgressUpdate()
            runtime.setText(mActivity.getString(R.string.movie_runtime, movie.getRuntime()));
            TrailerAdapter trailerAdapter = new TrailerAdapter(mActivity, movie.getTrailers());
            trailersList.setAdapter(trailerAdapter);
            trailersList.setLayoutManager(new LinearLayoutManager(mActivity));
            ReviewAdapter reviewAdapter = new ReviewAdapter(mActivity, movie.getReviews());
            reviewsList.setAdapter(reviewAdapter);
            reviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
        } else {
            //showErrorMessage();
        }
    }
}
