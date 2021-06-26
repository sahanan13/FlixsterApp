package com.example.flixsterapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixsterapp.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private static final String TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    //displayed movie
    Movie movie;

    //View objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView detailPoster;
    //Context context;
    ImageView ytIcon;
    public String movieKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int radius = 30;
        int margin = 10;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.detTitle);
        tvOverview = (TextView) findViewById(R.id.detOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.ratingBar);
        detailPoster = (ImageView) findViewById(R.id.detailPoster);
        ytIcon = (ImageView) findViewById(R.id.ytButton);


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
//        Log.e("DetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        Log.e("DetailsActivity", "Poster Path: " + movie.getPosterPath());

        ytIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trailerIntent = new Intent(DetailsActivity.this, MovieTrailerActivity.class);
                //movie id
                trailerIntent.putExtra("trailerKey", movieKey);
                startActivity(trailerIntent);
                //String videoId = "tKodtNFpzBA";
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://www.youtube.com/watch?v=%s", videoId))));
            }
        });
        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        Glide.with(DetailsActivity.this).load(movie.getPosterPath()).centerCrop().transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.flicks_movie_placeholder).into(detailPoster);

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(formatUrl(movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    String movieString = results.getString(0);
                    JSONObject movieObject = new JSONObject(movieString);
                    movieKey = movieObject.getString("key");
                    Log.d(TAG, "Movie key: " + movieKey);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public String formatUrl(int movieId){
        return String.format(TRAILER_URL, movieId);
    }
}


