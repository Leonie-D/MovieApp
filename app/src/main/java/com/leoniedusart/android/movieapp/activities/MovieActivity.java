package com.leoniedusart.android.movieapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolBarLayout;
    private FloatingActionButton fab;
    private Context mContext;
    private LinearLayout mLinearLayoutMovieDetails;
    private TextView mTextViewId;
    private TextView mTextViewContent;
    private TextView mTextViewSummary;
    private TextView mTextViewDate;
    private TextView mTextViewCategories;
    private TextView mTextViewActors;
    private TextView mTextViewAwards;
    private TextView mTextViewDirector;
    private ImageView mImageViewContent;
    private ProgressBar mProgressBar;
    private Movie mMovie;
    private OkHttpClient mOkHttpClient;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mContext = this;

        final String movieId = getIntent().getExtras().getString(DataKeys.movieIdKey);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                if (tag.equals("empty"))
                {
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_baseline_favorite_24);
                    view.setTag("filled");
                }
                else
                {
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    view.setTag("empty");
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

        mOkHttpClient = new OkHttpClient();
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Request request = new Request.Builder().url(String.format("http://www.omdbapi.com/?i=%s&apikey=bf4e1adb&plot=full", movieId)).build();
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
                                mMovie = gson.fromJson(stringJson, Movie.class);
                                ((MovieActivity)mContext).updateUi();
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
}