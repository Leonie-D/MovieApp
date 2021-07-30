package com.leoniedusart.android.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.activities.MovieActivity;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.utils.DataKeys;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Movie> mMovies;

    // Constructor
    public FavoriteAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @NonNull
    @NotNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_card, parent, false);
        return new FavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoriteAdapter.ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mTextViewMovieId.setText(movie.getImdbID());
        holder.mTextViewMovieTitle.setText(movie.getTitle());
        holder.mTextViewMovieDesc.setText(movie.getPlot());
        holder.mTextViewMovieReleased.setText(movie.getReleased());
        Picasso.get().load(movie.getPoster()).into(holder.mImageViewMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    // Classe holder qui contient la vue d’un item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // éléments d'un item de liste
        public TextView mTextViewMovieTitle;
        public ImageView mImageViewMoviePoster;
        public TextView mTextViewMovieId;
        public TextView mTextViewMovieReleased;
        public TextView mTextViewMovieDesc;

        public ViewHolder(View view) {
            super(view);
            mTextViewMovieTitle = view.findViewById(R.id.text_view_movie_title);
            mImageViewMoviePoster = view.findViewById(R.id.image_view_movie);
            mTextViewMovieId = view.findViewById(R.id.text_view_movie_id);
            mTextViewMovieReleased = view.findViewById(R.id.text_view_movie_released);
            mTextViewMovieDesc = view.findViewById(R.id.text_view_movie_summary);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, MovieActivity.class);
            intent.putExtra(DataKeys.movieIdKey, mTextViewMovieId.getText());
            mContext.startActivity(intent);
        }
    }
}
