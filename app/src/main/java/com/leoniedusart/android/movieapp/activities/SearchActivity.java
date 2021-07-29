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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;

        mRecyclerViewMovieList = findViewById(R.id.recycler_view_movie_list);
        mRecyclerViewMovieList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SearchAdapter(mContext, mMovies);
        mRecyclerViewMovieList.setAdapter(mAdapter);

        mOkHttpClient = new OkHttpClient();
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Request request = new Request.Builder().url("http://omdbapi.com/?s=star%20wars&apikey=bf4e1adb&plot=full").build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
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
                                mMovies.addAll(gson.fromJson(stringJson, MovieList.class).getSearch());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(R.string.pb);
                        builder.setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.create().show();
                    }
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.no_internet);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            builder.create().show();
        }
    }

    public void onClickMovieCard(View view)
    {
        Intent intent = new Intent(mContext, MovieActivity.class);
        Log.d("LeonieTag", ((TextView)view.findViewById(R.id.text_view_movie_id)).getText().toString());
        intent.putExtra(DataKeys.movieIdKey, ((TextView)view.findViewById(R.id.text_view_movie_id)).getText());
        startActivity(intent);
    }
}