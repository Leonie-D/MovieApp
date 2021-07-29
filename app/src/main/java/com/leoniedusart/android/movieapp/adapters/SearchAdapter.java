package com.leoniedusart.android.movieapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.utils.Helper;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Movie> mMovies;

    // Constructor
    public SearchAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mTextViewMovieId.setText(movie.getImdbID());
        holder.mTextViewMovieTitle.setText(movie.getTitle());
        Picasso.get().load(movie.getPoster()).into(holder.mImageViewMoviePoster);
        for (int i = 0; i < holder.mViewsToHide.size(); i++) {
            holder.mViewsToHide.get(i).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    // Classe holder qui contient la vue d’un item
    public class ViewHolder extends RecyclerView.ViewHolder {
        // éléments d'un item de liste
        public TextView mTextViewMovieTitle;
        public ImageView mImageViewMoviePoster;
        public ArrayList<View> mViewsToHide;
        public TextView mTextViewMovieId;

        public ViewHolder(View view) {
            super(view);
            mTextViewMovieTitle = view.findViewById(R.id.text_view_movie_title);
            mImageViewMoviePoster = view.findViewById(R.id.image_view_movie);
            mViewsToHide = Helper.getViewsByTag((ViewGroup) view, "hide_on_search");
            mTextViewMovieId = view.findViewById(R.id.text_view_movie_id);
        }
    }
}
