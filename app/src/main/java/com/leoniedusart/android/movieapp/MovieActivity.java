package com.leoniedusart.android.movieapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.internal.SafeIterableMap;

import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leoniedusart.android.movieapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MovieActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolBarLayout;
    private FloatingActionButton fab;
    private TextView mTextViewContent;
    private TextView mTextViewSummary;
    private TextView mTextViewDate;
    private TextView mTextViewCategories;
    private TextView mTextViewActors;
    private TextView mTextViewAwards;
    private TextView mTextViewDirector;
    private ImageView mImageViewContent;
    private Movie movie;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        try {
            is = getAssets().open("movieData");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String movieData = sb.toString();

        Log.d("LeonieTag", movieData);
        Gson gson = new Gson();
        try {
            movie = gson.fromJson(movieData, Movie.class);
        } catch(Exception e) {
            Log.d("LeonieTag", e.getMessage());
        }

        Log.d("LeonieTag", movie.getTitle());

        mTextViewSummary = findViewById(R.id.text_view_content_summary);
        mTextViewContent = findViewById(R.id.text_view_content_title);
        mTextViewDate = findViewById(R.id.text_view_date);
        mTextViewCategories = findViewById(R.id.text_view_categories);
        mTextViewActors = findViewById(R.id.text_view_actors);
        mTextViewAwards = findViewById(R.id.text_view_awards);
        mTextViewDirector = findViewById(R.id.text_view_director);
        mImageViewContent = findViewById(R.id.image_view_content);

        updateUi();
    }

    public void updateUi()
    {
        mTextViewContent.setText(movie.getTitle());
        mTextViewSummary.setText(movie.getPlot());
        mTextViewDate.setText(movie.getReleased());
        mTextViewDirector.setText(movie.getDirector());
        mTextViewAwards.setText(movie.getAwards());
        mTextViewCategories.setText(movie.getGenre());
        mTextViewActors.setText(movie.getActors());
        Picasso.get().load(movie.getPoster()).into(mImageViewContent);
    }

    public void onClickReadMore(View view) {
        int nbMaxLines = mTextViewSummary.getMaxLines();
        if(nbMaxLines > 4)
        {
            mTextViewSummary.setMaxLines(4);
            mTextViewSummary.setEllipsize(TextUtils.TruncateAt.END);
            ((TextView) view).setText(R.string.read_more);
        }
        else
        {
            mTextViewSummary.setMaxLines(Integer.MAX_VALUE);
            mTextViewSummary.setEllipsize(null);
            ((TextView) view).setText(R.string.read_less);
        }
    }
}