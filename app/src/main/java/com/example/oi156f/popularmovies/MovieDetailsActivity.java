package com.example.oi156f.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        title = (TextView) findViewById(R.id.movie_title);
        Intent intent = getIntent();
        if(intent.hasExtra("SelectedMovie")) {
            Movie movie = intent.getParcelableExtra("SelectedMovie");
            title.setText(movie.getTitle());
        }
    }
}
