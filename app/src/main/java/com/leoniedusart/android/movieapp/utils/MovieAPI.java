package com.leoniedusart.android.movieapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.leoniedusart.android.movieapp.R;
import com.leoniedusart.android.movieapp.models.Movie;
import com.leoniedusart.android.movieapp.models.MovieList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public interface MovieAPI {
    OkHttpClient mOkHttpClient = new OkHttpClient();

    default void apiCall(Context context, String url) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Request request = new Request.Builder().url(url).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUser(context, R.string.pb);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String stringJson = Objects.requireNonNull(response.body()).string();
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess(stringJson);
                            }
                        });
                    } else {
                        alertUser(context, R.string.pb);
                    }
                }
            });
        } else {
            alertUser(context, R.string.no_internet);
        }
    }

    default void alertUser(Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity)context).finish();
                    }
                });
        builder.create().show();
    }

    void onSuccess(String stringJson);
}
