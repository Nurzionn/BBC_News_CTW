package com.example.bbcnewsctw;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bbcnewsctw.services.NewsServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Utils {
    public static final String ARTICLE_PARAM = "Article";

    private static NewsServices newsServices;
    private static OkHttpClient client;
    private static Request.Builder request;

    /**
     * Config client connection timeouts
     * Config specific URL for requests
     * Instantiate the Services Class, where the request methods are
     *
     * The URL variables are saved in BuildConfig file, and can be changed on productFlavours in build.gradle(app)
     */
    public static void setAPIConnectionConfig() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        request = new Request.Builder()
                .url(String.format("%s%s&apiKey=%s", BuildConfig.BASE_URL, BuildConfig.NEWS_SOURCE,BuildConfig.API_KEY));

        newsServices = new NewsServices();
    }

    /**
     * Change Date format from API, to show a cleaner format to the user
     */
    public static String changeDateFormat(String dateNotFormatted){
        String dateFormatted = "";
        Date date;

        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    .parse(dateNotFormatted);

            dateFormatted = date!=null? new SimpleDateFormat("d MMM, HH:mm", Locale.getDefault())
                    .format(date):dateNotFormatted;
        } catch (ParseException e) {
            e.printStackTrace();
            dateFormatted = dateNotFormatted;
        }

        return dateFormatted.replace(", ", "\n");
    }

    /**
     * Trigger when a connection problem from a request exists
     */
    public static void connectionError(Activity activity, ProgressBar progressBar){
        progressBar.setVisibility(View.GONE);
        Toast.makeText(activity, activity.getString(R.string.something_wrong_with_connection), Toast.LENGTH_SHORT).show();
    }

    public static NewsServices getNewsServices() {
        return newsServices;
    }
    public static OkHttpClient getClient() {
        return client;
    }

    public static Request.Builder getRequest() {
        return request;
    }
}
