package com.example.bbcnewsctw.services;


import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsServicesTest {

    //Endpoint error response codes
    private final int BAD_REQUEST = 400;
    private final int API_KEY_INVALID = 401;

    private Response httpResponse;

    @Before
    public void setupApiConnection(){
        String source = "bbc-news";
        String apiKey = "646d663b7b7848648c16acd2e2814126";

        Request request = new Request.Builder()
                .url(String.format("https://newsapi.org/v2/top-headlines?sources=%s&apiKey=%s", source, apiKey))
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        try {
            httpResponse = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateConnection(){
        assertThat(httpResponse.isSuccessful()).isTrue();
    }

    @Test
    public void validateUrl(){
        assertThat(httpResponse.code() != BAD_REQUEST).isTrue();
    }

    @Test
    public void validateApiKey(){
        assertThat(httpResponse.code() != API_KEY_INVALID).isTrue();
    }

}