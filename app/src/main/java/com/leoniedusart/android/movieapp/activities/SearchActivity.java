package com.leoniedusart.android.movieapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.adapters.SearchAdapter;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.models.MovieList;
import com.leoniedusart.android.movieapp.utils.DataKeys;
import com.leoniedusart.android.movieapp.utils.MovieAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements MovieAPI {
    private Context mContext;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private ArrayList<String> mJsonMovies = new ArrayList<>();
    private RecyclerView mRecyclerViewMovieList;
    private SearchAdapter mAdapter;
    private EditText mEditTextSearch;
    private int mPageNumber = 1;
    private int mNbPages = 0;
    private boolean mIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;
        mEditTextSearch = findViewById(R.id.edit_text_search);

        mRecyclerViewMovieList = findViewById(R.id.recycler_view_movie_list);
        mRecyclerViewMovieList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SearchAdapter(mContext, mMovies);
        mRecyclerViewMovieList.setAdapter(mAdapter);

        mRecyclerViewMovieList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == mMovies.size() - 1 && mPageNumber < mNbPages && mMovies.size() != 0) {
                    // Il n'est pas dejà entrain de charger les suivants
                    // Il affiche le dernier élément
                    // Le nombre de page est supérieur à 0
                    // Le nombre de résultat est supérieur à 0
                    // Dans ce cas on télécharge les résultats suivants
                    mPageNumber++;
                    apiCall(mContext, String.format("http://omdbapi.com/?s=%s&apikey=bf4e1adb&plot=full&page=%d", mEditTextSearch.getText(), mPageNumber), false);
                    mIsLoading = true;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("movies", mJsonMovies);
        savedInstanceState.putInt("nbPages", mNbPages);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mJsonMovies = savedInstanceState.getStringArrayList("movies");
        mNbPages = savedInstanceState.getInt("nbPages");
        for(int i = 0; i < mJsonMovies.size(); i++) {
            Gson gson = new Gson();
            mMovies.add(gson.fromJson(mJsonMovies.get(i), Movie.class));
        }
        mAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }

    public void onClickMovieSearch(View view)
    {
        apiCall(mContext, String.format("http://omdbapi.com/?s=%s&apikey=bf4e1adb&plot=full&page=%d", mEditTextSearch.getText(), mPageNumber), true);
    }

    @Override
    public void onSuccess(String stringJson, boolean clear) {
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(stringJson, MovieList.class);
        ArrayList<Movie> result = movieList.getSearch();
        if(result == null) {
            Snackbar.make(mRecyclerViewMovieList, movieList.getError(), Snackbar.LENGTH_LONG).show();
        } else {
            mNbPages = (Integer.parseInt(movieList.getTotalResults()) / 10) + 1;
            Log.d("LeonieTag", String.valueOf(mMovies.size()));
            if(clear) {
                mMovies.removeAll(mMovies);
                mJsonMovies.removeAll(mJsonMovies);
            } else if (mNbPages > 1) {
                mMovies.remove(mMovies.size()-1);
            }
            Log.d("LeonieTag", String.valueOf(mMovies.size()));
            for(int i = 0; i < result.size(); i++) {
                mJsonMovies.add(gson.toJson(result.get(i)));
            }
            mMovies.addAll(result);
            Log.d("LeonieTag", String.valueOf(mMovies.size()));

            // ajout du loader s'il reste des pages à appeler
            if(mPageNumber < mNbPages) {
                mMovies.add(null);
            }

            Log.d("LeonieTag", String.valueOf(mMovies.size()));

            mIsLoading = false;
            mAdapter.notifyDataSetChanged();
        }
    }
}