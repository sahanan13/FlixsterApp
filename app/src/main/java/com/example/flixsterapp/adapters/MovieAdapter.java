package com.example.flixsterapp.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixsterapp.DetailsActivity;
import com.example.flixsterapp.R;
import com.example.flixsterapp.databinding.ActivityMainBinding;
import com.example.flixsterapp.databinding.ItemMovieBinding;
import com.example.flixsterapp.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //Context context;
    Activity activity; //for boilerplate
    List<Movie> movies;

    public MovieAdapter(Activity activity, List<Movie> movies) {
        this.activity = activity;
        this.movies = movies;
    }

    //Usually involves inflating a layout from XML and returning the holder
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        //View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        //return new ViewHolder(movieView);
        ItemMovieBinding binding = ItemMovieBinding.inflate(activity.getLayoutInflater(), parent, false);
        View movieView = binding.getRoot();
        return new ViewHolder(movieView, binding);
        //View view = binding.getRoot();
        //setContentView(view);

    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(View itemView, ItemMovieBinding binding) {
            super(itemView);
            tvTitle = binding.tvTitle;
            tvOverview = binding.tvOverview;
            ivPoster = binding.ivPoster;
            /* Not using View Binding Library
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
             */
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int radius = 30;

            //if phone is in landscape
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //then imageUrl = back drop image
                imageUrl = movie.getBackdropPath();
                Glide.with(activity).load(imageUrl).centerCrop().transform(new CenterInside(), new RoundedCorners(radius)).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivPoster);
            } else {
                // else imageUrl = poster image
                imageUrl = movie.getPosterPath();
                Glide.with(activity).load(imageUrl).centerCrop().transform(new CenterInside(), new RoundedCorners(radius)).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }

        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(activity, DetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                activity.startActivity(intent);
            }
        }
    }

}
