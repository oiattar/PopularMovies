package com.example.oi156f.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oi156f.popularmovies.data.FavoritesContract.*;

import com.example.oi156f.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsFragment extends Fragment implements View.OnClickListener {
    //@BindView(R.id.movie_title) TextView title;
    @BindView(R.id.movie_image) ImageView image;
    @BindView(R.id.movie_backdrop) ImageView backdrop;
    @BindView(R.id.movie_date) TextView date;
    @BindView(R.id.movie_rating) TextView rating;
    @BindView(R.id.movie_synopsis) TextView synopsis;
    @BindView(R.id.favorite_button) FloatingActionButton favoriteButton;
    @BindView(R.id.details_layout) ConstraintLayout detailsLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Movie movie = null;

    private Unbinder unbinder;

    private boolean isFavorite;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        favoriteButton.setOnClickListener(this);
        Intent intent = getActivity().getIntent();
        if (savedInstanceState != null) {
            isFavorite = savedInstanceState.getBoolean(MovieUtils.FAVORITES_KEY);
        }
        if(intent.hasExtra(getString(R.string.intent_tag))) {
            movie = intent.getParcelableExtra(getString(R.string.intent_tag));
            isFavorite = checkIfFavorite(movie);
            new FetchDetailsTask(getActivity(), rootView).execute(movie);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            collapsingToolbarLayout.setTitle(movie.getTitle());
            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            //title.setText(movie.getTitle());
            Picasso.with(getActivity()).load(movie.getPoster()).into(image);
            Picasso.with(getActivity()).load(movie.getBackdrop()).into(backdrop);
            date.setText(movie.getReleaseDate().substring(0, 4));
            rating.setText(getString(R.string.movie_rating, movie.getRating()));
            synopsis.setText(movie.getOverview());
            setFavIcon(isFavorite);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MovieUtils.FAVORITES_KEY, isFavorite);
    }

    private void addFavorite() {

        if (movie == null) return;

        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoritesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FavoritesEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavoritesEntry.COLUMN_POSTER_PATH, movie.getPoster());
        contentValues.put(FavoritesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoritesEntry.COLUMN_RUNTIME, movie.getRuntime());
        contentValues.put(FavoritesEntry.COLUMN_RATING, movie.getRating());
        contentValues.put(FavoritesEntry.COLUMN_SYNOPSIS, movie.getOverview());

        Uri uri = getContext().getContentResolver().insert(FavoritesEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            String message = "\'" + movie.getTitle() + "\' added to Favorites";
            Snackbar.make(detailsLayout, message, Snackbar.LENGTH_SHORT).show();
            setFavIcon(true);
        }
    }

    private void deleteFavorite() {
        Uri uri = FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(""+movie.getId()).build();
        int numDeleted = getContext().getContentResolver().delete(uri, null, null);

        if (numDeleted != 0) {
            String message = "\'" + movie.getTitle() + "\' removed from Favorites";
            Snackbar.make(detailsLayout, message, Snackbar.LENGTH_SHORT).show();
            setFavIcon(false);
        }
    }

    private boolean checkIfFavorite(Movie movie) {
        Uri uri = FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(""+movie.getId()).build();
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        boolean fav = (cursor.getCount() > 0);
        cursor.close();
        return fav;
    }

    private void setFavIcon(boolean isFavorite) {
        if (isFavorite)
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
        else
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_button:
                if (!isFavorite) {
                    addFavorite();
                    isFavorite = true;
                } else {
                    deleteFavorite();
                    isFavorite = false;
                }
        }
    }
}
