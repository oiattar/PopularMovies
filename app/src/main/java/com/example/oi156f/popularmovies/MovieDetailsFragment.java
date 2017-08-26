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

    TextView title;
    ImageView image;
    TextView date;
    TextView rating;
    TextView synopsis;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        title = (TextView) rootView.findViewById(R.id.movie_title);
        image = (ImageView) rootView.findViewById(R.id.movie_image);
        date = (TextView) rootView.findViewById(R.id.movie_date);
        rating = (TextView) rootView.findViewById(R.id.movie_rating);
        synopsis = (TextView) rootView.findViewById(R.id.movie_synopsis);
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
