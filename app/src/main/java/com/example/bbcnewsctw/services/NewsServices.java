package com.example.bbcnewsctw.services;

import android.app.Activity;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.bbcnewsctw.R;
import com.example.bbcnewsctw.Utils;
import com.example.bbcnewsctw.models.NewsModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NewsServices {

    public interface CompletionHandler<T> {
        void onValue(T value);
    }

    /**
     * Fetch the top headlines news from a specific URL
     * OkHttpClient used for request
     */
    public void fetchTopHeadlines(Activity activity, ProgressBar progressBar, final CompletionHandler<NewsModel> completion) {
        Request request = Utils.getRequest().build();

        Utils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(activity.getString(R.string.error_fetching_news), e.getMessage());
                activity.runOnUiThread(()->Utils.connectionError(activity, progressBar));
                completion.onValue(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = Objects.requireNonNull(response.body()).string();
                    Gson gson = new GsonBuilder().create();
                    NewsModel data = gson.fromJson(body, NewsModel.class);

                    completion.onValue(data != null ? data : new NewsModel());

                } else {
                    String stringResponse = Objects.requireNonNull(response.body()).string();
                    Log.e(activity.getString(R.string.error_fetching_news), !stringResponse.isEmpty() ? stringResponse : String.valueOf(response.code()));
                    activity.runOnUiThread(()->Utils.connectionError(activity, progressBar));
                    completion.onValue(null);
                }
            }
        });
    }

}
