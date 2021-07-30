package com.leoniedusart.android.movieapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.utils.DataKeys;
import com.leoniedusart.android.movieapp.utils.MovieAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity implements MovieAPI {

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolBarLayout;
    private FloatingActionButton fab;
    private Context mContext;
    private boolean isFavoriteMovie;
    private LinearLayout mLinearLayoutMovieDetails;
    private TextView mTextViewContent;
    private TextView mTextViewSummary;
    private TextView mTextViewDate;
    private TextView mTextViewCategories;
    private TextView mTextViewActors;
    private TextView mTextViewAwards;
    private TextView mTextViewDirector;
    private ImageView mImageViewContent;
    private ProgressBar mProgressBar;
    private String mJsonMovie;
    private Movie mMovie;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mContext = this;
        SharedPreferences sharedPref = getSharedPreferences("Leonie_test", Context.MODE_PRIVATE);

        final String movieId = getIntent().getExtras().getString(DataKeys.movieIdKey);
        isFavoriteMovie = sharedPref.contains(movieId);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        fab = findViewById(R.id.fab);
        if(isFavoriteMovie) {
            fab.setImageResource(R.drawable.ic_baseline_favorite_24);
            fab.setTag("filled");
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                SharedPreferences.Editor editor = sharedPref.edit();
                if (tag.equals("empty"))
                {
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_baseline_favorite_24);
                    view.setTag("filled");
                    editor.putString(movieId, mJsonMovie);
                    editor.apply();
                }
                else
                {
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    view.setTag("empty");
                    editor.remove(movieId);
                    editor.apply();
                }
            }
        });

        mProgressBar = findViewById(R.id.indeterminateBar);
        mLinearLayoutMovieDetails = findViewById(R.id.linear_layout_movie_details);

        mTextViewSummary = findViewById(R.id.text_view_content_summary);
        mTextViewContent = findViewById(R.id.text_view_content_title);
        mTextViewDate = findViewById(R.id.text_view_date);
        mTextViewCategories = findViewById(R.id.text_view_categories);
        mTextViewActors = findViewById(R.id.text_view_actors);
        mTextViewAwards = findViewById(R.id.text_view_awards);
        mTextViewDirector = findViewById(R.id.text_view_director);
        mImageViewContent = findViewById(R.id.image_view_content);

        apiCall(mContext, String.format("http://www.omdbapi.com/?i=%s&apikey=bf4e1adb&plot=full", movieId));
    }

    public void updateUi()
    {
        mTextViewContent.setText(mMovie.getTitle());
        mTextViewSummary.setText(mMovie.getPlot());
        mTextViewDate.setText(mMovie.getReleased());
        mTextViewDirector.setText(mMovie.getDirector());
        mTextViewAwards.setText(mMovie.getAwards());
        mTextViewCategories.setText(mMovie.getGenre());
        mTextViewActors.setText(mMovie.getActors());
        Picasso.get().load(mMovie.getPoster()).into(mImageViewContent);

        mProgressBar.setVisibility(View.INVISIBLE);
        mLinearLayoutMovieDetails.setVisibility(View.VISIBLE);
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

    @Override
    public void onSuccess(String stringJson) {
        Gson gson = new Gson();
        mJsonMovie = stringJson;
        mMovie = gson.fromJson(stringJson, Movie.class);
        ((MovieActivity)mContext).updateUi();
    }
}