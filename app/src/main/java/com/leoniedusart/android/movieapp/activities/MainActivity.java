package com.leoniedusart.android.movieapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.adapters.FavoriteAdapter;
import com.leoniedusart.android.movieapp.adapters.SearchAdapter;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.utils.DataKeys;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewBonjour;
    private Context mContext;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private RecyclerView mRecyclerViewFavoriteMovieList;
    private FavoriteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewBonjour = (TextView) findViewById(R.id.text_view_bonjour);
        mTextViewBonjour.setText(getString(R.string.hello, "Léonie"));

        mContext = this;

        mRecyclerViewFavoriteMovieList = findViewById(R.id.recycler_view_movie_list);
        mRecyclerViewFavoriteMovieList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FavoriteAdapter(mContext, mMovies);
        mRecyclerViewFavoriteMovieList.setAdapter(mAdapter);

        updateUi();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUi();
    }

    public void onClickStartSearch(View view)
    {
        Intent intent = new Intent(mContext, SearchActivity.class);
        startActivity(intent);
    }

    public void updateUi() {
        mMovies.removeAll(mMovies);
        SharedPreferences sharedPref = getSharedPreferences("Leonie_test", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Gson gson = new Gson();
            Movie movie = gson.fromJson(entry.getValue().toString(), Movie.class);
            mMovies.add(movie);
        }
        mAdapter.notifyDataSetChanged();
    }
}