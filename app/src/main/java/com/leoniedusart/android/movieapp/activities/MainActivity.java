package com.leoniedusart.android.movieapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.utils.DataKeys;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewBonjour;
    private Button mButtonSearch;
    private LinearLayout mLinearLayoutMovieList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewBonjour = (TextView) findViewById(R.id.text_view_bonjour);
        mTextViewBonjour.setText(getString(R.string.hello, "Léonie"));

        mContext = this;

        mLinearLayoutMovieList = (LinearLayout) findViewById(R.id.linear_layout_movie_list);
        int nbMovies = mLinearLayoutMovieList.getChildCount();
        for(int i = 0; i < nbMovies; i++) {
            View movieCard = mLinearLayoutMovieList.getChildAt(i);
            if(movieCard instanceof LinearLayout) {
                movieCard.findViewById(R.id.text_view_movie_title);
                ((TextView) movieCard.findViewById(R.id.text_view_movie_title)).setText(String.format("Titre du film %d", i));
            }
        }
    }

    public void onClickStartSearch(View view)
    {
        Intent intent = new Intent(mContext, SearchActivity.class);
        startActivity(intent);
    }

    public void onClickMovieCard(View view)
    {
        Intent intent = new Intent(mContext, MovieActivity.class);
        intent.putExtra(DataKeys.movieIdKey, ((TextView)view.findViewById(R.id.text_view_movie_id)).getText());
        startActivity(intent);
    }
}