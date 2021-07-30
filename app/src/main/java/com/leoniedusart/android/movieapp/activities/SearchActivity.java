package com.leoniedusart.android.movieapp.activities;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private OkHttpClient mOkHttpClient;
    private RecyclerView mRecyclerViewMovieList;
    private SearchAdapter mAdapter;
    private EditText mEditTextSearch;

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
    }

    public void onClickMovieSearch(View view)
    {
        mOkHttpClient = new OkHttpClient();
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Request request = new Request.Builder().url(String.format("http://omdbapi.com/?s=%s&apikey=bf4e1adb&plot=full", mEditTextSearch.getText())).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUser(R.string.pb);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String stringJson = Objects.requireNonNull(response.body()).string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Code exécuté dans le Thread principale
                                Gson gson = new Gson();
                                MovieList movieList = gson.fromJson(stringJson, MovieList.class);
                                ArrayList<Movie> result = movieList.getSearch();
                                if(result == null) {
                                    Snackbar.make(view, movieList.getError(), Snackbar.LENGTH_LONG).show();
                                } else {
                                    mMovies.removeAll(mMovies);
                                    mMovies.addAll(result);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        alertUser(R.string.pb);
                    }
                }
            });
        } else {
            alertUser(R.string.no_internet);
        }
    }

    private void alertUser(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.create().show();
    }
}