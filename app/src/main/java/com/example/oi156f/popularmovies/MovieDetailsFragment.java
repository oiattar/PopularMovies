package com.example.oi156f.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.movie_title);
        ImageView image = (ImageView) rootView.findViewById(R.id.movie_image);
        TextView date = (TextView) rootView.findViewById(R.id.movie_date);
        TextView rating = (TextView) rootView.findViewById(R.id.movie_rating);
        TextView synopsis = (TextView) rootView.findViewById(R.id.movie_synopsis);
        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(getString(R.string.intent_tag))) {
            Movie movie = intent.getParcelableExtra(getString(R.string.intent_tag));
            title.setText(movie.getTitle());
            Picasso.with(getActivity()).load(movie.getPoster()).into(image);
            date.setText(movie.getReleaseDate().substring(0, 4));
            rating.setText(getString(R.string.movie_rating, movie.getRating()));
            synopsis.setText(movie.getOverview());
        }
        return rootView;
    }
}
