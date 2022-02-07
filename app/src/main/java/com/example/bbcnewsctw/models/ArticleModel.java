package com.example.bbcnewsctw.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.bbcnewsctw.R;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ArticleModel implements Serializable {
    @SerializedName("source")
    private SourceModel source;
    @SerializedName("author")
    private String author;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;
    @SerializedName("urlToImage")
    private String urlToImage;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("content")
    private String content;

    public ArticleModel() {
    }

    public SourceModel getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateAndAuthor() {
        return String.format("%s, %s",
                author != null ? author.toUpperCase() : "",
                publishedAt != null ? publishedAt : "");
    }

    /**
     Load image using Picasso and binding it to ImageView
     **/
    @BindingAdapter("bind:imageUrl")
    public static void setImage(ImageView imageView, String imageUrl){

        Picasso picasso = Picasso.get();
        picasso
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);

        //Set a triangle on imageview left up corner to show if the image is loading from memory/disk cache or network
        //Green: Memory; Blue: Disk; Red: Network
        picasso.setIndicatorsEnabled(true);
    }
}
