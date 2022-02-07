package com.example.bbcnewsctw.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsModel implements Serializable {

    @SerializedName("articles")
    private ArrayList<ArticleModel> articles;

    public NewsModel() {
    }

    public ArrayList<ArticleModel> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<ArticleModel> articles) {
        this.articles = articles;
    }
}
