package com.leoniedusart.android.movieapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.activities.MovieActivity;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.utils.DataKeys;
import com.leoniedusart.android.movieapp.utils.Helper;
import com.leoniedusart.android.movieapp.utils.MovieAPI;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        if(movie != null) {
            holder.mLoader.setVisibility(View.INVISIBLE);
            holder.mRelativeLayoutMovieCard.setVisibility(View.VISIBLE);
            holder.mTextViewMovieId.setText(movie.getImdbID());
            holder.mTextViewMovieTitle.setText(movie.getTitle());
            Picasso.get().load(movie.getPoster()).into(holder.mImageViewMoviePoster);
            for (int i = 0; i < holder.mViewsToHide.size(); i++) {
                holder.mViewsToHide.get(i).setVisibility(View.INVISIBLE);
            }
        } else {
            holder.mRelativeLayoutMovieCard.setVisibility(View.INVISIBLE);
            holder.mLoader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    // Classe holder qui contient la vue d’un item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, MovieAPI {
        // éléments d'un item de liste
        public TextView mTextViewMovieTitle;
        public ImageView mImageViewMoviePoster;
        public ArrayList<View> mViewsToHide;
        public TextView mTextViewMovieId;
        public boolean isFavoriteMovie;
        public FloatingActionButton mFavoriteButton;
        public RelativeLayout mRelativeLayoutMovieCard;
        public ProgressBar mLoader;

        public ViewHolder(View view) {
            super(view);

            mRelativeLayoutMovieCard = view.findViewById(R.id.relative_layout_movie_card);
            mLoader = view.findViewById(R.id.indeterminateBar);
            mTextViewMovieTitle = view.findViewById(R.id.text_view_movie_title);
            mImageViewMoviePoster = view.findViewById(R.id.image_view_movie);
            mViewsToHide = Helper.getViewsByTag((ViewGroup) view, "hide_on_search");
            mTextViewMovieId = view.findViewById(R.id.text_view_movie_id);
            mFavoriteButton = view.findViewById(R.id.button_favorite);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);

            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = (String) view.getTag();
                    if (tag.equals("empty"))
                    {
                       apiCall(mContext, String.format("http://www.omdbapi.com/?i=%s&apikey=bf4e1adb&plot=full", mTextViewMovieId.getText().toString()), false);
                    }
                    else
                    {
                        SharedPreferences sharedPref = mContext.getSharedPreferences("Leonie_test", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        ((FloatingActionButton)view).setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        view.setTag("empty");
                        editor.remove(mTextViewMovieId.getText().toString());
                        editor.apply();
                    }
                }
            });
        }

        @Override
        public boolean onLongClick(View view) {
            SharedPreferences sharedPref = mContext.getSharedPreferences("Leonie_test", Context.MODE_PRIVATE);
            isFavoriteMovie = sharedPref.contains(mTextViewMovieId.getText().toString());

            if(isFavoriteMovie) {
                mFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                mFavoriteButton.setTag("filled");
            }

            mFavoriteButton.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, MovieActivity.class);
            intent.putExtra(DataKeys.movieIdKey, mTextViewMovieId.getText());
            mContext.startActivity(intent);
        }

        @Override
        public void onSuccess(String stringJson, boolean clear) {
            SharedPreferences sharedPref = mContext.getSharedPreferences("Leonie_test", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            mFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            mFavoriteButton.setTag("filled");
            editor.putString(mTextViewMovieId.getText().toString(), stringJson);
            editor.apply();
        }
    }
}
