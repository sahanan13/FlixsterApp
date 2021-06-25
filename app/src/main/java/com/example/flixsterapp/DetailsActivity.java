package com.example.flixsterapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flixsterapp.models.Movie;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    //displayed movie
    Movie movie;

    //View objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView detailPoster;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.detTitle);
        tvOverview = (TextView) findViewById(R.id.detOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.ratingBar);
        detailPoster = (ImageView) findViewById(R.id.detailPoster);


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
//        Log.e("DetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        Log.e("DetailsActivity", "Poster Path: " + movie.getPosterPath());

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        Glide.with(DetailsActivity.this).load(movie.getPosterPath()).placeholder(R.drawable.flicks_movie_placeholder).into(detailPoster);

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);
    }

}
