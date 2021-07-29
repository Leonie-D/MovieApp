package com.leoniedusart.android.movieapp.activities;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewBonjour = (TextView) findViewById(R.id.text_view_bonjour);
        mTextViewBonjour.setText(getString(R.string.hello, "Léonie"));
        //Toast.makeText(this, mTextViewBonjour.getText(), Toast.LENGTH_SHORT).show();

        //mButtonSearch = (Button) findViewById(R.id.button_search);
        //mButtonSearch.setOnClickListener(mouseEvent -> {
        //});

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
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void onClickMovieCard(View view)
    {
        //Toast.makeText(this, ((TextView)view.findViewById(R.id.text_view_movie_title)).getText(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(DataKeys.movieTitleKey, ((TextView)view.findViewById(R.id.text_view_movie_title)).getText());
        intent.putExtra(DataKeys.movieSummaryKey, ((TextView)view.findViewById(R.id.text_view_movie_summary)).getText());
        startActivity(intent);
    }
}